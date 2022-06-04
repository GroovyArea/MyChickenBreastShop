package me.daniel.domain.DTO.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PayReadyDTO {

    private String partnerOrderId, partnerUserId, itemName;
    private Integer quantity, totalAmount, taxFreeAmount;
    private String tid, nextRedirectPcUrl;
    private Date createdAt;

    public PayReadyDTO(String partnerOrderId, String partnerUserId, String itemName, Integer quantity, Integer totalAmount, Integer taxFreeAmount, String tid, String nextRedirectPcUrl, Date createdAt) {
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.taxFreeAmount = taxFreeAmount;
        this.tid = tid;
        this.nextRedirectPcUrl = nextRedirectPcUrl;
        this.createdAt = createdAt;
    }

    public PayReadyDTO() {
    }

    public String getPartnerOrderId() {
        return partnerOrderId;
    }

    public String getPartnerUserId() {
        return partnerUserId;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public Integer getTaxFreeAmount() {
        return taxFreeAmount;
    }

    public String getTid() {
        return tid;
    }

    public String getNextRedirectPcUrl() {
        return nextRedirectPcUrl;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
