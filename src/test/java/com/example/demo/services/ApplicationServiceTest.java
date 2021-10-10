package com.example.demo.services;

import com.example.demo.dto.ApplicationDto;
import com.example.demo.dto.ApplicationState;
import com.example.demo.dto.FilteringCriteria;
import com.example.demo.entities.Application;
import com.example.demo.exceptions.UpdateException;
import com.example.demo.mappers.ApplicationMapper;
import com.example.demo.repositories.ApplicationRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


class ApplicationServiceTest {
    private static final ApplicationFilteringService applicationFilteringService = Mockito.mock(ApplicationFilteringService.class);
    private static final ApplicationRepository applicationRepository = Mockito.mock(ApplicationRepository.class);
    private static final ApplicationMapper applicationMapper = Mockito.mock(ApplicationMapper.class);

//    createApplication

    @Test
    void givenNewApplication_whenSavingItToDB_thenProperDtoReturned() {
        ApplicationService applicationService = new ApplicationService(null, applicationRepository, applicationMapper);
        String name = "Wniosek1";
        String content = "test";
        Application applicationToSave = new Application(null, name, content, ApplicationState.CREATED, null, null);
        Application createdApplication = new Application(1L, name, content, ApplicationState.CREATED, null, null);
        ApplicationDto expectedApplicationDto = new ApplicationDto(1L, name, content, ApplicationState.CREATED, null, null);
        Mockito.when(applicationRepository.save(applicationToSave)).thenReturn(createdApplication);
        Mockito.when(applicationMapper.toApplicationDto(createdApplication)).thenReturn(expectedApplicationDto);

        ApplicationDto result = applicationService.createApplication(name, content);
        assertEquals(expectedApplicationDto, result);
    }

//    updateApplicationContent

    @Test
    void givenIdAndContent_whenUpdatedApplicationWithCreatedOrVerifiedStatus_thenProperDtoReturned() {
        ApplicationService applicationService = new ApplicationService(null, applicationRepository, applicationMapper);
        Long id = 1L;
        String content = "test";
        String name = "Wniosek1";
        List<ApplicationState> allowedStates = new ArrayList<>(List.of(ApplicationState.CREATED, ApplicationState.VERIFIED));
        for (ApplicationState state : allowedStates) {
            Application application = new Application(id, name, null, state, null, null);
            Application updatedApplication = new Application(id, name, content, state, null, null);
            ApplicationDto expectedApplicationDto = new ApplicationDto(id, name, content, state, null, null);
            Mockito.when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
            Mockito.when(applicationMapper.toApplicationDto(updatedApplication)).thenReturn(expectedApplicationDto);

            ApplicationDto result = applicationService.updateApplicationContent(id, content);
            assertEquals(expectedApplicationDto, result);
        }
    }

    @Test
    void givenIdAndContent_whenUpdatedApplicationWithForbiddenStatus_thenThrowUpdateError() {
        Long id = 1L;
        String content = "test";
        ApplicationService applicationService = new ApplicationService(null, applicationRepository, applicationMapper);
        List<ApplicationState> forbiddenStates = new ArrayList<>(List.of(ApplicationState.ACCEPTED, ApplicationState.DELETED, ApplicationState.PUBLISHED, ApplicationState.REJECTED));
        for (ApplicationState state : forbiddenStates) {
            Application application = new Application(id, "Wniosek1", null, state, null, null);
            Mockito.when(applicationRepository.findById(id)).thenReturn(Optional.of(application));

            assertThatThrownBy(() -> applicationService.updateApplicationContent(id, content))
                    .isInstanceOf(UpdateException.class)
                    .hasMessage("you cannot change the content in " + state.name().toLowerCase() + " state");
        }
    }

//    updateApplicationStatus

    Map<ApplicationState, ApplicationState> possibleTransitionsWithoutComment = Map.of(
            ApplicationState.CREATED, ApplicationState.VERIFIED,
            ApplicationState.VERIFIED, ApplicationState.ACCEPTED,
            ApplicationState.ACCEPTED, ApplicationState.PUBLISHED
    );

    @Test
    public void givenCurrentApplicationStatus_whenUpdateToAllowedNextLevel_thanApplicationUpdatedCorrectly() {
        Long id = 1L;
        ApplicationService applicationService = new ApplicationService(null, applicationRepository, applicationMapper);
        for (Map.Entry<ApplicationState, ApplicationState> testcase : possibleTransitionsWithoutComment.entrySet()) {
            Long bid = null;
            if (testcase.getValue() == ApplicationState.PUBLISHED) {
                bid = 1L;
            }
            Application application = new Application(1L, null, null, testcase.getKey(), null, bid);
            Application updatedApplication = new Application(1L, null, null, testcase.getValue(), null, bid);
            ApplicationDto expectedResult = new ApplicationDto(1L, null, null, testcase.getValue(), null, bid);
            Mockito.when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
            Mockito.when(applicationMapper.toApplicationDto(updatedApplication)).thenReturn(expectedResult);

            ApplicationDto result = applicationService.updateApplicationStatus(id, testcase.getValue(), null);

            assertEquals(expectedResult, result);
        }
    }


