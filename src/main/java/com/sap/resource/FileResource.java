package com.sap.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/api")

/**
 * PUT https://www.googleapis.com/uploadtoweb HTTP/1.1 Content-Length: 524223
 * Content-Type: image/jpeg Content-Range: bytes 0-524223/2000000 [BYTES
 * 0-524287]
 **/

public class FileResource {

	@Autowired
	private FileMetaDataRepository fileMetaDataRepository;

	@RequestMapping(value = "/download/file/{name}", method = RequestMethod.GET)
	public void getFileByName(HttpServletResponse resp, @PathVariable String name) {
		System.out.println(name);
		List<FileMetaData> files = fileMetaDataRepository.findByName(name);
		Collections.sort(files, new SequenceComparator());
		System.out.println(files.size());
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		String fileName = "";
		try {
			for (FileMetaData file : files) {
				fileName = file.getName();
				bout.write(readfilePartFromLocation(file.getCdnUrl(), file.getReplicationUrls()));
			}
			resp.setContentType("application/octet-stream");
			resp.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileName + "\""));
			resp.getOutputStream().write(bout.toByteArray());
		} catch (Exception e) {
			resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
		}
	}

	@RequestMapping(value = "/download/file/{name}/part/{seq}", method = RequestMethod.GET)
	public void getFileByNameAndPart(HttpServletResponse resp, @PathVariable String name, @PathVariable Integer seq) {
		System.out.println(name);
		System.out.println(seq);

		Optional<FileMetaData> fileMd = getFileMetadataForPart(name, seq);
		if(fileMd.isPresent()){
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			String fileName = "";
			try {
					FileMetaData fmd = fileMd.get();
					fileName = fmd.getName();
					bout.write(readfilePartFromLocation(fmd.getCdnUrl(), fmd.getReplicationUrls()));
					resp.setContentType("application/octet-stream");
					resp.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileName + "\""));
					resp.getOutputStream().write(bout.toByteArray());
			} catch (Exception e) {
				resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
			}
		}
		
	}

	
	/*
	 * Upload a dummy file by calling this url
	 * 
	 * 
	 */
	@RequestMapping(value = "/upload/file/dummy", method = RequestMethod.GET)
	public void dummyUpload(HttpServletResponse resp) throws IOException {
		resp.getWriter().write(uploadFile());
		resp.setStatus(200);
	}

	private Optional<FileMetaData> getFileMetadataForPart(String name, int seq){
		List<FileMetaData> files = fileMetaDataRepository.findByName(name);
		for(FileMetaData metadata: files){
			if(metadata.getSeq() == seq){
				return Optional.of(metadata);
			}
		}
		return Optional.empty();
	}

	private byte[] readfilePartFromLocation(String cdnUrl, String repUrl) throws IOException {
		File f = new File(cdnUrl);
		if(f.exists()){
			InputStream inputStream = new BufferedInputStream(new FileInputStream(f));
			byte[] fileBytes = new byte[(int) f.length()];
			inputStream.read(fileBytes, 0, (int) f.length() - 1);
			inputStream.close();
			return fileBytes;

		}else{
			////This is dummy representing accessing file part from redundant location 

			return tryReadFromRedundantLocation(repUrl);
		}
	}

	private FileMetaData buildFileMetaData(String filename, int startByte, int endByte, int part, String cdnurl, String repurl) {
		FileMetaData fileMeta = new FileMetaData();
		fileMeta.setId(UUID.randomUUID().toString());
		fileMeta.setName(filename);
		fileMeta.setOwner("me");
		fileMeta.setCdnUrl(cdnurl);
		//Ideally some other location to me mentioned
		fileMeta.setReplicationUrls(repurl);
		fileMeta.setCreateDate(Calendar.getInstance().getTime());
		fileMeta.setModifyDate(Calendar.getInstance().getTime());
		fileMeta.setStartByte(startByte);
		fileMeta.setEndByte(endByte);
		fileMeta.setSeq(part);
		return fileMeta;
	}

	private String uploadFile() {
		String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();

		String srcdir = currentDir + "/data/";
		String cdndir1 = currentDir + "/cdn1/";
		String cdndir2 = currentDir + "/cdn2/";

		String filename = "test";
		File file = new File(srcdir + filename);
		long length = file.length();
		int chunksize = (int) (length / 2);
		int readBytes = 0;
		int startByte = 0;
		int endByte = 0;
		int part = 0;
		String storageFileName = UUID.randomUUID().toString();

		try {
			InputStream in = new FileInputStream(file);
			byte[] data = new byte[chunksize];
			while ((readBytes = in.read(data, 0, chunksize)) != -1) {
				endByte = startByte + readBytes;
				String cdnUrl = writeToCDN(cdndir1, storageFileName, part, data);
				String repUrl = writeToCDN(cdndir2, storageFileName, part, data);
				part++;
				FileMetaData fd = buildFileMetaData(storageFileName, startByte, endByte, part, cdnUrl, repUrl);
				fileMetaDataRepository.save(fd);
				startByte = endByte + 1;
				readBytes = 0;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "FileName : " + storageFileName + " : Parts " + part + ": " ;
	}
	
	public String writeToCDN(String cdndir, String filename, int part, byte[] data) throws IOException{
		String cdn_url = cdndir + filename+ "_" + part;
		BufferedOutputStream buf = new BufferedOutputStream(new FileOutputStream(cdn_url));
		buf.write(data);
		buf.close();
		return cdn_url;
	}

	private byte[] tryReadFromRedundantLocation(String repUrl) throws IOException{
		//This is dummy representing accessing file part from redundant location 
		File f = new File(repUrl);

		InputStream  inputStream = new BufferedInputStream(new FileInputStream(repUrl));
		byte[] fileBytes = new byte[(int) f.length()];
		inputStream.read(fileBytes, 0, (int) f.length() - 1);
		inputStream.close();
		return fileBytes;
	}
}
