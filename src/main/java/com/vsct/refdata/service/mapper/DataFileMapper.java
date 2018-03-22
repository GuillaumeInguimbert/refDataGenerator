package com.vsct.refdata.service.mapper;

import com.vsct.refdata.domain.*;
import com.vsct.refdata.service.dto.DataFileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DataFile and its DTO DataFileDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DataFileMapper extends EntityMapper<DataFileDTO, DataFile> {


    @Mapping(target = "actionHistories", ignore = true)
    DataFile toEntity(DataFileDTO dataFileDTO);

    default DataFile fromId(Long id) {
        if (id == null) {
            return null;
        }
        DataFile dataFile = new DataFile();
        dataFile.setId(id);
        return dataFile;
    }
}
