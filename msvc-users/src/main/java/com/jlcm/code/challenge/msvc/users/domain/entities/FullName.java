package com.jlcm.code.challenge.msvc.users.domain.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FullName {

    private String title;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
