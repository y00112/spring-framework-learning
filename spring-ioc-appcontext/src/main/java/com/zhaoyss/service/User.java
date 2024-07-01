package com.zhaoyss.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long id;

    private String email;

    private String password;

    private String name;

}
