package com.bh.intra.plp.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
public class LoginDetails {

    String username;
    int pickerId;
    int userId;
    int queueId;
    String orgCode;
    String token;


}