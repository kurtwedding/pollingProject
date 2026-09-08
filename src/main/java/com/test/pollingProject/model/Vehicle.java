package com.test.pollingProject.model;

import com.test.pollingProject.model.enums.vehicles.OccupancyStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "vehicles")
@Builder
public class Vehicle {
    @Id
    private String id;

    private double latitude;
    private double longitude;

    private double bearing;
    private String route_id;

    private OccupancyStatus occupancy_status;
}
