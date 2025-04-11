package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class CWaybill {
    private Integer id;
    private String waybillNumber;
    private Integer declarationId;
    private String waybillImage;
    private Integer packageCount;
    private Integer perPackageQuantity;
    private Integer totalQuantity;
    private BigDecimal totalWeight;
    private BigDecimal freight;
    private String status;
    private String shipTime;
    private String receiveTime;
    private String planReceiveTime;
}