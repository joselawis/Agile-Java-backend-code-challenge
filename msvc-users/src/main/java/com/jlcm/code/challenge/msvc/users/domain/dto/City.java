package com.jlcm.code.challenge.msvc.users.domain.dto;

import java.util.List;

import com.jlcm.code.challenge.msvc.users.domain.entities.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class City {

    private String name;
    private int numberOfUsers;
    private List<User> users;
}
