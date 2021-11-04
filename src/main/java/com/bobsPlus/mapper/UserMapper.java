package com.bobsPlus.mapper;

import com.bobsPlus.dto.SessionDto;
import com.bobsPlus.dto.UserDto;
import com.bobsPlus.entity.Ban;
import com.bobsPlus.entity.Room;
import com.bobsPlus.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<UserDto, User> {

    @Override
    @Mapping(target = "banSrcList", ignore = true)
    @Mapping(target = "banDestList", ignore = true)
    @Mapping(target = "ownedRoomList", ignore = true)
    UserDto toDto(User user);

    @Override
    @Mapping(target = "banSrcList", ignore = true)
    @Mapping(target = "banDestList", ignore = true)
    @Mapping(target = "ownerList", ignore = true)
    @Mapping(target = "participantList", ignore = true)
    User toEntity(UserDto userDto);

    default UserDto convertToUserDto(User user) {
        UserDto userDto = toDto(user);

        // 내가 벤한 사람들 id
        for (Ban ban : user.getBanSrcList()) {
            userDto.getBanSrcList().add(ban.getDest().getId());
        }
        // 나를 벤한 사람들 id
        for (Ban ban : user.getBanDestList()) {
            userDto.getBanDestList().add(ban.getSrc().getId());
        }
        // 내가 방장으로 있는 방
        for (Room room : user.getOwnerList()) {
            userDto.getOwnedRoomList().add(room.getId());
        }
        return userDto;
    }

    default User convertToUser(SessionDto sessionDto) {
        User user = new User();

        user.setId(sessionDto.getUserId());
        user.setProfile(sessionDto.getProfile());
        user.setRole("1");
        return user;
    }
}
