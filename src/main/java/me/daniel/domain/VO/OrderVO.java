package me.daniel.domain.VO;

public class OrderVO {

    private String tid, userId, aid;
    private String cid, partnerOrderId, partnerUserId;
    private String paymentMethodType, itemName, itemCode;
    private Integer quantity;
    private String createdAt, approvedAt;
    private CardVO cardVO;
    private AmountVO amountVO;
    private String orderStatus;

    public OrderVO(String tid, String userId, String aid, String cid, String partnerOrderId, String partnerUserId, String paymentMethodType, String itemName, String itemCode, Integer quantity, String createdAt, String approvedAt, CardVO cardVO, AmountVO amountVO, String orderStatus) {
        this.tid = tid;
        this.userId = userId;
        this.aid = aid;
        this.cid = cid;
        this.partnerOrderId = partnerOrderId;
        this.partnerUserId = partnerUserId;
        this.paymentMethodType = paymentMethodType;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
        this.cardVO = cardVO;
        this.amountVO = amountVO;
        this.orderStatus = orderStatus;
    }

    public OrderVO() {
    }

    public String getTid() {
        return tid;
    }

    public String getUserId() {
        return userId;
    }

    public String getAid() {
        return aid;
    }

    public String getCid() {
        return cid;
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

    public String getItemName() {
        return itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getCreateAt() {
        return createdAt;
    }

    public String getApprovedAt() {
        return approvedAt;
    }

    public CardVO getCardVO() {
        return cardVO;
    }

    public AmountVO getAmountVO() {
        return amountVO;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
