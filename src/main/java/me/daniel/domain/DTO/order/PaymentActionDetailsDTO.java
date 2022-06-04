package me.daniel.domain.DTO.order;

public class PaymentActionDetailsDTO {

    private String aid, approvedAt, paymentActionType, payload;
    private Integer amount, pointAmount, discountAmount;

    public PaymentActionDetailsDTO(String aid, String approvedAt, String paymentActionType, String payload, Integer amount, Integer pointAmount, Integer discountAmount) {
        this.aid = aid;
        this.approvedAt = approvedAt;
        this.paymentActionType = paymentActionType;
        this.payload = payload;
        this.amount = amount;
        this.pointAmount = pointAmount;
        this.discountAmount = discountAmount;
    }

    public String getAid() {
        return aid;
    }

    public String getApprovedAt() {
        return approvedAt;
    }

    public String getPaymentActionType() {
        return paymentActionType;
    }

    public String getPayload() {
        return payload;
    }

    public Integer getAmount() {
        return amount;
    }

    public Integer getPointAmount() {
        return pointAmount;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }
}
