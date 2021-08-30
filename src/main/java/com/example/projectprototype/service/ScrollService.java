package com.example.projectprototype.service;

import com.example.projectprototype.dto.ResponseDto;
import com.example.projectprototype.dto.ViewListRequestDto;
import com.example.projectprototype.dto.ViewListResponseDto;
import com.example.projectprototype.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScrollService {
	void scrollInit(ViewListRequestDto dto);
	ResponseDto<List<ViewListResponseDto>> scrollView(User user, ViewListRequestDto dto, Pageable pageable);
}
