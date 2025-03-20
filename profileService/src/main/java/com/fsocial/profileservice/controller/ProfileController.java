package com.fsocial.profileservice.controller;

import com.fsocial.profileservice.dto.ApiResponse;
import com.fsocial.profileservice.dto.response.FindProfileResponse;
import com.fsocial.profileservice.services.AccountProfileService;
import com.fsocial.profileservice.services.ProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/actions")
public class ProfileController {

    ProfileService profileService;
    private final AccountProfileService accountProfileService;

    @GetMapping("/find")
    public ApiResponse<List<FindProfileResponse>> findByName(@RequestParam("find_name") String findName) {

        List<FindProfileResponse> findProfileResponse = profileService.findByCombinedName(findName).stream()
                .map(profile -> {
                        String displayName = profile.getFirstName() + " " + profile.getLastName();
                    return FindProfileResponse.builder()
                            .id(profile.getId())
                            .userId(profile.getId())
                            .displayName(displayName)
                            .avatar(profile.getAvatar())
                            .build();
                }).collect(Collectors.toList());;

        return ApiResponse.<List<FindProfileResponse>>builder()
                .data(findProfileResponse)
                .message("Tìm kiếm user thành công")
                .build();
    }

}
