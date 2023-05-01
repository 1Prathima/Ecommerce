package com.example.ecommerce.service;

import com.example.ecommerce.DTO.RequestDto.OrderRequestDto;
import com.example.ecommerce.DTO.ResponseDto.HighestBillResponseDto;
import com.example.ecommerce.DTO.ResponseDto.OrderResponseDto;
import com.example.ecommerce.Exceptions.InvalidCardException;
import com.example.ecommerce.Exceptions.InvalidCustomerException;
import com.example.ecommerce.Exceptions.InvalidProductException;
import com.example.ecommerce.Exceptions.OutOfStockException;
import com.example.ecommerce.entity.Card;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.entity.Ordered;

import java.util.List;

public interface OrderedService {
    public OrderResponseDto placeDirectOrder(OrderRequestDto orderRequestDto) throws InvalidCustomerException, InvalidProductException, InvalidCardException, OutOfStockException;
    public Ordered placeOrderForCheckOutCart(Customer customer, Card card);
    public List<OrderResponseDto> getAllOrders(int customerId);
    public List<OrderResponseDto> getRecentOrders(int customerId);
    public void deleteOrder(int orderId);
    public HighestBillResponseDto getHighestBillOrder();
}
