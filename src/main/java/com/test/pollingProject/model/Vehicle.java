package com.test.pollingProject.model;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.pollingProject.model.enums.vehicles.OccupancyStatus;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    // Link the route table entry that corresponds to this vehicles route_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Route route;

    @JsonProperty("occupancy_status")
    private OccupancyStatus occupancyStatus;
}
