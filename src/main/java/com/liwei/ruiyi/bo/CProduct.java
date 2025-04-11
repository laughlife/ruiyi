package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class CProduct {
    private Integer id;
    private String productName;
    private String imageUrl;
    private Integer supplierId;
    private String supplierName;
    private BigDecimal costPrice;
    private Boolean isActive;
    private String createTime;
    private String updateTime;
    private String disableTime;
    private Integer unshipQuantity;
}