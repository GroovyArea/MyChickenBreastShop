
package com.daniel.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AmountVO {

    private String tid;
    private Integer total;
    private Integer taxFree;
    private Integer vat;
    private Integer point;
    private Integer discount;
}
