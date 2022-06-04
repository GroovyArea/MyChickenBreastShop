package me.daniel.domain.DTO.order;

import java.util.Date;

public class OrderInfoDTO {

    String cid, cidSecret, tid;
    String status, partnerOrderId, partnerUserId, paymentMethodType;
    AmountDTO amountDTO;
    CanceledAmountDTO canceledAmountDTO;
    CancelAvailableAmountDTO cancelAvailableAmountDTO;
    String itemName, itemCode;
    Integer quantity;
    Date createdAt, approvedAt, canceledAt;
    SelectedCardInfoDTO selectedCardInfoDTO;
    PaymentActionDetailsDTO[] paymentActionDetails;

    public OrderInfoDTO(String cid, String cidSecret, String tid, String status, String partnerOrderId, String partnerUserId, String paymentMethodType, AmountDTO amountDTO, CanceledAmountDTO canceledAmountDTO, CancelAvailableAmountDTO cancelAvailableAmountDTO, String itemName, String itemCode, Integer quantity, Date createdAt, Date approvedAt, Date canceledAt, SelectedCardInfoDTO selectedCardInfoDTO, PaymentActionDetailsDTO[] paymentActionDetails) {
        this.cid = cid;
        this.cidSecret = cidSecret;
        this.tid = tid;
        this.status = status;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.paymentMethodType = paymentMethodType;
        this.amountDTO = amountDTO;
        this.canceledAmountDTO = canceledAmountDTO;
        this.cancelAvailableAmountDTO = cancelAvailableAmountDTO;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
        this.canceledAt = canceledAt;
        this.selectedCardInfoDTO = selectedCardInfoDTO;
        this.paymentActionDetails = paymentActionDetails;
    }

    public OrderInfoDTO() {
    }


    public String getCid() {
        return cid;
    }

    public String getCidSecret() {
        return cidSecret;
    }

    public String getTid() {
        return tid;
    }

    public String getStatus() {
        return status;
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

    public AmountDTO getAmountDTO() {
        return amountDTO;
    }

    public CanceledAmountDTO getCanceledAmountDTO() {
        return canceledAmountDTO;
    }

    public CancelAvailableAmountDTO getCancelAvailableAmountDTO() {
        return cancelAvailableAmountDTO;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getApprovedAt() {
        return approvedAt;
    }

    public Date getCanceledAt() {
        return canceledAt;
    }

    public SelectedCardInfoDTO getSelectedCardInfoDTO() {
        return selectedCardInfoDTO;
    }

    public PaymentActionDetailsDTO[] getPaymentActionDetails() {
        return paymentActionDetails;
    }
}
