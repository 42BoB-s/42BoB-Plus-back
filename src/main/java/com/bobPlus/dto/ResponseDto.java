package com.bobPlus.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ResponseDto<T> {
	//임시
	private int interCode;
	private T component;
}
