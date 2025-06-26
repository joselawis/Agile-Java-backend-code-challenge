package com.jlcm.code.challenge.msvc.users.domain.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Location {

    @NotBlank
    private String country;

    @NotBlank
    private String state;

    @NotBlank
    private String city;
}
