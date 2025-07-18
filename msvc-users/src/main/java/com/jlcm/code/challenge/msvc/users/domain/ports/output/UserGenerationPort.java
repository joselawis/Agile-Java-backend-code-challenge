package com.jlcm.code.challenge.msvc.users.domain.ports.output;

import java.util.Collection;

import com.jlcm.code.challenge.msvc.users.domain.entities.User;

public interface UserGenerationPort {
    public Collection<User> generateUsers(int count);
}
