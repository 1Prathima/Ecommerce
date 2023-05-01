package com.example.ecommerce.DTO.ResponseDto;

import com.example.ecommerce.entity.Item;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class OrderResponseDto {
    String customerName;
    String orderNo;
    Date orderDate;
    int totalValue;
    String cardUsed;
    List<ItemResponseDto> itemResponseDtos = new ArrayList<>();

}
