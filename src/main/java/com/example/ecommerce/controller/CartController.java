package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.RequestDto.CheckOutRequestDto;
import com.example.ecommerce.DTO.RequestDto.ItemRequestDto;
import com.example.ecommerce.DTO.RequestDto.RemoveItemRequestDto;
import com.example.ecommerce.DTO.ResponseDto.CartResponseDto;
import com.example.ecommerce.DTO.ResponseDto.ItemResponseDto;
import com.example.ecommerce.DTO.ResponseDto.OrderResponseDto;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    ItemService itemService;
    @Autowired
    CartService cartService;

    @PostMapping("/addToCart")
    public ResponseEntity addToCart(@RequestBody ItemRequestDto itemRequestDto){
//        Cart consists of list of items so we need to create item to add it to the cart
        try{
            Item savedItem = itemService.addItem(itemRequestDto);
            CartResponseDto cartResponseDto = cartService.addToCart(itemRequestDto.getCustomerId(), savedItem);
            return new ResponseEntity<>(cartResponseDto, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/checkOutCart")
    public ResponseEntity checkOutCart(@RequestBody CheckOutRequestDto checkOutRequestDto){
        try{
            OrderResponseDto orderResponseDto = cartService.checkOutCart(checkOutRequestDto);
            return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // remove from cart
    @DeleteMapping("/delete")
    public ResponseEntity removeFromCart(@RequestBody RemoveItemRequestDto removeItemRequestDto){
        try {
            cartService.removeFromCart(removeItemRequestDto);
            return new ResponseEntity<>("Item removed from cart.", HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // view all items in cart
    @GetMapping("getAllItems")
    public List<ItemResponseDto> getAllItems(@RequestParam int customerId){
        return cartService.getAllItems(customerId);
    }

    // email sending for checkOutCart and placeOrder
    //send customer name and total value of order

    // my email - kunaljindal995@gmail.com
}
