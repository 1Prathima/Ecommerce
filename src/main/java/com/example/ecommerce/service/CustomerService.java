package com.example.ecommerce.service;

import com.example.ecommerce.DTO.RequestDto.CustomerEmailMobDto;
import com.example.ecommerce.DTO.RequestDto.CustomerRequestDto;
import com.example.ecommerce.DTO.ResponseDto.CustomerResponseDto;
import com.example.ecommerce.Exceptions.MobileNoAlreadyPresentException;
import com.example.ecommerce.Exceptions.NoInformationException;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.enums.CardType;

import java.util.List;

public interface CustomerService {
    public CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto) throws MobileNoAlreadyPresentException;
    public List<CustomerResponseDto> getAllCustomers();
    public CustomerResponseDto getByEmailOrMob(CustomerEmailMobDto customerEmailMobDto) throws NoInformationException;
    public List<CustomerResponseDto> getByAge(int age);
    public List<CustomerResponseDto> getByCardType(CardType cardType);
    public void updateByEmail(String emailId, String address);
    public void deleteByEmailOrMob(CustomerEmailMobDto customerEmailMobDto) throws NoInformationException;
}
