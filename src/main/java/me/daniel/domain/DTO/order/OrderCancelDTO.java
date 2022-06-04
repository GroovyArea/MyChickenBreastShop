package me.daniel.domain.DTO.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderCancelDTO {

    private String cid, tid, aid, status;
    private Integer cancelAmount, cancelTaxFreeAmount;
    private String partnerOrderId, partnerUserId, paymentMethodType;
    private AmountDTO amountDTO;
    private ApprovedCancelAmountDTO approvedCancelAmountDTO;
    private CanceledAmountDTO canceledAmountDTO;
    private CancelAvailableAmountDTO cancelAvailableAmountDTO;
    private String itemName, itemCode;
    private Integer quantity;
    private Date createdAt, approvedAt, canceledAt;

    public OrderCancelDTO(String cid, String tid, String aid, String status, Integer cancelAmount, Integer cancelTaxFreeAmount, String partnerOrderId, String partnerUserId, String paymentMethodType, AmountDTO amountDTO, ApprovedCancelAmountDTO approvedCancelAmountDTO, CanceledAmountDTO canceledAmountDTO, CancelAvailableAmountDTO cancelAvailableAmountDTO, String itemName, String itemCode, Integer quantity, Date createdAt, Date approvedAt, Date canceledAt) {
        this.cid = cid;
        this.tid = tid;
        this.aid = aid;
        this.status = status;
        this.cancelAmount = cancelAmount;
        this.cancelTaxFreeAmount = cancelTaxFreeAmount;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.paymentMethodType = paymentMethodType;
        this.amountDTO = amountDTO;
        this.approvedCancelAmountDTO = approvedCancelAmountDTO;
        this.canceledAmountDTO = canceledAmountDTO;
        this.cancelAvailableAmountDTO = cancelAvailableAmountDTO;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
        this.canceledAt = canceledAt;
    }

    public OrderCancelDTO() {
    }


    public String getCid() {
        return cid;
    }

    public String getTid() {
        return tid;
    }

    public String getAid() {
        return aid;
    }

    public String getStatus() {
        return status;
    }

    public Integer getCancelAmount() {
        return cancelAmount;
    }

    public Integer getCancelTaxFreeAmount() {
        return cancelTaxFreeAmount;
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

    public ApprovedCancelAmountDTO getApprovedCancelAmountDTO() {
        return approvedCancelAmountDTO;
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


}
