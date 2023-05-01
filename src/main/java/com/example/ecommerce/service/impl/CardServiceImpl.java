package com.example.ecommerce.service.impl;

import com.example.ecommerce.DTO.RequestDto.CardRequestDto;
import com.example.ecommerce.DTO.ResponseDto.CardResponseDto;
import com.example.ecommerce.Exceptions.InvalidCustomerException;
import com.example.ecommerce.Transformer.CardTransformer;
import com.example.ecommerce.entity.Card;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.enums.CardType;
import com.example.ecommerce.repository.CardRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public CardResponseDto addCard(CardRequestDto cardRequestDto) throws InvalidCustomerException {
        Customer customer = customerRepository.findByMobNo(cardRequestDto.getMobNo());
        if(customer == null){
            throw new InvalidCustomerException("Sorry! The customer does not exist.");
        }
        Card card = CardTransformer.CardRequestDtoToCard(cardRequestDto);
        card.setCustomer(customer);

        customer.getCards().add(card);
        customerRepository.save(customer);

        //prepare response dto
        return CardTransformer.CardToCardResponseDto(card);
    }

    @Override
    public List<CardResponseDto> getByCardType(String cardType) {
        List<Card> cards = cardRepository.getByCardType(cardType);

        List<CardResponseDto> cardResponseDtos = new ArrayList<>();
        for(Card card : cards){
            cardResponseDtos.add(CardTransformer.CardToCardResponseDto(card));
        }

        return cardResponseDtos;
    }

    @Override
    public List<CardResponseDto> getByExpiryDate(String cardType, Date expiryDate) {
        List<Card> cards = cardRepository.getByExpiryDate(cardType, expiryDate);

        List<CardResponseDto> cardResponseDtos = new ArrayList<>();
        for(Card card : cards){
            cardResponseDtos.add(CardTransformer.CardToCardResponseDto(card));
        }

        return cardResponseDtos;
    }

    @Override
    public CardType getMaxCardType() {
        int visaCount = cardRepository.getByCardType("VISA").size();
        int mastercardCount = cardRepository.getByCardType("MASTERCARD").size();
        int rupayCount = cardRepository.getByCardType("RUPAY").size();

        CardType cardType = CardType.VISA;
        if(visaCount > mastercardCount && visaCount > rupayCount){
            cardType = CardType.VISA;
        }
        if(mastercardCount > visaCount && mastercardCount > rupayCount){
            cardType = CardType.MASTERCARD;
        }
        if(rupayCount > visaCount && rupayCount > mastercardCount){
            cardType = CardType.RUPAY;
        }

        return cardType;

    }
}
