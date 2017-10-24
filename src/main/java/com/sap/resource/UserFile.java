package com.sap.resource;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserFile {

	public UserFile() {
	}

	public UserFile(String id, String name, String owner, long fileNum) {
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.fileNum = fileNum;
	}

	@Id
	private String id;

	private long fileNum;

	private String name;

	private String owner;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getFileNum() {
		return fileNum;
	}

	public void setFileNum(long fileNum) {
		this.fileNum = fileNum;
	}

}
