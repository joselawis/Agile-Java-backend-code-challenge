package com.jlcm.code.challenge.msvc.users.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class State {

    private String name;
    private int numberOfUsers;
    private List<City> cities;
}
