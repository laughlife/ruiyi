package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data
public class TOrder {
    private String amazonOrderId;
    private Integer sid;
    private String sellerName;
    private String orderStatus;
    private BigDecimal orderTotalAmount;
    private String fulfillmentChannel;
    private String postalCode;
    private Integer isReturn;
    private Integer isMcfOrder;
    private Integer isAssessed;
    private Integer isReplacedOrder;
    private Integer isReplacementOrder;
    private Integer isReturnOrder;
    private String orderTotalCurrencyCode;
    private String salesChannel;
    private String trackingNumber;
    private BigDecimal refundAmount;
    private String purchaseDateLocal;
    private String purchaseDateLocalUtc;
    private String shipmentDate;
    private String shipmentDateUtc;
    private String shipmentDateLocal;
    private String lastUpdateDate;
    private String lastUpdateDateUtc;
    private String postedDate;
    private String postedDateUtc;
    private String purchaseDate;
    private String purchaseDateUtc;
    private String earliestShipDate;
    private String earliestShipDateUtc;
    private String gmtModified;
    private String gmtModifiedUtc;
}