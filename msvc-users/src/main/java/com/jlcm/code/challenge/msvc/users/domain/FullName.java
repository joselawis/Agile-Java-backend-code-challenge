package com.jlcm.code.challenge.msvc.users.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FullName {

    private String title;
    private String firstName;
    private String lastName;
}
