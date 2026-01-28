package com.fsocial.postservice.mapper;

import com.fsocial.postservice.dto.termOfService.TermOfServiceDTO;
import com.fsocial.postservice.entity.TermOfServices;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TermOfServiceMapper {
    TermOfServiceDTO toDTO(TermOfServices termOfServices);
    TermOfServices toEntity(TermOfServiceDTO termOfServiceDTO);
    List<TermOfServiceDTO> toListDTO(List<TermOfServices> termOfServices);
}
