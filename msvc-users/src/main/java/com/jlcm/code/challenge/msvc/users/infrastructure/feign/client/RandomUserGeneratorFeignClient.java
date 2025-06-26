package com.jlcm.code.challenge.msvc.users.infrastructure.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jlcm.code.challenge.msvc.users.infrastructure.feign.dto.RandomUserResponseDto;

@FeignClient(name = "RandomUserGenerator", url = "https://randomuser.me")
public interface RandomUserGeneratorFeignClient {

    @GetMapping("api/")
    public RandomUserResponseDto getRandomUsers(@RequestParam("results") int count, @RequestParam("inc") String fields);
}
