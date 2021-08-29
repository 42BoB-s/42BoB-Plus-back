package com.example.projectprototype.dto;

import com.example.projectprototype.entity.Room;
import com.example.projectprototype.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
public class ParticipantRequestDto {
	private Room room;
	private User user;

	@Builder
	public ParticipantRequestDto(Room room, User user)
	{
		this.room = room;
		this.user = user;
	}
}