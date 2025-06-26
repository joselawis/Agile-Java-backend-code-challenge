package com.jlcm.code.challenge.msvc.users.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class User {
    @NotBlank
    private String username;
    @NotBlank
    private FullName name;
    @NotBlank
    private String email;
    @NotBlank
    private Gender gender;
    @NotBlank
    private String picture;
    @NotBlank
    private Location location;
}
