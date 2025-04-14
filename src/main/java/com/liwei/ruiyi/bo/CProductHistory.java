package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class CProductHistory {
    private Integer id;
    private Integer proId;
    private String name;
    private String link;
    private String imageUrl;
    private Integer supplierId;
    private String supplierName;
    private BigDecimal costPrice;
    private Boolean isActive;
    private String createTime;
    private String updateTime;
    private String disableTime;
    private Integer unshipQuantity;
    private BigDecimal unshipPrice;
    private String other;
    private String history;
}