package me.daniel.domain.DTO.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CancelAvailableAmountDTO {

    Integer total, taxFree, vat, point, discount;

    public CancelAvailableAmountDTO(Integer total, Integer taxFree, Integer vat, Integer point, Integer discount) {
        this.total = total;
        this.taxFree = taxFree;
        this.vat = vat;
        this.point = point;
        this.discount = discount;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getTaxFree() {
        return taxFree;
    }

    public Integer getVat() {
        return vat;
    }

    public Integer getPoint() {
        return point;
    }

    public Integer getDiscount() {
        return discount;
    }
}
