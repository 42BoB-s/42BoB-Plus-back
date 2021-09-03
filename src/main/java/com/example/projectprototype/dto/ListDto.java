package com.example.projectprototype.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ListDto<T>{
    private int code;
    private int interCode;
    private List<T> component;
}
