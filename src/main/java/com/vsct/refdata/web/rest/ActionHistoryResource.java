package com.vsct.refdata.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vsct.refdata.service.ActionHistoryService;
import com.vsct.refdata.web.rest.errors.BadRequestAlertException;
import com.vsct.refdata.web.rest.util.HeaderUtil;
import com.vsct.refdata.web.rest.util.PaginationUtil;
import com.vsct.refdata.service.dto.ActionHistoryDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ActionHistory.
 */
@RestController
@RequestMapping("/api")
public class ActionHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ActionHistoryResource.class);

    private static final String ENTITY_NAME = "actionHistory";

    private final ActionHistoryService actionHistoryService;

    public ActionHistoryResource(ActionHistoryService actionHistoryService) {
        this.actionHistoryService = actionHistoryService;
    }

    /**
     * POST  /action-histories : Create a new actionHistory.
     *
     * @param actionHistoryDTO the actionHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new actionHistoryDTO, or with status 400 (Bad Request) if the actionHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/action-histories")
    @Timed
    public ResponseEntity<ActionHistoryDTO> createActionHistory(@Valid @RequestBody ActionHistoryDTO actionHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save ActionHistory : {}", actionHistoryDTO);
        if (actionHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new actionHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActionHistoryDTO result = actionHistoryService.save(actionHistoryDTO);
        return ResponseEntity.created(new URI("/api/action-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /action-histories : Updates an existing actionHistory.
     *
     * @param actionHistoryDTO the actionHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated actionHistoryDTO,
     * or with status 400 (Bad Request) if the actionHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the actionHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/action-histories")
    @Timed
    public ResponseEntity<ActionHistoryDTO> updateActionHistory(@Valid @RequestBody ActionHistoryDTO actionHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update ActionHistory : {}", actionHistoryDTO);
        if (actionHistoryDTO.getId() == null) {
            return createActionHistory(actionHistoryDTO);
        }
        ActionHistoryDTO result = actionHistoryService.save(actionHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, actionHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /action-histories : get all the actionHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of actionHistories in body
     */
    @GetMapping("/action-histories")
    @Timed
    public ResponseEntity<List<ActionHistoryDTO>> getAllActionHistories(Pageable pageable) {
        log.debug("REST request to get a page of ActionHistories");
        Page<ActionHistoryDTO> page = actionHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/action-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /action-histories/:id : get the "id" actionHistory.
     *
     * @param id the id of the actionHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the actionHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/action-histories/{id}")
    @Timed
    public ResponseEntity<ActionHistoryDTO> getActionHistory(@PathVariable Long id) {
        log.debug("REST request to get ActionHistory : {}", id);
        ActionHistoryDTO actionHistoryDTO = actionHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(actionHistoryDTO));
    }

    /**
     * DELETE  /action-histories/:id : delete the "id" actionHistory.
     *
     * @param id the id of the actionHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/action-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteActionHistory(@PathVariable Long id) {
        log.debug("REST request to delete ActionHistory : {}", id);
        actionHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
