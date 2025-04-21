package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class CDeclaration {
    private Integer id;
    private Integer userId;
    private String userName;
    private String userPhone;
    private Integer sellerId;
    private String sellerName;
    private Integer proId;
    private String proName;
    private String tips;
    private String fapiao;
    private String shc;
    private String link;
    private String asin;
    private String imagePath;
    private Integer purchasePackages;
    private Integer perPackageQuantity;
    private Integer totalQuantity;
    private Integer kcsl;
    private Integer kcyl;
    private BigDecimal kcdj;
    private BigDecimal ksjz;
    private BigDecimal costPrice;
    private Integer buyQuantity;
    private BigDecimal costAllPrice;
    private BigDecimal totalPrice;
    private Integer planTotalQuantity;
    private String other;
    private Integer shippedQuantity;
    private Integer receivedQuantity;
    private String declareTime;
    private String confirmTime;
    private String purchaseTime;
    private String planShipTime;
    private String shipTime;
    private String planReceiveTime;
    private String status;
}