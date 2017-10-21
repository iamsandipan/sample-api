package com.sap.resource;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FileMetaDataRepository extends CrudRepository<FileMetaData, Long> {
	List<FileMetaData> findByName(String name);
	FileMetaData findByNameAndSeq(String name, Integer seq);
}
