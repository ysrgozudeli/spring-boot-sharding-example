package com.example.sharding_demo.sharding.request_data;

public class ShopContext {

    private static final ThreadLocal<Long> SHOP_ID = new ThreadLocal<>();

    public static void setShopId(Long shopId) {

        SHOP_ID.set(shopId);
    }

    public static Long getShopId() {

        return SHOP_ID.get();
    }

    public static void clear() {

        SHOP_ID.remove();
    }
}