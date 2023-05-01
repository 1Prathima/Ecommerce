package com.example.ecommerce.DTO.RequestDto;

import com.example.ecommerce.enums.ProductCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequestDto {
    int sellerId;
    String productName;
    int price;
    int quantity;
    ProductCategory productCategory;
}
