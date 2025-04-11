package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class CDeclaration {
    private Integer id;
    private Integer declarantId;
    private String declarantName;
    private String declarantPhone;
    private Integer declareQuantity;
    private Integer purchasePackages;
    private Integer perPackageQuantity;
    private Integer totalQuantity;
    private Integer shippedQuantity;
    private Integer receivedQuantity;
    private String purchaseTime;
    private String planShipTime;
    private String planReceiveTime;
    private String status;
}