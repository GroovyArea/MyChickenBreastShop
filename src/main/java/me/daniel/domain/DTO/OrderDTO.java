package me.daniel.domain.DTO;

import java.beans.ConstructorProperties;

public class OrderDTO {

    String itemName;
    Integer quantity, totalAmount;

    @ConstructorProperties({"itemName","quantity", "totalAmount"})
    public OrderDTO(String itemName, Integer quantity, Integer totalAmount) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
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
}
