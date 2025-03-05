package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.dto.termOfService.TermOfServiceDTO;
import com.fsocial.postservice.entity.TermOfServices;
import com.fsocial.postservice.mapper.TermOfServiceMapper;
import com.fsocial.postservice.repository.TermRepository;
import com.fsocial.postservice.services.TermOfServicesService;
import org.springframework.stereotype.Service;

@Service
public class TermOfServicesServiceImpl implements TermOfServicesService {

    TermRepository termRepository;
    TermOfServiceMapper termOfServiceMapper;

    @Override
    public TermOfServiceDTO addTermOfService(TermOfServiceDTO termOfService) {
        return termOfServiceMapper.toDTO(termRepository.save(termOfServiceMapper.toEntity(termOfService)));
    }
}
