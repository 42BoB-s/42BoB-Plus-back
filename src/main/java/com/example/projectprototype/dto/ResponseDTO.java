package com.example.projectprototype.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseDTO {

    private int code;
    private int interCode;
}
