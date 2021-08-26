package com.example.projectprototype.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class SessionDTO {
    String userId;
    String email;
    String profile;
}
