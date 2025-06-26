package com.jlcm.code.challenge.msvc.users.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Location {

    private String city;
    private String state;
    private String country;
}
