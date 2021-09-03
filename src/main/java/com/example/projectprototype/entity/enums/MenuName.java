package com.example.projectprototype.entity.enums;

import java.util.Arrays;

public enum MenuName {
    한식, 중식, 일식, 분식, 배달음식, 도시락, 빵, 커피, 술, 편의점;

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
}
