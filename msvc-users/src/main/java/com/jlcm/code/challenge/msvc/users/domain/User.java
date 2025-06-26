package com.jlcm.code.challenge.msvc.users.domain;

import jakarta.validation.constraints.Email;
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
    @Email
    private String email;

    @NotBlank
    private Gender gender;

    private String pictureUrl;

    @NotBlank
    private Location location;
}
