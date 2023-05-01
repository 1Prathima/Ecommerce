package com.example.ecommerce.service;

import com.example.ecommerce.DTO.RequestDto.SellerRequestDto;
import com.example.ecommerce.DTO.ResponseDto.SellerResponseDto;
import com.example.ecommerce.Exceptions.EmailAlreadyPresentException;
import com.example.ecommerce.Exceptions.InvalidSellerException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SellerService {

    public SellerResponseDto addSeller(SellerRequestDto sellerRequestDto) throws EmailAlreadyPresentException;
    public SellerResponseDto getSellerByEmail(String emailId) throws InvalidSellerException;
    public SellerResponseDto getSellerById(int id) throws InvalidSellerException;
    public List<SellerResponseDto> getAllSellers();
    public List<SellerResponseDto> getSellersByAge(int age);
    public void update(String emailId, String name) throws InvalidSellerException;
    public  void deleteByEmail(String emailId) throws InvalidSellerException;
    public void deleteById(int id) throws InvalidSellerException;
}
