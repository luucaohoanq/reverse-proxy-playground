package com.fpt.mss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN(1),
    MODERATOR(2),
    DEVELOPER(3),
    MEMBER(4);

    private final int value;
}
