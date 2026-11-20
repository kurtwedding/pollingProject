package com.test.pollingProject.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.pollingProject.model.enums.stops.LocationType;
import com.test.pollingProject.model.enums.stops.WheelchairBoarding;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;

@Entity
@Getter
@Setter
@Table(name = "stops")
public class Stop {

    // Required (+conditionally) Fields
    @Id
    @NotNull
    @JsonProperty("stop_id")
    private String id;
    @NotNull
    @JsonProperty("stop_name")
    private String name;
    @JsonProperty("parent_station")
    private String parentStation;

    // Mapping these under a different name to make bounds checking easier in
    // StopRepository.java
    @NotNull
    @JsonProperty("stop_lat")
    private Double latitude;
    @NotNull
    @JsonProperty("stop_lon")
    private Double longitude;

    @NotNull
    @JsonProperty("wheelchair_boarding")
    private WheelchairBoarding wheelchairBoarding = WheelchairBoarding.UNKNOWN; // Set default value to unknown
    @NotNull
    @JsonProperty("location_type")
    private LocationType locationType = LocationType.UNKNOWN;

}
