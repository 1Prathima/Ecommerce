package com.example.ecommerce.Transformer;

import com.example.ecommerce.DTO.RequestDto.ItemRequestDto;
import com.example.ecommerce.DTO.ResponseDto.ItemResponseDto;
import com.example.ecommerce.entity.Item;

public class ItemTransformer {

    public static Item ItemRequestDtoToItem(ItemRequestDto itemRequestDto){
        return Item.builder()
                .requiredQuantity(itemRequestDto.getRequiredQuantity())
                .build();
    }

    public static ItemResponseDto ItemToItemResponseDto(Item item){
        return ItemResponseDto.builder()
                .productName(item.getProduct().getName())
                .quantity(item.getRequiredQuantity())
                .priceOfOneItem(item.getProduct().getPrice())
                .totalPrice(item.getRequiredQuantity() * item.getProduct().getPrice())
                .build();
    }
}
