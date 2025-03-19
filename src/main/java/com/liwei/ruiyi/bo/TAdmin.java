package com.liwei.ruiyi.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TAdmin {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
}