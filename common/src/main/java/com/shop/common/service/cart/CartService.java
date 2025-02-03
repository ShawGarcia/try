package com.shop.common.service.cart;

public interface CartService {
    AddItemResp addItem(AddItemReq request);

    GetCartResp getCart(GetCartReq request);

    EmptyCartResp emptyCart(EmptyCartReq request);
}