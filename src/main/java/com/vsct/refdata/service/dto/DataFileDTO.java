package com.vsct.refdata.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.vsct.refdata.domain.enumeration.Type;

/**
 * A DTO for the DataFile entity.
 */
public class DataFileDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private String logicalName;

    @NotNull
    private Type type;

    @NotNull
    private String version;

    private String path;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLogicalName() {
        return logicalName;
    }

    public void setLogicalName(String logicalName) {
        this.logicalName = logicalName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DataFileDTO dataFileDTO = (DataFileDTO) o;
        if(dataFileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataFileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataFileDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", logicalName='" + getLogicalName() + "'" +
            ", type='" + getType() + "'" +
            ", version='" + getVersion() + "'" +
            ", path='" + getPath() + "'" +
            "}";
    }
}
