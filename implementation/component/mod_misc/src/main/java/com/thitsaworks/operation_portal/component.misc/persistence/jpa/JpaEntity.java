package com.thitsaworks.operation_portal.component.misc.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.Instant;

@Getter
@MappedSuperclass
public abstract class JpaEntity<ID extends JpaId<?>> {

    @Column(name = "created_date", updatable = false)
    @Convert(converter = JpaInstantConverter.class)
    protected Instant createdAt;

    @Column(name = "updated_date")
    @Convert(converter = JpaInstantConverter.class)
    protected Instant updatedAt;

    protected JpaEntity() {

    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj instanceof JpaEntity<?> entity) {

            return entity.getId()
                         .equals(this.getId());
        }

        return false;
    }

    public abstract ID getId();

    @Override
    public int hashCode() {

        return this.getId()
                   .hashCode();
    }

    public void setCreatedAt(Instant createdAt) {

        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {

        this.updatedAt = updatedAt;
    }

    @PrePersist
    public void prePersist() {

//        this.createdAt = Instant.now();
//        this.updatedAt = Instant.now();
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = Instant.now();
        }

    }

    @PreUpdate
    public void preUpdate() {

        this.updatedAt = Instant.now();

    }

}
