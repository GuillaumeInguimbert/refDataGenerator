package com.vsct.refdata.service.dto;


import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ActionHistory entity.
 */
public class ActionHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String user;

    @NotNull
    private String message;

    @NotNull
    private LocalDate date;

    private Long dataFileId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getDataFileId() {
        return dataFileId;
    }

    public void setDataFileId(Long dataFileId) {
        this.dataFileId = dataFileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ActionHistoryDTO actionHistoryDTO = (ActionHistoryDTO) o;
        if(actionHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actionHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActionHistoryDTO{" +
            "id=" + getId() +
            ", user='" + getUser() + "'" +
            ", message='" + getMessage() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
