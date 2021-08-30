package com.example.projectprototype.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ResponseDto<T> {
	//private int code;
	private int interCode;
	private T component;
}
