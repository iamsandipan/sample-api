package com.sap.resource;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

interface FileDownloadTrackerRepository extends CrudRepository<FileDownloadTracker, Long>{
	  Page<FileDownloadTracker> findAll(Pageable pageable);
	  
	  @Query("from FileDownloadTracker c where c.status = :status order by file_num asc")
	  List<FileDownloadTracker> findFilesWithStatus(@Param("status") String status, Pageable page);

}
