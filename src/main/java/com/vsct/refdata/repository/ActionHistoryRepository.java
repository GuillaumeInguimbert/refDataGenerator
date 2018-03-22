package com.vsct.refdata.repository;

import com.vsct.refdata.domain.ActionHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ActionHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActionHistoryRepository extends JpaRepository<ActionHistory, Long> {

}
