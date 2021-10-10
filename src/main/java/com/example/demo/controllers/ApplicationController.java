package com.example.demo.controllers;

import com.example.demo.dto.ApplicationCreateRequest;
import com.example.demo.dto.ApplicationDto;
import com.example.demo.dto.ApplicationUpgradeRequest;
import com.example.demo.dto.FilteringCriteria;
import com.example.demo.dto.ApplicationUpdateContentRequest;
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
    public ResponseEntity<List<ApplicationDto>> getApplications(FilteringCriteria criteria) {
        return ok(applicationService.getFiltered(criteria));
    }

    @PostMapping
    public ResponseEntity<ApplicationDto> createApplication(@RequestBody @Valid ApplicationCreateRequest request) {
        return created(URI.create("/applications")).body(applicationService.createApplication(request.getName(), request.getContent()));
    }

//    assumption: separated endpoint for chaning the contant for the CREATED and VERIFIED state
    @PutMapping("/content")
    public ResponseEntity<ApplicationDto> updateApplicationContent(@RequestBody @Valid ApplicationUpdateContentRequest request) {
        return ok(applicationService.updateApplicationContent(request.getId(), request.getContent()));
    }

    @PutMapping("/status")
    public ResponseEntity<ApplicationDto> upgradeApplicationStatus(@RequestBody @Valid ApplicationUpgradeRequest request) {
        return ok(applicationService.updateApplicationStatus(request.getId(), request.getState(), request.getComment()));
    }


}
