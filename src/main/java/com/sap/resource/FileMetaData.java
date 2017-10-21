package com.sap.resource;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FileMetaData {
	
	public FileMetaData(){}
	
	public FileMetaData(String fileName, int startByte, int endByte, int seq, String cdnUrl){
		this.name = fileName;
		this.startByte = startByte;
		this.endByte = endByte;
		this.seq = seq;
		this.cdn_url = cdnUrl;
	}
	
	
	@Id
    private String id;

    private String name;
    
    private String cdn_url;
    
    private String replicationUrls;

    private Date create_date;
    
    private Date modify_date;
    
    private String owner;
    
    private Integer startByte;
    
    private Integer endByte;
    
    private Integer cksum;
    
    private Integer seq;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCdnUrl() {
		return cdn_url;
	}

	public void setCdnUrl(String cdnUrl) {
		this.cdn_url = cdnUrl;
	}

	public Date getCreateDate() {
		return create_date;
	}

	public void setCreateDate(Date createDate) {
		this.create_date = createDate;
	}

	public Date getModifyDate() {
		return modify_date;
	}

	public void setModifyDate(Date modifyDate) {
		this.modify_date = modifyDate;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Integer getStartByte() {
		return startByte;
	}

	public void setStartByte(Integer startByte) {
		this.startByte = startByte;
	}

	public Integer getEndByte() {
		return endByte;
	}

	public void setEndByte(Integer endByte) {
		this.endByte = endByte;
	}

	public Integer getCksum() {
		return cksum;
	}

	public void setCksum(Integer cksum) {
		this.cksum = cksum;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getReplicationUrls() {
		return replicationUrls;
	}

	public void setReplicationUrls(String replicationUrls) {
		this.replicationUrls = replicationUrls;
	}

}
