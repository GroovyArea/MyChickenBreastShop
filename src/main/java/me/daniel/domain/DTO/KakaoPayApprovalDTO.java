package me.daniel.domain.DTO;

import me.daniel.domain.VO.AmountVO;
import me.daniel.domain.VO.CardVO;

import java.util.Date;

public class KakaoPayApprovalDTO {

    //response
    private String aid, tid, cid, sid;
    private String partner_order_id, partner_user_id, payment_method_type;
    private AmountVO amount;
    private CardVO card_info;
    private String item_name, item_code, payload;
    private Integer quantity, tax_free_amount, vat_amount;
    private Date created_at, approved_at;

      public KakaoPayApprovalDTO(String aid, String tid, String cid, String sid, String partner_order_id, String partner_user_id, String payment_method_type, AmountVO amount, CardVO card_info, String item_name, String item_code, String payload, Integer quantity, Integer tax_free_amount, Integer vat_amount, Date created_at, Date approved_at) {
        this.aid = aid;
        this.tid = tid;
        this.cid = cid;
        this.sid = sid;
        this.partner_order_id = partner_order_id;
        this.partner_user_id = partner_user_id;
        this.payment_method_type = payment_method_type;
        this.amount = amount;
        this.card_info = card_info;
        this.item_name = item_name;
        this.item_code = item_code;
        this.payload = payload;
        this.quantity = quantity;
        this.tax_free_amount = tax_free_amount;
        this.vat_amount = vat_amount;
        this.created_at = created_at;
        this.approved_at = approved_at;
    }

    public KakaoPayApprovalDTO() {
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

    public String getPartner_order_id() {
        return partner_order_id;
    }

    public String getPartner_user_id() {
        return partner_user_id;
    }

    public String getPayment_method_type() {
        return payment_method_type;
    }

    public AmountVO getAmount() {
        return amount;
    }

    public CardVO getCard_info() {
        return card_info;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_code() {
        return item_code;
    }

    public String getPayload() {
        return payload;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getTax_free_amount() {
        return tax_free_amount;
    }

    public Integer getVat_amount() {
        return vat_amount;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getApproved_at() {
        return approved_at;
    }
}
