package com.example.ecommerce.service;

import com.example.ecommerce.DTO.RequestDto.ProductRequestDto;
import com.example.ecommerce.DTO.ResponseDto.ProductResponseDto;
import com.example.ecommerce.Exceptions.InvalidSellerException;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.enums.ProductCategory;

import java.util.List;

public interface ProductService {
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) throws InvalidSellerException;
    public List<ProductResponseDto> getProductsByCategory(ProductCategory category);
    public List<ProductResponseDto> getProductById(int sellerId);
    public void deleteProduct(int sellerId, int productId);
    public List<ProductResponseDto> getCheapest(int n);
    public List<ProductResponseDto> getCostliest(int n);
    public List<ProductResponseDto> getOutOfStock();
    public List<ProductResponseDto> getAvailable();
    public List<ProductResponseDto> getByQuantity(int n);
    public ProductResponseDto cheapestInCategory();
    public ProductResponseDto costliestInCategory();
    public List<ProductResponseDto> getByPriceAndCategory(int price, String category);
    public void updateById(int productId, String productName);
}
