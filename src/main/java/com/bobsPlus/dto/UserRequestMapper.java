package com.bobsPlus.dto;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper {
    public UserDto toDto(OAuth2User oAuth2User) {
        var attributes = oAuth2User.getAttributes();
        return UserDto.builder()
                .id((String) attributes.get("login"))
                .email((String) attributes.get("email"))
                .profile((String) attributes.get("image_url"))
                .build();
    }
}
