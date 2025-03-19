package com.liwei.ruiyi.bo;

import lombok.*;
import java.math.BigDecimal;
import java.util.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class TDeepseekLog {
    private Integer id;
    private String question;
    private String qt;
    private String answer;
    private String at;
}