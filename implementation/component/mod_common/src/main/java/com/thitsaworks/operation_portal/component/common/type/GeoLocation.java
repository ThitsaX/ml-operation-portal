package com.thitsaworks.operation_portal.component.common.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GeoLocation {

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
    private BigDecimal longitude;

    public GeoLocation(BigDecimal latitude, BigDecimal longitude) {

        assert latitude != null : "Latitude must not be NULL.";
        assert longitude != null : "Longitude must not be NULL.";

        this.latitude = latitude;
        this.longitude = longitude;

    }

}
