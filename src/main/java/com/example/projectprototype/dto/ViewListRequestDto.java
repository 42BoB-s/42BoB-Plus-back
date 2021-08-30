package com.example.projectprototype.dto;

import com.example.projectprototype.entity.enums.Location;
import com.example.projectprototype.entity.enums.MenuName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ViewListRequestDto {
	private Location location;
	private List<String> menu;
	private String strStartTime; // 파싱 전
	private String strEndTime; // 파싱 전
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String keyword;
	private String orderBy; // 정렬 조건 : 시간순, 모집인원 순 (현재 미구현)
}
