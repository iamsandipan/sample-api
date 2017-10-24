package com.sap.resource;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

interface UserFileRepository extends CrudRepository<UserFile, Long> {
	Page<UserFile> findAll(Pageable pageable);

	@Query("from UserFile c where c.name=:name ")
	Iterable<UserFile> findByCategory(@Param("name") String name);

	@Query("from UserFile c where c.fileNum > :fileNum order by fileNum asc")
	List<UserFile> findNextFiles(@Param("fileNum") long fileNum, Pageable page);

	@Query("from UserFile c order by fileNum desc")
	List<UserFile> findMaxFileNum(Pageable page);

}
