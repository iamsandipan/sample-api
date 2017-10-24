package com.sap.resource;

import java.util.Comparator;

public class SequenceComparator implements Comparator<FileMetaData> {

	@Override
	public int compare(FileMetaData o1, FileMetaData o2) {
		if (o1 == null || o2 == null) {
			throw new RuntimeException("Cant compare camel and apple");
		}
		if (o1.getSeq() == o2.getSeq()) {
			return 0;
		}
		return o1.getSeq() > o2.getSeq() ? 1 : -1;
	}

}
