
package com.daniel.domain.VO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AmountVO {

    private String tid;
    private Integer total, taxFree, vat, point, discount;
}
