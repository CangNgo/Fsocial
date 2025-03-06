package com.fsocial.timelineservice.mapper;

import com.fsocial.timelineservice.dto.termOfService.TermOfServiceDTO;
import com.fsocial.timelineservice.entity.TermOfServices;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TermOfSerivceMapper {
    TermOfServices toEntity(TermOfServiceDTO termOfServices);

    TermOfServiceDTO toDto(TermOfServices termOfServices);

    List<TermOfServiceDTO> toListDTO(List<TermOfServices> termOfServices);
}
