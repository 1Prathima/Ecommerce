package com.example.ecommerce.service.impl;

import com.example.ecommerce.DTO.RequestDto.CustomerEmailMobDto;
import com.example.ecommerce.DTO.RequestDto.CustomerRequestDto;
import com.example.ecommerce.DTO.ResponseDto.CustomerResponseDto;
import com.example.ecommerce.Exceptions.MobileNoAlreadyPresentException;
import com.example.ecommerce.Exceptions.NoInformationException;
import com.example.ecommerce.Transformer.CustomerTransformer;
import com.example.ecommerce.entity.Card;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.enums.CardType;
import com.example.ecommerce.repository.CardRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CardRepository cardRepository;

    @Override
    public CustomerResponseDto addCustomer(CustomerRequestDto customerRequestDto) throws MobileNoAlreadyPresentException {
        if(customerRepository.findByMobNo(customerRequestDto.getMobNo()) != null){
            throw new MobileNoAlreadyPresentException("Sorry! Customer already exists!");
        }

        Customer customer = CustomerTransformer.CustomerRequestDtoToCustomer(customerRequestDto);
        Cart cart = Cart.builder()
                .numberOfItems(0)
                .cartTotal(0)
                .customer(customer)
                .build();

        customer.setCart(cart);
        Customer savedCustomer = customerRepository.save(customer);  //saves customer & cart

        //prepare response dto
        return CustomerTransformer.CustomerToCustomerResponseDto(savedCustomer);

    }

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerResponseDto> customerResponseDtos = new ArrayList<>();
        for(Customer customer : customers){
            customerResponseDtos.add(CustomerTransformer.CustomerToCustomerResponseDto(customer));
        }

        return customerResponseDtos;
    }

    @Override
    public CustomerResponseDto getByEmailOrMob(CustomerEmailMobDto customerEmailMobDto) throws NoInformationException {
        if(customerEmailMobDto.getEmailId()==null && customerEmailMobDto.getMobNo()==null){
            throw new NoInformationException("You have to enter atleast one information");
        }
        Customer customer;
        if(customerEmailMobDto.getEmailId() != null){
            customer = customerRepository.findByEmailId(customerEmailMobDto.getEmailId());
            return CustomerTransformer.CustomerToCustomerResponseDto(customer);
        }
        else{
            customer = customerRepository.findByMobNo(customerEmailMobDto.getMobNo());
            return CustomerTransformer.CustomerToCustomerResponseDto(customer);
        }
    }

    @Override
    public List<CustomerResponseDto> getByAge(int age) {
        List<Customer> customers = customerRepository.getByAge(age);

        List<CustomerResponseDto> customerResponseDtos = new ArrayList<>();
        for(Customer customer : customers){
            customerResponseDtos.add(CustomerTransformer.CustomerToCustomerResponseDto(customer));
        }

        return customerResponseDtos;
    }

    @Override
    public List<CustomerResponseDto> getByCardType(CardType cardType) {
        List<Customer> customers = customerRepository.findAll();

        List<CustomerResponseDto> customerResponseDtos = new ArrayList<>();
        for(Customer customer : customers){
            List<Card> cards = customer.getCards();
           for(Card card : cards){
               if(card.getCardType().equals(cardType)){
                   customerResponseDtos.add(CustomerTransformer.CustomerToCustomerResponseDto(customer));
               }
           }
        }

        return customerResponseDtos;
    }

    @Override
    public void updateByEmail(String emailId, String address) {
        Customer customer = customerRepository.findByEmailId(emailId);
        customer.setAddress(address);
        customerRepository.save(customer);
    }

    @Override
    public void deleteByEmailOrMob(CustomerEmailMobDto customerEmailMobDto) throws NoInformationException {
        if(customerEmailMobDto.getEmailId()==null && customerEmailMobDto.getMobNo()==null){
            throw new NoInformationException("You have to enter atleast one information");
        }

        Customer customer;
        if(customerRepository.findByEmailId(customerEmailMobDto.getEmailId()) != null){
//            customerRepository.deleteByEmailId(customerEmailMobDto.getEmailId()); //not working
            customer = customerRepository.findByEmailId(customerEmailMobDto.getEmailId());
        }
        else{
            customer = customerRepository.findByMobNo(customerEmailMobDto.getMobNo());
        }

        customerRepository.delete(customer);
    }
}
