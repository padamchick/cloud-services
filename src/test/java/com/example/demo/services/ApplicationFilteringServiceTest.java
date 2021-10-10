package com.example.demo.services;

import com.example.demo.dto.FilteringCriteria;
import com.example.demo.entities.Application;
import com.example.demo.repositories.ApplicationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ApplicationFilteringServiceTest {

    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        applicationRepository.saveAll(TestData.generateListOfApplication());
    }

    @AfterEach
    void tearDown() {
        applicationRepository.deleteAll();
    }

    @Test
    public void givenSavedApplication_whenSpecificFilteringCriteriaApplied_thenCorrectApplicationsReturned() {
        ApplicationFilteringService applicationFilteringService = new ApplicationFilteringService(entityManager);
        for(Map.Entry<FilteringCriteria, List<Long>> testcase: TestData.expectedApplicationIdsForFilteringCriteria.entrySet()) {
            List<Application> filtered = applicationFilteringService.getFiltered(testcase.getKey());
            List<Long> resultIds = filtered.stream().map(Application::getId).collect(Collectors.toList());

            assertEquals(testcase.getValue(), resultIds);
        }
    }
}
