package com.jlu.preparedproject.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserT {

    private Integer uid;
    private String uname;
    private String pwd;
    private String perm;
}
