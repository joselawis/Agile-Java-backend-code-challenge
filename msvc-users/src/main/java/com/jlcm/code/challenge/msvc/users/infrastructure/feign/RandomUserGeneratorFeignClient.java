package com.jlcm.code.challenge.msvc.users.infrastructure.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.RandomUserResponseDto;

@FeignClient(name = "RandomUserGenerator", url = "https://randomuser.me")
public interface RandomUserGeneratorFeignClient {

    @GetMapping("api/?results=5&inc=gender, name, location, email, login, picture")
    public RandomUserResponseDto getRandomUsers(int count);
}
