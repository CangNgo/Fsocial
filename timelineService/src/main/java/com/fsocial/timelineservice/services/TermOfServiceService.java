package com.fsocial.timelineservice.services;


import com.fsocial.timelineservice.dto.termOfService.TermOfServiceDTO;
import com.fsocial.timelineservice.entity.ReplyComment;

import java.util.List;

public interface TermOfServiceService {
    List<TermOfServiceDTO> getTermOfServices();
}
