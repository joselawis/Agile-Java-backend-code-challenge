package com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResultDto {
    private String gender;
    private NameDto name;
    private LocationDto location;
    private String email;
    private LoginDto login;
    private PictureDto picture;
}