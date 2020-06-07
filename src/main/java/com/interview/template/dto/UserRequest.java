package com.interview.template.dto;

import lombok.Data;

@Data
public class UserRequest {

    private Long id;

    private String username;

    private String email;

    private String password;

}
