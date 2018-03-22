package com.vsct.refdata.service;

import com.vsct.refdata.service.dto.DataFileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing DataFile.
 */
public interface DataFileService {

    /**
     * Save a dataFile.
     *
     * @param dataFileDTO the entity to save
     * @return the persisted entity
     */
    DataFileDTO save(DataFileDTO dataFileDTO);

    /**
     * Get all the dataFiles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<DataFileDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dataFile.
     *
     * @param id the id of the entity
     * @return the entity
     */
    DataFileDTO findOne(Long id);

    /**
     * Delete the "id" dataFile.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
