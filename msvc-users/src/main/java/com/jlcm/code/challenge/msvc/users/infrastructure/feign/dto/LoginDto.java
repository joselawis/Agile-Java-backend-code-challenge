package com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LoginDto(String username) {
}