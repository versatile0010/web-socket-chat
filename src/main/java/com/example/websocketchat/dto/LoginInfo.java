package com.example.websocketchat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
public class LoginInfo {
    private String name;
    private String token;

    public static LoginInfo of(String name, String token) {
        return LoginInfo.builder()
                .name(name)
                .token(token)
                .build();
    }
}
