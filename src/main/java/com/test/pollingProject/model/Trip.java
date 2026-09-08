package com.test.pollingProject.model;

import com.test.pollingProject.model.enums.trips.BikesAllowed;
import com.test.pollingProject.model.enums.trips.Direction;
import com.test.pollingProject.model.enums.trips.WheelchairAccessible;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trips")
public class Trip {

    @Id
    private String trip_id;
    private String route_id;
    private String service_id;
    private String shape_id;
    private String trip_headsign;

    private String origin_stop_id;
    private String destination_stop_id;

    private Direction direction_id;
    private BikesAllowed bikes_allowed;
    private WheelchairAccessible wheelchair_accessible;

}
