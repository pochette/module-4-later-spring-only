package ru.practicum.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserShortWithIp implements UserShort {
    private String firstName;
    private String email;
    private String ip;

    public UserShortWithIp(UserShort userShort, String ip) {
        this.firstName = userShort.getFirstName();
        this.email = userShort.getEmail();
        this.ip = ip;
    }
}
