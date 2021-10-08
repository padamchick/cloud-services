package com.example.demo.services;

import com.example.demo.dto.ApplicationState;
import com.example.demo.dto.FilteringCriteria;
import com.example.demo.entities.Application;
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

    public List<Application> getFiltered(FilteringCriteria criteria) {
        return applicationFilteringService.getFiltered(criteria);
    }

    public void createApplication(String name, String content) {
        Application application = new Application(name, content);
        application.setState(ApplicationState.CREATED);
        applicationRepository.save(application);
    }

    @Transactional
    public void updateApplicationContent(Long id, String content) {
        Application application = applicationRepository.findById(id).orElseThrow(() -> new RuntimeException("application not found"));
        if(application.getState() == ApplicationState.CREATED || application.getState() == ApplicationState.VERIFIED) {
            application.setContent(content);
        } else {
            throw new RuntimeException("you cannot change the content in "+ application.getState().name().toLowerCase() + " state");
        }
    }

    @Transactional
    public void upgradeApplicationStatus(Long id, ApplicationState state, String comment) {
        Application application = applicationRepository.findById(id).orElseThrow(() -> new RuntimeException("application not found"));
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
            default:
                throw new RuntimeException("wrong application state");
        }
    }

    private void deleteApplication(Application application, String comment) {
        if(application.getState() != ApplicationState.CREATED) {
            throw new RuntimeException("you cannot delete the application in "+ application.getState().name().toLowerCase() + " state");
        }
        if(comment == null) {
            throw new RuntimeException("you did not provide the reason of deletion");
        }
        application.setState(ApplicationState.DELETED);
        application.setComment(comment);
    }

    private void verifyApplication(Application application) {
        if(application.getState() != ApplicationState.CREATED) {
            throw new RuntimeException("you cannot verify the application in "+ application.getState().name().toLowerCase() + " state");
        }
        application.setState(ApplicationState.VERIFIED);
    }

    private void acceptApplication(Application application) {
        if(application.getState() != ApplicationState.VERIFIED) {
            throw new RuntimeException("you cannot accept the application in "+ application.getState().name().toLowerCase() + " state");
        }
        application.setState(ApplicationState.ACCEPTED);
    }

    private void rejectApplication(Application application, String comment) {
        if(application.getState() != ApplicationState.VERIFIED && application.getState() != ApplicationState.ACCEPTED) {
            throw new RuntimeException("you cannot reject the application in "+ application.getState().name().toLowerCase() + " state");
        }
        if(comment == null) {
            throw new RuntimeException("you did not provide the reason of rejection");
        }
        application.setState(ApplicationState.REJECTED);
        application.setComment(comment);
    }

    private void publishApplication(Application application) {
        if(application.getState() != ApplicationState.ACCEPTED) {
            throw new RuntimeException("you cannot publish the application in "+ application.getState().name().toLowerCase() + " state");
        }
        application.setState(ApplicationState.PUBLISHED);
        application.setBid(generateNewBid());
    }

    private Long generateNewBid() {
        Long maxBid = applicationRepository.findMaxBid().orElse(0L);
        return maxBid + 1;
    }
}
