package com.bh.intra.plp.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomUser extends User {

    int id;
    String name;
    String orgCode;
    int pickerId;
    int queueId;

    public CustomUser(int id, int pickerId, String orgCode, int queueId, String username, String pwd) {
        super(username, pwd, true,
                true, true,
                true, new ArrayList<>());
        this.id = id;
        this.name = username;
        this.pickerId = pickerId;
        this.queueId = queueId;
        this.orgCode = orgCode;
    }
}
