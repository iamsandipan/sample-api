package com.sap.resource;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

interface FileMetaDataRepository extends CrudRepository<FileMetaData, Long> {
	List<FileMetaData> findByName(String name);
}
