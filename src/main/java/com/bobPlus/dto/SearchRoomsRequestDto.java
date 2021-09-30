package com.bobPlus.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchRoomsRequestDto {

    private String location;
    private String menu;
    private String startTime;
    private String endTime;
    private String keyword;
}
