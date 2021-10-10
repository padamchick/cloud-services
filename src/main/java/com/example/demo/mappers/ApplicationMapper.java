package com.example.demo.mappers;

import com.example.demo.dto.ApplicationDto;
import com.example.demo.entities.Application;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    ApplicationDto toApplicationDto(Application application);
    List<ApplicationDto> toApplicationDtos(List<Application> applicationList);

}
