package com.fsocial.postservice.services.impl;

import com.fsocial.postservice.dto.termOfService.TermOfServiceDTO;
import com.fsocial.postservice.entity.TermOfServices;
import com.fsocial.postservice.mapper.TermOfServiceMapper;
import com.fsocial.postservice.repository.TermRepository;
import com.fsocial.postservice.services.TermOfServicesService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor()
public class TermOfServicesServiceImpl implements TermOfServicesService {

    TermRepository termRepository;
    TermOfServiceMapper termOfServiceMapper;

    @Override
    public TermOfServiceDTO addTermOfService(TermOfServiceDTO termOfService) {
        return termOfServiceMapper.toDTO(termRepository.save(termOfServiceMapper.toEntity(termOfService)));
    }
}
