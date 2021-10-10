package com.example.demo.services;

import com.example.demo.dto.ApplicationState;
import com.example.demo.dto.FilteringCriteria;
import com.example.demo.entities.Application;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class TestData {

    static Map<FilteringCriteria, List<Long>> expectedApplicationIdsForFilteringCriteria = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(new FilteringCriteria(null, null), List.of(1L, 2L, 3L, 4L, 5L, 6L)),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria("Wniosek", null), List.of(1L, 2L, 3L, 4L, 5L, 6L)),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria("Wniosek 1", null), List.of(1L)),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria("Wniosek 5", null), List.of(5L)),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria("Wniosek", ApplicationState.CREATED), List.of(1L)),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria("Wniosek", ApplicationState.VERIFIED), List.of(2L)),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria("Wniosek", ApplicationState.ACCEPTED), List.of(3L)),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria("Wniosek", ApplicationState.DELETED), List.of(4L)),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria("Wniosek", ApplicationState.REJECTED), List.of(5L)),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria("Wniosek", ApplicationState.PUBLISHED), List.of(6L)),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria(null, null, 1, 2), List.of(1L, 2L)),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria(null, null, 2, 2), List.of(3L, 4L)),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria(null, null, 3, 2), List.of(5L, 6L)),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria(null, null, 2, 6), List.of()),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria("Test", null), List.of()),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria("5", ApplicationState.DELETED), List.of()),
            new AbstractMap.SimpleEntry<>(new FilteringCriteria("5", ApplicationState.REJECTED), List.of(5L))
    );

    static List<Application> generateListOfApplication() {
        return List.of(
                new Application(1L, "Wniosek 1", null, ApplicationState.CREATED, null, null),
                new Application(2L, "Wniosek 2", null, ApplicationState.VERIFIED, null, null),
                new Application(3L, "Wniosek 3", null, ApplicationState.ACCEPTED, null, null),
                new Application(4L, "Wniosek 4", null, ApplicationState.DELETED, null, null),
                new Application(5L, "Wniosek 5", null, ApplicationState.REJECTED, null, null),
                new Application(6L, "Wniosek 6", null, ApplicationState.PUBLISHED, null, 1L)
        );
    }
}
