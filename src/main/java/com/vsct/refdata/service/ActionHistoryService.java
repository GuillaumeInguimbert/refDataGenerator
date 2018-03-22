package com.vsct.refdata.service;

import com.vsct.refdata.service.dto.ActionHistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ActionHistory.
 */
public interface ActionHistoryService {

    /**
     * Save a actionHistory.
     *
     * @param actionHistoryDTO the entity to save
     * @return the persisted entity
     */
    ActionHistoryDTO save(ActionHistoryDTO actionHistoryDTO);

    /**
     * Get all the actionHistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ActionHistoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" actionHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ActionHistoryDTO findOne(Long id);

    /**
     * Delete the "id" actionHistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
