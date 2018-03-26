package com.vsct.refdata.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.vsct.refdata.domain.enumeration.Status;

import com.vsct.refdata.domain.enumeration.Type;

/**
 * A DataFile.
 */
@Entity
@Table(name = "data_file")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "jhi_date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "logical_name", nullable = false)
    private String logicalName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type", nullable = false)
    private Type type;

    @NotNull
    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "source_path")
    private String sourcePath;

    @Column(name = "taget_path")
    private String tagetPath;

    @OneToMany(mappedBy = "dataFile")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ActionHistory> actionHistories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public DataFile date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLogicalName() {
        return logicalName;
    }

    public DataFile logicalName(String logicalName) {
        this.logicalName = logicalName;
        return this;
    }

    public void setLogicalName(String logicalName) {
        this.logicalName = logicalName;
    }

    public Status getStatus() {
        return status;
    }

    public DataFile status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public DataFile type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public DataFile version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public DataFile sourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getTagetPath() {
        return tagetPath;
    }

    public DataFile tagetPath(String tagetPath) {
        this.tagetPath = tagetPath;
        return this;
    }

    public void setTagetPath(String tagetPath) {
        this.tagetPath = tagetPath;
    }

    public Set<ActionHistory> getActionHistories() {
        return actionHistories;
    }

    public DataFile actionHistories(Set<ActionHistory> actionHistories) {
        this.actionHistories = actionHistories;
        return this;
    }

    public DataFile addActionHistory(ActionHistory actionHistory) {
        this.actionHistories.add(actionHistory);
        actionHistory.setDataFile(this);
        return this;
    }

    public DataFile removeActionHistory(ActionHistory actionHistory) {
        this.actionHistories.remove(actionHistory);
        actionHistory.setDataFile(null);
        return this;
    }

    public void setActionHistories(Set<ActionHistory> actionHistories) {
        this.actionHistories = actionHistories;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataFile dataFile = (DataFile) o;
        if (dataFile.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataFile.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataFile{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", logicalName='" + getLogicalName() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", version='" + getVersion() + "'" +
            ", sourcePath='" + getSourcePath() + "'" +
            ", tagetPath='" + getTagetPath() + "'" +
            "}";
    }
}
