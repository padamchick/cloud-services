package com.example.demo.controllers;

import com.example.demo.dto.ApplicationCreateRequest;
import com.example.demo.dto.ApplicationUpdateRequest;
import com.example.demo.dto.ApplicationUpgradeRequest;
import com.example.demo.dto.FilteringCriteria;
import com.example.demo.entities.Application;
import com.example.demo.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<List<Application>> getApplications(FilteringCriteria criteria) {
        return ok(applicationService.getFiltered(criteria));
    }

    @PostMapping
    public ResponseEntity<Void> createApplication(@RequestBody @Valid ApplicationCreateRequest request) {
        applicationService.createApplication(request.getName(), request.getContent());
        return created(URI.create("/applications")).build();
    }

    @PutMapping("/content")
    public ResponseEntity<Void> updateApplicationContent(@RequestBody @Valid ApplicationUpdateRequest request) {
        applicationService.updateApplicationContent(request.getId(), request.getContent());
        return ok().build();
    }

    @PutMapping("/status")
    public ResponseEntity<?> upgradeApplicationStatus(@RequestBody @Valid ApplicationUpgradeRequest request) {
        applicationService.upgradeApplicationStatus(request.getId(), request.getState(), request.getComment());
        return ok().build();
    }


}
