package com.interview.template.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {

    private Long id;

    private String username;

    private String email;

    private String password;

}
