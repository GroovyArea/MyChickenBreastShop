package me.daniel.domain.DTO;

import java.beans.ConstructorProperties;

public class OrderReadyDTO {

    String cid, partner_order_id, partner_user_id, item_name;
    Integer quantity, total_Amount, tax_free_amount;
    String approval_url, cancel_url, fail_url;

    @ConstructorProperties({"cid","partner_order_id","partner_user_id","item_name","quantity", "total_Amount","tax_free_amount", "approval_url", "cancel_url", "fail_url"})
    public OrderReadyDTO(String cid, String partner_order_id, String partner_user_id, String item_name, Integer quantity, Integer total_Amount, Integer tax_free_amount, String approval_url, String cancel_url, String fail_url) {
        this.cid = cid;
        this.partner_order_id = partner_order_id;
        this.partner_user_id = partner_user_id;
        this.item_name = item_name;
        this.quantity = quantity;
        this.total_Amount = total_Amount;
        this.tax_free_amount = tax_free_amount;
        this.approval_url = approval_url;
        this.cancel_url = cancel_url;
        this.fail_url = fail_url;
    }

    public String getCid() {
        return cid;
    }

    public String getPartner_user_id() {
        return partner_user_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getTax_free_amount() {
        return tax_free_amount;
    }

    public Integer getTotal_Amount() {
        return total_Amount;
    }

    public String getApproval_url() {
        return approval_url;
    }

    public String getCancel_url() {
        return cancel_url;
    }

    public String getFail_url() {
        return fail_url;
    }

    public String getPartner_order_id() {
        return partner_order_id;
    }
}
