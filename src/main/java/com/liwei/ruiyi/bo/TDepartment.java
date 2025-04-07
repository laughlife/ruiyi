package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class TDepartment {
    private Integer id;
    private String name;
    private String code;
    private Integer parentId;
    private Integer level;
    private String path;
}