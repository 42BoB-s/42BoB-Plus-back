package com.example.projectprototype.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SessionDto {
    String userId;
    String email;
    String profile;
}
