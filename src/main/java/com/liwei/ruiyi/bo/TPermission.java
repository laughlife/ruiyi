package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class TPermission {
    private Integer id;
    private String name;
    private Integer parentId;
    private String dataScope;
    private String icon;
    private String path;
    private String description;
    private Integer px;
    private Integer isLink;
}