package com.test.pollingProject.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.pollingProject.model.Stop;

public interface StopRepository extends JpaRepository<Stop, String> {
    
}
