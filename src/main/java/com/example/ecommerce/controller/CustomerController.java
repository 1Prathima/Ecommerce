package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.RequestDto.CustomerEmailMobDto;
import com.example.ecommerce.DTO.RequestDto.CustomerRequestDto;
import com.example.ecommerce.DTO.ResponseDto.CustomerResponseDto;
import com.example.ecommerce.Exceptions.MobileNoAlreadyPresentException;
import com.example.ecommerce.Exceptions.NoInformationException;
import com.example.ecommerce.Transformer.CustomerTransformer;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.enums.CardType;
import com.example.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping("/add")
    public CustomerResponseDto addCustomer(@RequestBody CustomerRequestDto customerRequestDto) throws MobileNoAlreadyPresentException {
        return customerService.addCustomer(customerRequestDto);
    }

    // view all customers
    @GetMapping("/getAllCustomers")
    public List<CustomerResponseDto> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    // get a customer by email/mob
    @GetMapping("/getByEmailOrMob")
    public ResponseEntity getByEmailOrMob(@RequestBody CustomerEmailMobDto customerEmailMobDto){
        try{
            CustomerResponseDto customerResponseDto = customerService.getByEmailOrMob(customerEmailMobDto);
            return new ResponseEntity<>(customerResponseDto, HttpStatus.CREATED);
        }
        catch (NoInformationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // get all customers whose age is greater than 25
    @GetMapping("/getByAge")
    public List<CustomerResponseDto> getByAge(@RequestParam int age){
        return customerService.getByAge(age);
    }

    // get all customers who use VISA card
    @GetMapping("/getByCardType")
    public List<CustomerResponseDto> getByCardType(@RequestParam CardType cardType){
        return customerService.getByCardType(cardType);
    }

    // update a customer info by email
    @PutMapping("/updateByEmail")
    public String updateByEmail(@RequestParam String emailId, @RequestParam String address){
        customerService.updateByEmail(emailId, address);
        return "Address updated successfully!";
    }

    // delete a customer by email/mob
    @DeleteMapping("/deleteByEmailOrMob")
    public String deleteByEmailOrMob(@RequestBody CustomerEmailMobDto customerEmailMobDto){
        try{
            customerService.deleteByEmailOrMob(customerEmailMobDto);
            return "Customer deleted successfully!";
        }
        catch (NoInformationException e){
            return e.getMessage();
        }
    }
}
