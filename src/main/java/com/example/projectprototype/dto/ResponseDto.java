package com.example.projectprototype.dto;

import lombok.Builder;

import java.util.List;

@Builder
public class ResponseDto<T> {
	//int code;
	int interCode;
	List<T> list;
}
