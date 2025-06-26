package com.jlcm.code.challenge.msvc.users.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class User {
    @NotBlank
    @NotEmpty
    private String username;
    private FullName name;
    private String email;
    private Gender gender;
    private String picture;
    private Location location;
}
