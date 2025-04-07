package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class TPermission {
    private Integer id;
    private String name;
    private String dataScope;
    private String path;
    private String description;
}