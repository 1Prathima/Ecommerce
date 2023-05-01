package com.example.ecommerce.DTO.ResponseDto;

import com.example.ecommerce.enums.ProductStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductResponseDto {
    String sellerName;
    String productName;
    int price;
    int quantity;
    ProductStatus productStatus;
}
