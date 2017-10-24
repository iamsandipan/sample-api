package com.sap.resource;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FileUploadResource {

	@Autowired
	private FileMetaDataRepository fileMetaDataRepository;

	@Autowired
	private UserFileRepository userFileRepository;

	@PostConstruct
	private String uploadFile() {
		String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();

		String srcdir = currentDir + "/data/";
		String cdndir1 = currentDir + "/cdn1/";

		String filename = "test";
		File file = new File(srcdir + filename);
		long length = file.length();
		int chunksize = (int) (length / 2);
		int startByte = 0;
		int endByte = 0;
		int part = 0;

		try {
			InputStream in = new FileInputStream(file);
			byte[] data = new byte[chunksize];
			in.read(data, 0, chunksize);
			String cdnUrl = "";
			for (int i = 0; i < 100; i++) {
				String storageFileName = UUID.randomUUID().toString();
				UserFile f = new UserFile(UUID.randomUUID().toString(), storageFileName, "me", i);
				if (i == 0) {
					cdnUrl = writeToCDN(cdndir1, storageFileName, data);
					FileMetaData fd = buildFileMetaData(storageFileName, startByte, endByte, part, cdnUrl);
					fileMetaDataRepository.save(fd);

				}
				FileMetaData fd = buildFileMetaData(storageFileName, startByte, endByte, part, cdnUrl);
				fileMetaDataRepository.save(fd);
				userFileRepository.save(f);
			}
			in.close();
			return "Files Uploaded";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "No file created";
	}

	public String writeToCDN(String cdndir, String filename, byte[] data) throws IOException {
		String cdn_url = cdndir + filename;
		BufferedOutputStream buf = new BufferedOutputStream(new FileOutputStream(cdn_url));
		buf.write(data);
		buf.close();
		return cdn_url;
	}

	private FileMetaData buildFileMetaData(String filename, int startByte, int endByte, int part, String cdnurl) {
		FileMetaData fileMeta = new FileMetaData();
		fileMeta.setId(UUID.randomUUID().toString());
		fileMeta.setName(filename);
		fileMeta.setOwner("me");
		fileMeta.setCdnUrl(cdnurl);
		// Ideally some other location to me mentioned
		fileMeta.setReplicationUrls(cdnurl);
		fileMeta.setCreateDate(Calendar.getInstance().getTime());
		fileMeta.setModifyDate(Calendar.getInstance().getTime());
		fileMeta.setStartByte(startByte);
		fileMeta.setEndByte(endByte);
		fileMeta.setSeq(part);
		return fileMeta;
	}

}
