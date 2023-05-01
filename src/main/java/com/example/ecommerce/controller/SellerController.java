package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.RequestDto.SellerRequestDto;
import com.example.ecommerce.DTO.ResponseDto.SellerResponseDto;
import com.example.ecommerce.Exceptions.InvalidSellerException;
import com.example.ecommerce.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    SellerService sellerService;

    @PostMapping("/add")
    public ResponseEntity addSeller(@RequestBody SellerRequestDto sellerRequestDto){
        //when we have multiple response types we can use ResponseEntity
        try{
            SellerResponseDto sellerResponseDto = sellerService.addSeller(sellerRequestDto);
            return new ResponseEntity(sellerResponseDto, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //  GET a seller by email
    @GetMapping("/getByEmail")
    public SellerResponseDto getSellerByEmail(@RequestParam String emailId) throws InvalidSellerException {
        return sellerService.getSellerByEmail(emailId);
    }

    // get by id
    @GetMapping("/getById")
    public SellerResponseDto getSellerById(@RequestParam int id) throws InvalidSellerException {
        return sellerService.getSellerById(id);
    }

    // get all seller
    @GetMapping("/getAllSellers")
    public List<SellerResponseDto> getAllSellers(){
        return sellerService.getAllSellers();
    }

    // get all sellers of a particular age
    @GetMapping("/getSellersByAge")
    public List<SellerResponseDto> getSellersByAge(@RequestParam int age){
        return sellerService.getSellersByAge(age);
    }

    // update seller info based on email id
    @PutMapping("/update")
    public ResponseEntity update(@RequestParam String emailId, @RequestParam String name) throws InvalidSellerException {
        sellerService.update(emailId, name);
        return new ResponseEntity("Seller updated", HttpStatus.CREATED);
    }

    // delete a seller based on email
    @DeleteMapping("/deleteByEmail")
    public ResponseEntity deleteByEmail(@RequestParam String emailId) throws InvalidSellerException {
        sellerService.deleteByEmail(emailId);
        return new ResponseEntity("Seller deleted", HttpStatus.OK);
    }

    //delete by id
    @DeleteMapping("/deleteById")
    public ResponseEntity deleteById(@RequestParam int id) throws InvalidSellerException {
        sellerService.deleteById(id);
        return new ResponseEntity("Seller deleted", HttpStatus.OK);
    }

}
