package com.example.ecommerce.Transformer;

import com.example.ecommerce.DTO.ResponseDto.OrderResponseDto;
import com.example.ecommerce.entity.Ordered;

public class OrderedTransformer {
    public static OrderResponseDto OrderToOrderResponseDto(Ordered ordered){
        return OrderResponseDto.builder()
                .customerName(ordered.getCustomer().getName())
                .orderNo(ordered.getOrderNo())
                .orderDate(ordered.getOrderDate())
                .totalValue(ordered.getTotalValue())
                .cardUsed(ordered.getCardUsed())
                .build();
    }
}
