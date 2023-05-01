package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.RequestDto.CardRequestDto;
import com.example.ecommerce.DTO.RequestDto.CustomerRequestDto;
import com.example.ecommerce.DTO.ResponseDto.CardResponseDto;
import com.example.ecommerce.DTO.ResponseDto.CustomerResponseDto;
import com.example.ecommerce.Exceptions.InvalidCustomerException;
import com.example.ecommerce.entity.Card;
import com.example.ecommerce.enums.CardType;
import com.example.ecommerce.repository.CardRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/card")
public class CardController {
    @Autowired
    CardService cardService;

    @PostMapping("/add")
    public ResponseEntity addCard(@RequestBody CardRequestDto cardRequestDto){
        try {
            CardResponseDto cardResponseDto = cardService.addCard(cardRequestDto);
            return new ResponseEntity(cardResponseDto, HttpStatus.CREATED);
        }
        catch (InvalidCustomerException e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // get all VISA cards
    @GetMapping("/getByCardType")
    public List<CardResponseDto> getByCardType(@RequestParam String cardType){
        return cardService.getByCardType(cardType);
    }

    // get all MASTERCARD cards whose expiry is greater than 1 Jan 2025
    @GetMapping("/getByExpiryDate")
    public List<CardResponseDto> getByExpiryDate(@RequestParam String cardType, @RequestParam Date expiryDate){
        return cardService.getByExpiryDate(cardType, expiryDate);
    }

    // Return the CardType which has maximum number of that card
    @GetMapping("/getMaxCardType")
    public CardType getMaxCardType(){
        return cardService.getMaxCardType();
    }
}
