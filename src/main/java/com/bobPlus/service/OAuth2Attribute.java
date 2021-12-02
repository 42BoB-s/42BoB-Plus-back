package com.bobPlus.service;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
class OAuth2Attribute {
    //private Map<String, Object> attributes;
    private String attributeKey;
    private String login;
    private String email;
    private String image_url;

    static OAuth2Attribute of(String provider, String attributeKey,
                              Map<String, Object> attributes) {
        switch (provider) {
            case "42seoul":
                return of42seoul(attributeKey, attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuth2Attribute of42seoul(String attributeKey,
                                            Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                //.attributes(attributes)
                .login((String)attributes.get("login"))
                .email((String)attributes.get("email"))
                .image_url((String)attributes.get("image_url"))
                .attributeKey(attributeKey)
                .build();
    }

    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("attributeKey", attributeKey);
        map.put("login", login);
        map.put("email", email);
        map.put("image_url", image_url);

        return map;
    }
}