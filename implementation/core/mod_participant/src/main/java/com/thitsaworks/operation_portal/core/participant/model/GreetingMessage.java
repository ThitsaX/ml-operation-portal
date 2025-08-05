package com.thitsaworks.operation_portal.core.participant.model;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaInstantConverter;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "tbl_greeting")
@Getter
@NoArgsConstructor
public class GreetingMessage extends JpaEntity<GreetingId> {

    @EmbeddedId
    protected GreetingId greetingId;

    @Column(name = "greeting_title")
    protected String greetingTitle;

    @Column(name = "greeting_detail")
    protected String greetingDetail;

    @Column(name = "is_deleted")
    protected boolean isDeleted;

    @Column(name = "greeting_date")
    @Convert(converter = JpaInstantConverter.class)
    protected Instant greetingDate;

    public GreetingMessage(String greetingTitle,
                           String greetingDetail,
                           Instant greetingDate) {

        this.greetingId =new GreetingId(Snowflake.get().nextId());
        this.greetingTitle(greetingTitle);
        this.greetingDetail(greetingDetail);
        this.isDeleted(isDeleted);
        this.greetingDate(greetingDate);
    }

    @Override
    public GreetingId getId() {

        return greetingId;
    }


    public void greetingTitle(String greetingTitle) {

        this.greetingTitle = greetingTitle;
    }

    public void greetingDetail(String greetingDetail) {

        this.greetingDetail = greetingDetail;
    }

    public GreetingMessage isDeleted(boolean isDeleted){
        this.isDeleted = isDeleted;
        return  this;
    }

    public  void greetingDate(Instant greetingDate){
        this.greetingDate =greetingDate;
    }
}
