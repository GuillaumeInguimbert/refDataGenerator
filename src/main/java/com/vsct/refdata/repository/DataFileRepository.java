package com.vsct.refdata.repository;

import com.vsct.refdata.domain.DataFile;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DataFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DataFileRepository extends JpaRepository<DataFile, Long> {

}
