package com.sap.resource;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class FileDownloadTracker {

	@Id
	private String id;
	
	private Long fileNum;
	
	private String status;
	
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getFileNum() {
		return fileNum;
	}

	public void setFileNum(Long fileNum) {
		this.fileNum = fileNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
