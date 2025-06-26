package com.jlcm.code.challenge.msvc.users.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class User {
    private String username;
    private String name;
    private String email;
    private Gender gender;
    private String picture;
}