    Map<ApplicationState, List<ApplicationState>> impossibleTransitions = Map.of(
            ApplicationState.CREATED, List.of(ApplicationState.CREATED, ApplicationState.REJECTED, ApplicationState.ACCEPTED, ApplicationState.PUBLISHED),
            ApplicationState.VERIFIED, List.of(ApplicationState.DELETED, ApplicationState.CREATED, ApplicationState.VERIFIED, ApplicationState.PUBLISHED),
            ApplicationState.ACCEPTED, List.of(ApplicationState.DELETED, ApplicationState.CREATED, ApplicationState.VERIFIED, ApplicationState.ACCEPTED),
            ApplicationState.DELETED, List.of(ApplicationState.DELETED, ApplicationState.CREATED, ApplicationState.VERIFIED, ApplicationState.REJECTED, ApplicationState.ACCEPTED, ApplicationState.PUBLISHED),
            ApplicationState.REJECTED, List.of(ApplicationState.DELETED, ApplicationState.CREATED, ApplicationState.VERIFIED, ApplicationState.REJECTED, ApplicationState.ACCEPTED, ApplicationState.PUBLISHED),
            ApplicationState.PUBLISHED, List.of(ApplicationState.DELETED, ApplicationState.CREATED, ApplicationState.VERIFIED, ApplicationState.REJECTED, ApplicationState.ACCEPTED, ApplicationState.PUBLISHED)
    );

    Map<ApplicationState, String> applicationStatesWithVerbs = Map.of(
            ApplicationState.CREATED, "create",
            ApplicationState.VERIFIED, "verify",
            ApplicationState.ACCEPTED, "accept",
            ApplicationState.DELETED, "delete",
            ApplicationState.REJECTED, "reject",
            ApplicationState.PUBLISHED, "publish"
    );

    @Test
    public void givenCurrentStatus_whenUpdateToForbiddenLevel_thanThrowUpdateException() {
        Long id = 1L;
        ApplicationService applicationService = new ApplicationService(null, applicationRepository, applicationMapper);
        for (Map.Entry<ApplicationState, List<ApplicationState>> testcase : impossibleTransitions.entrySet()) {

            Application application = new Application(1L, null, null, testcase.getKey(), null, null);
            Mockito.when(applicationRepository.findById(id)).thenReturn(Optional.of(application));

            for (ApplicationState forbiddenState : testcase.getValue()) {
                assertThatThrownBy(() -> applicationService.updateApplicationStatus(id, forbiddenState, null))
                        .isInstanceOf(UpdateException.class)
                        .hasMessage("you cannot " + applicationStatesWithVerbs.get(forbiddenState) + " the application in " + testcase.getKey().name().toLowerCase() + " state");
            }
        }
    }

    Map<ApplicationState, ApplicationState> possibleTransitionsWithComment = Map.of(
            ApplicationState.CREATED, ApplicationState.DELETED,
            ApplicationState.VERIFIED, ApplicationState.REJECTED,
            ApplicationState.ACCEPTED, ApplicationState.REJECTED
    );

    @Test
    public void givenCurrentApplicationStatus_whenRejectOrDeleteWithComment_thanApplicationUpdatedCorrectly() {
        Long id = 1L;
        String comment = "test";
        ApplicationService applicationService = new ApplicationService(null, applicationRepository, applicationMapper);
        for (Map.Entry<ApplicationState, ApplicationState> testcase : possibleTransitionsWithComment.entrySet()) {
            Application application = new Application(1L, null, null, testcase.getKey(), null, null);
            Application updatedApplication = new Application(1L, null, null, testcase.getValue(), comment, null);
            ApplicationDto expectedResult = new ApplicationDto(1L, null, null, testcase.getValue(), comment, null);
            Mockito.when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
            Mockito.when(applicationMapper.toApplicationDto(updatedApplication)).thenReturn(expectedResult);

            ApplicationDto result = applicationService.updateApplicationStatus(id, testcase.getValue(), comment);

            assertEquals(expectedResult, result);
        }
    }

    @Test
    public void givenCurrentApplicationStatus_whenRejectOrDeleteWithoutComment_thanThrowUpdateException() {
        Long id = 1L;
        ApplicationService applicationService = new ApplicationService(null, applicationRepository, applicationMapper);
        for (Map.Entry<ApplicationState, ApplicationState> testcase : possibleTransitionsWithComment.entrySet()) {
            Application application = new Application(1L, null, null, testcase.getKey(), null, null);
            Mockito.when(applicationRepository.findById(id)).thenReturn(Optional.of(application));

            assertThatThrownBy(() -> applicationService.updateApplicationStatus(id, testcase.getValue(), null))
                    .isInstanceOf(UpdateException.class)
                    .hasMessage("you cannot " + applicationStatesWithVerbs.get(testcase.getValue()) + " the application without reason");
        }
    }

    List<Application> applications = TestData.generateListOfApplication();

    @Test
    public void givenFilteringCriteriaWithExpectedResults_whenGetFilteredInvoked_correctDtoListReturned() {
        ApplicationService applicationService = new ApplicationService(applicationFilteringService, null, applicationMapper);
        for(Map.Entry<FilteringCriteria, List<Long>> testcase: TestData.expectedApplicationIdsForFilteringCriteria.entrySet()) {
            List<Application> applicationList = applications.stream().filter(app -> testcase.getValue().contains(app.getId())).collect(toList());
            Mockito.when(applicationFilteringService.getFiltered(testcase.getKey()))
                    .thenReturn(applicationList);
            Mockito.when(applicationMapper.toApplicationDtos(applicationList))
                    .thenReturn(applicationList.stream().map(this::toApplicationDto).collect(toList()));

            List<ApplicationDto> result = applicationService.getFiltered(testcase.getKey());
            assertEquals(testcase.getValue(), result.stream().map(ApplicationDto::getId).collect(toList()));
        }
    }

    private ApplicationDto toApplicationDto(Application app) {
        return new ApplicationDto(app.getId(), app.getName(), app.getContent(), app.getState(), app.getComment(), app.getBid());
    }

}
