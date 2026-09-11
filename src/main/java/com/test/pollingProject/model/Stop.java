package com.test.pollingProject.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.pollingProject.model.enums.stops.LocationType;
import com.test.pollingProject.model.enums.stops.WheelchairBoarding;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stops")
public class Stop {

    // Required (+conditionally) Fields
    @Id
    @JsonProperty("stop_id")
    private String id;
    @JsonProperty("stop_name")
    private String name;
    @JsonProperty("parent_station")
    private String parentStation;

    // Mapping these under a different name to make bounds checking easier in
    // StopRepository.java
    @JsonProperty("stop_lat")
    private Double latitude;
    @JsonProperty("stop_lon")
    private Double longitude;

    @JsonProperty("wheelchair_boarding")
    private WheelchairBoarding wheelchairBoarding = WheelchairBoarding.UNKNOWN; // Set default value to unknown
    @JsonProperty("location_type")
    private LocationType locationType = LocationType.UNKNOWN;

}
