package com.jlcm.code.challenge.msvc.users.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Country {

    private String name;
    private int numberOfUsers;
    private List<State> states;
}
