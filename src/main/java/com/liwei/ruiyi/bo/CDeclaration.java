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
    private String proName;
    private String link;
    private String asin;
    private String imagePath;
    private Integer purchasePackages;
    private Integer perPackageQuantity;
    private Integer totalQuantity;
    private String other;
    private Integer shippedQuantity;
    private Integer receivedQuantity;
    private String declareTime;
    private String confirmTime;
    private String purchaseTime;
    private String planShipTime;
    private String planReceiveTime;
    private String status;
}