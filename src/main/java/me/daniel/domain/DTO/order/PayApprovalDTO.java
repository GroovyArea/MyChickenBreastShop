package me.daniel.domain.DTO.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PayApprovalDTO {

    private String aid, tid, cid, sid;
    private String partnerOrderId, partnerUserId, paymentMethodType;
    private AmountDTO amount;
    private CardDTO cardInfo;
    private String itemName, itemCode, payload;
    private Integer quantity, taxFreeAmount, vatAmount;
    private Date createdAt, approvedAt;
    private String orderStatus;

    public PayApprovalDTO(String aid, String tid, String cid, String sid, String partnerOrderId, String partnerUserId, String paymentMethodType, AmountDTO amount, CardDTO cardInfo, String itemName, String itemCode, String payload, Integer quantity, Integer taxFreeAmount, Integer vatAmount, Date createdAt, Date approvedAt, String orderStatus) {
        this.aid = aid;
        this.tid = tid;
        this.cid = cid;
        this.sid = sid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.paymentMethodType = paymentMethodType;
        this.amount = amount;
        this.cardInfo = cardInfo;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.payload = payload;
        this.quantity = quantity;
        this.taxFreeAmount = taxFreeAmount;
        this.vatAmount = vatAmount;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
        this.orderStatus = orderStatus;
    }

    public PayApprovalDTO() {
    }

    public String getAid() {
        return aid;
    }

    public String getTid() {
        return tid;
    }

    public String getCid() {
        return cid;
    }

    public String getSid() {
        return sid;
    }

    public String getPartnerOrderId() {
        return partnerOrderId;
    }

    public String getPartnerUserId() {
        return partnerUserId;
    }

    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public AmountDTO getAmount() {
        return amount;
    }

    public CardDTO getCardInfo() {
        return cardInfo;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getPayload() {
        return payload;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getTaxFreeAmount() {
        return taxFreeAmount;
    }

    public Integer getVatAmount() {
        return vatAmount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getApprovedAt() {
        return approvedAt;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
