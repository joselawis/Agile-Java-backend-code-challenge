package com.jlcm.code.challenge.msvc.users.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Country {

    private String name;
    private List<State> states;
}
