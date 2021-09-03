package com.example.projectprototype.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleDto<T>{

    private int code;
    private int interCode;
    private T component;
}
