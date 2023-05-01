package com.example.ecommerce.Transformer;

import com.example.ecommerce.DTO.ResponseDto.CartResponseDto;
import com.example.ecommerce.entity.Cart;

public class CartTransformer {
    public static CartResponseDto CartToCartResponseDto(Cart cart){
        return CartResponseDto.builder()
                .customerName(cart.getCustomer().getName())
                .numberOfItems(cart.getNumberOfItems())
                .cartTotal(cart.getCartTotal())
                .build();
    }
}
