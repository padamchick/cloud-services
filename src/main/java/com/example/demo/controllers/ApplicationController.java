package com.example.demo.controllers;

import com.example.demo.dto.FilteringCriteria;
import com.example.demo.entities.Application;
import com.example.demo.services.ApplicationFilteringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationFilteringService applicationFilteringService;

    @GetMapping("/applications")
    public ResponseEntity<List<Application>> getApplications(FilteringCriteria criteria) {
        return ok(applicationFilteringService.getFiltered(criteria));
    }
}
