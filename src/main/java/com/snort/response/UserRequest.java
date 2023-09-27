package com.snort.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String designation;
}
