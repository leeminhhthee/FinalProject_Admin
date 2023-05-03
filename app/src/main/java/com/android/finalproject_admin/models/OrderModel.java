package com.android.finalproject_admin.models;

import java.util.List;

public class OrderModel {
    OrderInformationModel informationModel;
    List<OrderProductModel> products;

    public OrderModel() {
    }

    public OrderModel(OrderInformationModel informationModel, List<OrderProductModel> products) {
        this.informationModel = informationModel;
        this.products = products;
    }

    public OrderInformationModel getInformationModel() {
        return informationModel;
    }

    public void setInformationModel(OrderInformationModel informationModel) {
        this.informationModel = informationModel;
    }

    public List<OrderProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductModel> products) {
        this.products = products;
    }
}
