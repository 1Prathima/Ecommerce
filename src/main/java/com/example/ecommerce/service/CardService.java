package com.example.ecommerce.service;

import com.example.ecommerce.DTO.RequestDto.CardRequestDto;
import com.example.ecommerce.DTO.ResponseDto.CardResponseDto;
import com.example.ecommerce.Exceptions.InvalidCustomerException;
import com.example.ecommerce.enums.CardType;

import java.util.Date;
import java.util.List;


public interface CardService {
    public CardResponseDto addCard(CardRequestDto cardRequestDto) throws InvalidCustomerException;
    public List<CardResponseDto> getByCardType(String cardType);
    public List<CardResponseDto> getByExpiryDate(String cardType, Date expiryDate);
    public CardType getMaxCardType();
}
