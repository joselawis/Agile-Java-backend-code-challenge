package com.jlcm.code.challenge.msvc.users.domain.dto;

import java.util.List;

import com.jlcm.code.challenge.msvc.users.domain.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class City {

    private String name;
    private List<User> users;
}
