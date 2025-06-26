package com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto;

import java.util.List;

import lombok.Data;

@Data
public class RandomUserResponseDto {
    private List<UserResultDto> results;
}
