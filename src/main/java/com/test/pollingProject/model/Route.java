package com.test.pollingProject.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.pollingProject.model.enums.routes.RouteType;

import jakarta.persistence.Column;
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
@Table(name = "routes")
public class Route {

    @Id
    @Column(name = "route_id")
    @JsonProperty("route_id")
    private String routeId;

    @JsonProperty("agency_id")
    private String agencyId;

    @JsonProperty("route_short_name")
    private String routeShortName;
    @JsonProperty("route_long_name")
    private String routeLongName;

    private String route_color;
    private String route_text_color;

    @JsonProperty("route_type")
    private RouteType routeType = RouteType.UNKNOWN; // Runs RouteTypeConverter automatically for this field.

}
