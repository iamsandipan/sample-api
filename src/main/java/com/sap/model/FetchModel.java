package com.sap.model;

public class FetchModel {
	private long startFileNum;
	private int numberToFetch;

	public long getStartFileNum() {
		return startFileNum;
	}
	public void setStartFileNum(long maxFileNumber) {
		this.startFileNum = maxFileNumber;
	}
	public int getNumberToFetch() {
		return numberToFetch;
	}
	public void setNumberToFetch(int numberToFetch) {
		this.numberToFetch = numberToFetch;
	}
	
}
