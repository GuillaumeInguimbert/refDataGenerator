package com.vsct.refdata.service.mapper;

import com.vsct.refdata.domain.*;
import com.vsct.refdata.service.dto.ActionHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ActionHistory and its DTO ActionHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {DataFileMapper.class})
public interface ActionHistoryMapper extends EntityMapper<ActionHistoryDTO, ActionHistory> {

    @Mapping(source = "dataFile.id", target = "dataFileId")
    ActionHistoryDTO toDto(ActionHistory actionHistory);

    @Mapping(source = "dataFileId", target = "dataFile")
    ActionHistory toEntity(ActionHistoryDTO actionHistoryDTO);

    default ActionHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActionHistory actionHistory = new ActionHistory();
        actionHistory.setId(id);
        return actionHistory;
    }
}
