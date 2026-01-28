package com.fsocial.postservice.services;

import com.fsocial.postservice.dto.termOfService.TermOfServiceDTO;
import com.fsocial.postservice.exception.AppCheckedException;

import java.util.List;

public interface TermOfServicesService {
    TermOfServiceDTO addTermOfService(TermOfServiceDTO termOfServiceDTO);
    TermOfServiceDTO updateTermOfService(TermOfServiceDTO termOfService) throws AppCheckedException;
    String deleteTermOfService(String termOfServiceId) throws AppCheckedException;
    List<TermOfServiceDTO> getTermOfServices();
}
