package com.example.ecommerce.service;

import com.example.ecommerce.DTO.RequestDto.ItemRequestDto;
import com.example.ecommerce.Exceptions.InvalidCustomerException;
import com.example.ecommerce.Exceptions.InvalidProductException;
import com.example.ecommerce.Exceptions.OutOfStockException;
import com.example.ecommerce.entity.Item;

public interface ItemService {
    public Item addItem(ItemRequestDto itemRequestDto) throws InvalidCustomerException, InvalidProductException, OutOfStockException;
}
