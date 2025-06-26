package com.jlcm.code.challenge.msvc.users.infrastructure.feign;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.jlcm.code.challenge.msvc.users.application.UserGenerationPort;
import com.jlcm.code.challenge.msvc.users.domain.entities.User;
import com.jlcm.code.challenge.msvc.users.infrastructure.feign.mappers.RandomUserMapper;

@Service
public class RandomUserGeneratorFeignAdapter implements UserGenerationPort {

    private final RandomUserGeneratorFeignClient randomUserGeneratorFeignClient;
    private final RandomUserMapper randomUserMapper;

    public RandomUserGeneratorFeignAdapter(RandomUserGeneratorFeignClient randomUserGeneratorFeignClient,
            RandomUserMapper randomUserMapper) {
        this.randomUserGeneratorFeignClient = randomUserGeneratorFeignClient;
        this.randomUserMapper = randomUserMapper;
    }

    @Override
    public Collection<User> generateUsers(int count) {
        return randomUserGeneratorFeignClient.getRandomUsers(count)
                .getResults()
                .stream()
                .map(randomUserMapper::toDomain)
                .toList();
    }

}
