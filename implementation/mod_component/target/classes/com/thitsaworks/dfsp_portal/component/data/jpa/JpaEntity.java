package com.thitsaworks.dfsp_portal.component.data.jpa;

import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.Instant;

@Setter
@MappedSuperclass
public abstract class JpaEntity<ID extends JpaId> implements Serializable {

    private static final long serialVersionUID = -5526155220442471285L;

    @Column(name = "created_date", updatable = false)
    @Convert(converter = JpaInstantConverter.class)
    protected Instant createdDate;

    @Column(name = "updated_date")
    @Convert(converter = JpaInstantConverter.class)
    protected Instant updatedDate;

//    @Column(name = "version")
//    @Version
//    protected Integer version;

    @PrePersist
    public void prePersist() {

        this.createdDate = Instant.now();
        this.updatedDate = Instant.now();

    }

    @PreUpdate
    public void preUpdate() {

        this.updatedDate = Instant.now();

    }

    @Override
    public int hashCode() {

        return this.getPrimaryId().getEntityId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {return true;}

        if (obj instanceof JpaEntity) {

            JpaEntity<?> entity = (JpaEntity<?>) obj;

            return entity.getPrimaryId().getEntityId().equals(this.getPrimaryId().getEntityId());
        }

        return false;
    }

    protected abstract ID getPrimaryId();
}