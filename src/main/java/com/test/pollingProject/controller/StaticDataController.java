package com.test.pollingProject.controller;

import com.test.pollingProject.model.Stop;
import com.test.pollingProject.service.DataService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StaticDataController {

    public final DataService dataService;

    public StaticDataController(DataService dataService) {
        this.dataService = dataService;
    }

    @GetMapping("/stopsData")
    public List<Stop> getData(){
        return dataService.getData();
    }
}
