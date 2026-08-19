package com.test.pollingProject.model;

import com.test.pollingProject.model.enums.RouteType;

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
@Table(name = "routes")
public class Route {

    @Id
    private String route_id;
    private String agency_id;
    private String short_name;
    private String long_name;
    private String route_color;
    private RouteType route_type; // Runs RouteTypeConverter automatically for this field.

}
