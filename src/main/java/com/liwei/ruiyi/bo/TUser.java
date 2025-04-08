package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class TUser {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private Integer departmentId;
    private String departmentCode;
    private Integer isLadder;
    private Integer isAdmin;
    private String createTime;
    private String deleteTime;
    private Integer isBan;
}