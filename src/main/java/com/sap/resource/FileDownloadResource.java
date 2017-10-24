package com.sap.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping(value = "/api")

public class FileDownloadResource {

	private static final String FILE_DOWNLOAD_ENDPOINT = "http://localhost:8080/api/download/file/";

	@Autowired
	private UserFileRepository userFileRepository;

	@Autowired
	private FileDownloadTrackerRepository fileDownloadTrackerRepository;

	@RequestMapping(value = "/download/files", method = RequestMethod.GET)
	public void downloadAll(HttpServletResponse resp) throws InterruptedException, ExecutionException {
		populateTrackerTable();
		startProcess();
		resp.setStatus(200);
	}

	@Async
	public void startProcess() throws InterruptedException, ExecutionException {

		List<FileDownloadTracker> files = fileDownloadTrackerRepository.findFilesWithStatus("NOT_STARTED",
				new PageRequest(0, 10));

		while (files.size() > 0) {
			for (FileDownloadTracker file : files) {
				asyncUpdate(fileDownloadTrackerRepository, file);
			}
			files = fileDownloadTrackerRepository.findFilesWithStatus("NOT_STARTED", new PageRequest(0, 10));
		}
	}

	@Async
	private void populateTrackerTable() {
		try {
			List<FileDownloadTracker> trackers = new ArrayList<FileDownloadTracker>(100);
			long maxFileNum = 0;
			List<UserFile> userfiles = userFileRepository.findNextFiles(0, new PageRequest(0, 10));
			while (!userfiles.isEmpty()) {
				for (int i = 0; i < userfiles.size(); i++) {
					UserFile file = userfiles.get(i);
					FileDownloadTracker tracker = new FileDownloadTracker();
					tracker.setId(UUID.randomUUID().toString());
					tracker.setFileNum(file.getFileNum());
					tracker.setStatus("NOT_STARTED");
					tracker.setName(file.getName());
					trackers.add(tracker);
					maxFileNum = file.getFileNum();
				}

				if (trackers.size() > 0) {
					fileDownloadTrackerRepository.save(trackers);
				}
				userfiles = userFileRepository.findNextFiles(maxFileNum, new PageRequest(0, 10));
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
		}
	}

	@Async
	public void asyncUpdate(FileDownloadTrackerRepository fileTrackerRepository, FileDownloadTracker fileTracker) {
		try {
			byte[] bytes = downloadFile(fileTracker);
			saveFileToLocalDir(fileTrackerRepository, fileTracker, bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	

	private byte[] downloadFile(FileDownloadTracker fileTracker) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		String url = FILE_DOWNLOAD_ENDPOINT + fileTracker.getName();
		ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
		return response.getBody();
	}

	private void saveFileToLocalDir(FileDownloadTrackerRepository fileTrackerRepository, FileDownloadTracker fileTracker,  byte[] bytes)
			throws FileNotFoundException, IOException {
		String currentDir = Paths.get(".").toAbsolutePath().normalize().toString();
		File f = new File(currentDir + "/download/" + fileTracker.getName());
		OutputStream outputStream = new FileOutputStream(f);
		outputStream.write(bytes);
		outputStream.close();
		fileTracker.setStatus("COMPLETED");
		fileTrackerRepository.save(fileTracker);
	}

}
