package com.jlcm.code.challenge.msvc.users.domain.entities;

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

    private FullName name;

    @NotBlank
    @Email
    private String email;

    private Gender gender;

    private String pictureUrl;

    private Location location;
}
