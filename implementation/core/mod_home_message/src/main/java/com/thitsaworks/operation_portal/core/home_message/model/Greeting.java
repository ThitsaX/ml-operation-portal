package com.thitsaworks.operation_portal.core.home_message.model;

import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.component.misc.persistence.jpa.JpaEntity;
import com.thitsaworks.operation_portal.component.misc.util.Snowflake;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tbl_greeting")
@Getter
@NoArgsConstructor
public class Greeting extends JpaEntity<GreetingId> {

    @EmbeddedId
    protected GreetingId greetingId;

    @Column(name = "greeting_title")
    protected String greetingTitle;

    @Column(name = "greeting_detail")
    protected String greetingDetail;

    public Greeting(String greetingTitle,
                    String greetingDetail) {

        this.greetingId =new GreetingId(Snowflake.get().nextId());
        this.greetingTitle(greetingTitle);
        this.greetingDetail(greetingDetail);
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

}
