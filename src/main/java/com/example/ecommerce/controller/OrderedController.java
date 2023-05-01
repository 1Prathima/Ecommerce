package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.RequestDto.OrderRequestDto;
import com.example.ecommerce.DTO.ResponseDto.HighestBillResponseDto;
import com.example.ecommerce.DTO.ResponseDto.OrderResponseDto;
import com.example.ecommerce.service.OrderedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderedController {
    @Autowired
    OrderedService orderedService;

    @PostMapping("/placeDirectOrder")
    public ResponseEntity placeDirectOrder(@RequestBody OrderRequestDto orderRequestDto){
        try{
            OrderResponseDto orderResponseDto = orderedService.placeDirectOrder(orderRequestDto);
            return new ResponseEntity(orderResponseDto, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // get all the orders for a customer
    @GetMapping("/getAllOrders")
    public List<OrderResponseDto> getAllOrders(@RequestParam int customerId){
        return orderedService.getAllOrders(customerId);
    }

    // get recent 5 orders
    @GetMapping("/getRecentOrders")
    public List<OrderResponseDto> getRecentOrders(@RequestParam int customerId){
        return orderedService.getRecentOrders(customerId);
    }

    // delete an order from the order list
    @DeleteMapping("/delete")
    public String deleteOrder(@RequestParam int orderId){
        orderedService.deleteOrder(orderId);
        return "Order has been deleted";
    }

    // select the order and also tell the customer name with the highest total value.
    @GetMapping("/getHighestBillOrder")
    public HighestBillResponseDto getHighestBillOrder(){
        return orderedService.getHighestBillOrder();
    }


}
