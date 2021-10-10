package com.example.demo.services;

import com.example.demo.dto.ApplicationDto;
import com.example.demo.dto.ApplicationState;
import com.example.demo.dto.FilteringCriteria;
import com.example.demo.entities.Application;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.UpdateException;
import com.example.demo.mappers.ApplicationMapper;
import com.example.demo.repositories.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationFilteringService applicationFilteringService;
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    public List<ApplicationDto> getFiltered(FilteringCriteria criteria) {
        return applicationMapper.toApplicationDtos(applicationFilteringService.getFiltered(criteria));
    }

    public ApplicationDto createApplication(String name, String content) {
        Application application = new Application(name, content);
        application.setState(ApplicationState.CREATED);
        return applicationMapper.toApplicationDto(applicationRepository.save(application));
    }

    @Transactional
    public ApplicationDto updateApplicationContent(Long id, String content) {
        Application application = applicationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("application with id " + id + " not found"));
        if (application.getState() != ApplicationState.CREATED && application.getState() != ApplicationState.VERIFIED) {
            throw new UpdateException("you cannot change the content in "+ application.getState().name().toLowerCase() + " state");
        }

        application.setContent(content);

        return applicationMapper.toApplicationDto(application);
    }

    @Transactional
    public ApplicationDto updateApplicationStatus(Long id, ApplicationState state, String comment) {
        Application application = applicationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("application with id " + id + " not found"));
        switch (state) {
            case DELETED:
                deleteApplication(application, comment);
                break;
            case VERIFIED:
                verifyApplication(application);
                break;
            case REJECTED:
                rejectApplication(application, comment);
                break;
            case ACCEPTED:
                acceptApplication(application);
                break;
            case PUBLISHED:
                publishApplication(application);
                break;
            case CREATED:
                throw new UpdateException("you cannot create the application in "+ application.getState().name().toLowerCase() + " state");
        }
        return applicationMapper.toApplicationDto(application);
    }

    private void deleteApplication(Application application, String comment) {
        if(application.getState() != ApplicationState.CREATED) {
            throw new UpdateException("you cannot delete the application in "+ application.getState().name().toLowerCase() + " state");
        }
        if(comment == null) {
            throw new UpdateException("you cannot delete the application without reason");
        }
        application.setState(ApplicationState.DELETED);
        application.setComment(comment);
    }

    private void verifyApplication(Application application) {
        if(application.getState() != ApplicationState.CREATED) {
            throw new UpdateException("you cannot verify the application in "+ application.getState().name().toLowerCase() + " state");
        }
        application.setState(ApplicationState.VERIFIED);
    }

    private void acceptApplication(Application application) {
        if(application.getState() != ApplicationState.VERIFIED) {
            throw new UpdateException("you cannot accept the application in "+ application.getState().name().toLowerCase() + " state");
        }
        application.setState(ApplicationState.ACCEPTED);
    }

    private void rejectApplication(Application application, String comment) {
        if(application.getState() != ApplicationState.VERIFIED && application.getState() != ApplicationState.ACCEPTED) {
            throw new UpdateException("you cannot reject the application in "+ application.getState().name().toLowerCase() + " state");
        }
        if(comment == null) {
            throw new UpdateException("you cannot reject the application without reason");
        }
        application.setState(ApplicationState.REJECTED);
        application.setComment(comment);
    }

    private void publishApplication(Application application) {
        if(application.getState() != ApplicationState.ACCEPTED) {
            throw new UpdateException("you cannot publish the application in "+ application.getState().name().toLowerCase() + " state");
        }
        application.setState(ApplicationState.PUBLISHED);
        application.setBid(generateNewBid());
    }

    private Long generateNewBid() {
        Long maxBid = applicationRepository.findMaxBid().orElse(0L);
        return maxBid + 1;
    }
}
