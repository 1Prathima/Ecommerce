package com.example.ecommerce.service;

import com.example.ecommerce.DTO.RequestDto.CheckOutRequestDto;
import com.example.ecommerce.DTO.RequestDto.RemoveItemRequestDto;
import com.example.ecommerce.DTO.ResponseDto.CartResponseDto;
import com.example.ecommerce.DTO.ResponseDto.ItemResponseDto;
import com.example.ecommerce.DTO.ResponseDto.OrderResponseDto;
import com.example.ecommerce.Exceptions.*;
import com.example.ecommerce.entity.Item;

import java.util.List;

public interface CartService {
    public CartResponseDto addToCart(int customerId, Item item);
    public OrderResponseDto checkOutCart(CheckOutRequestDto checkOutRequestDto) throws InvalidCustomerException, InvalidCardException, CartIsEmptyException, OutOfStockException;
    public void removeFromCart(RemoveItemRequestDto removeItemRequestDto) throws InvalidCardException, InvalidProductException;
    public List<ItemResponseDto> getAllItems(int customerId);
}
