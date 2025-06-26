package com.jlcm.code.challenge.msvc.users.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Location {

    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotBlank
    private String country;
}
