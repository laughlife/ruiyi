package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class CSupplier {
    private Integer id;
    private String name;
    private String fzr;
    private String phone;
    private String address;
    private String otherInfo;
    private String createTime;
}