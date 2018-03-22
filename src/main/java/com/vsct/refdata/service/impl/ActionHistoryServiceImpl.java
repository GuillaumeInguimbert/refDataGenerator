package com.vsct.refdata.service.impl;

import com.vsct.refdata.service.ActionHistoryService;
import com.vsct.refdata.domain.ActionHistory;
import com.vsct.refdata.repository.ActionHistoryRepository;
import com.vsct.refdata.service.dto.ActionHistoryDTO;
import com.vsct.refdata.service.mapper.ActionHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ActionHistory.
 */
@Service
@Transactional
public class ActionHistoryServiceImpl implements ActionHistoryService {

    private final Logger log = LoggerFactory.getLogger(ActionHistoryServiceImpl.class);

    private final ActionHistoryRepository actionHistoryRepository;

    private final ActionHistoryMapper actionHistoryMapper;

    public ActionHistoryServiceImpl(ActionHistoryRepository actionHistoryRepository, ActionHistoryMapper actionHistoryMapper) {
        this.actionHistoryRepository = actionHistoryRepository;
        this.actionHistoryMapper = actionHistoryMapper;
    }

    /**
     * Save a actionHistory.
     *
     * @param actionHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ActionHistoryDTO save(ActionHistoryDTO actionHistoryDTO) {
        log.debug("Request to save ActionHistory : {}", actionHistoryDTO);
        ActionHistory actionHistory = actionHistoryMapper.toEntity(actionHistoryDTO);
        actionHistory = actionHistoryRepository.save(actionHistory);
        return actionHistoryMapper.toDto(actionHistory);
    }

    /**
     * Get all the actionHistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ActionHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ActionHistories");
        return actionHistoryRepository.findAll(pageable)
            .map(actionHistoryMapper::toDto);
    }

    /**
     * Get one actionHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ActionHistoryDTO findOne(Long id) {
        log.debug("Request to get ActionHistory : {}", id);
        ActionHistory actionHistory = actionHistoryRepository.findOne(id);
        return actionHistoryMapper.toDto(actionHistory);
    }

    /**
     * Delete the actionHistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ActionHistory : {}", id);
        actionHistoryRepository.delete(id);
    }
}
