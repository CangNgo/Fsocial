package com.fsocial.timelineservice.services.impl;

import com.fsocial.timelineservice.dto.termOfService.TermOfServiceDTO;
import com.fsocial.timelineservice.entity.TermOfServices;
import com.fsocial.timelineservice.mapper.TermOfSerivceMapper;
import com.fsocial.timelineservice.repository.TermOfServicesRepository;
import com.fsocial.timelineservice.services.TermOfServiceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TermOfServiceImpl implements TermOfServiceService {

    TermOfServicesRepository termOfServicesRepository;
    TermOfSerivceMapper termOfSerivceMapper;

    @Override
    public List<TermOfServiceDTO> getTermOfServices() {

        List<TermOfServices> res = termOfServicesRepository.findAll();

        return termOfSerivceMapper.toListDTO(res);
    }
}
