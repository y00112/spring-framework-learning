package com.zhaoyss.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@MappedSuperclass
public class AbstractEntity {

    private long id;

    private Long createdAt;


    public long getId() {
        return id;
    }

    @Column
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Transient
    public ZonedDateTime getCreatedDateTime() {
        return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault());
    }

    @PrePersist
    public void preInsert() {
        setCreatedAt(System.currentTimeMillis());
    }
}
