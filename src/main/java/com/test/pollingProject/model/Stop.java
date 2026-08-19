package com.test.pollingProject.model;

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
    private String stop_id;

    private String stop_name;

    private double stop_lat;
    private double stop_lon;

    private String parent_station;




}
