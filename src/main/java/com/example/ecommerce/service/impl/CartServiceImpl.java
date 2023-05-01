package com.example.ecommerce.service.impl;

import com.example.ecommerce.DTO.RequestDto.CheckOutRequestDto;
import com.example.ecommerce.DTO.RequestDto.RemoveItemRequestDto;
import com.example.ecommerce.DTO.ResponseDto.CartResponseDto;
import com.example.ecommerce.DTO.ResponseDto.ItemResponseDto;
import com.example.ecommerce.DTO.ResponseDto.OrderResponseDto;
import com.example.ecommerce.Exceptions.*;
import com.example.ecommerce.Transformer.CartTransformer;
import com.example.ecommerce.Transformer.ItemTransformer;
import com.example.ecommerce.Transformer.OrderedTransformer;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    OrderedService orderedService;
    @Autowired
    OrderedRepository orderedRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public CartResponseDto addToCart(int customerId, Item item) {
        Customer customer = customerRepository.findById(customerId).get();

        Cart cart = customer.getCart();
        cart.getItems().add(item);
        //        cart.setNumberOfItems(cart.getItems().size()); //count of unique items
        int newNumberOfItems = cart.getNumberOfItems() + item.getRequiredQuantity();
        cart.setNumberOfItems(newNumberOfItems);
        int newCartTotal = cart.getCartTotal() + (item.getRequiredQuantity() * item.getProduct().getPrice());
        cart.setCartTotal(newCartTotal);

        Cart savedCart = cartRepository.save(cart);
        CartResponseDto cartResponseDto = CartTransformer.CartToCartResponseDto(savedCart);
        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
        for(Item item1 : savedCart.getItems()){
            itemResponseDtos.add(ItemTransformer.ItemToItemResponseDto(item1));
        }
        cartResponseDto.setItemResponseDtos(itemResponseDtos);

        return cartResponseDto;

    }

    @Override
    public OrderResponseDto checkOutCart(CheckOutRequestDto checkOutRequestDto) throws InvalidCustomerException, InvalidCardException, CartIsEmptyException, OutOfStockException {
       //first check for valid customer and card
        Customer customer;
        try{
            customer = customerRepository.findById(checkOutRequestDto.getCustomerId()).get();
        }
        catch (Exception e){
            throw new InvalidCustomerException("Customer id is invalid.");
        }
        Card card = cardRepository.findByCardNo(checkOutRequestDto.getCardNo());
        if(card==null || card.getCvv()!= checkOutRequestDto.getCvv() || card.getCustomer()!=customer){
            throw new InvalidCardException("Card is not valid");
        }

        //check if cart is empty
        Cart cart = customer.getCart();
        if(cart.getItems().size() == 0){
            throw new CartIsEmptyException("Cart is empty.");
        }

        //check products quantity
        for(Item item : cart.getItems()){
            Product product = item.getProduct();
            if(item.getRequiredQuantity() > product.getQuantity()){
                throw new OutOfStockException(product.getName() + " is out of stock");
            }
        }

        //decrease the product quantity in database
        for(Item item : cart.getItems()){
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getRequiredQuantity());
        }

        //Create an order
        Ordered ordered = orderedService.placeOrderForCheckOutCart(customer, card);

        //set order for all the items in the cart
        for(Item item : cart.getItems()){
            item.setOrder(ordered);
        }

        customer.getOrders().add(ordered); //add new order to the list of orders

        //update the cart
        cart.setNumberOfItems(0);
        cart.setCartTotal(0);
        cart.setItems(new ArrayList<>());

        Ordered savedOrder = orderedRepository.save(ordered); //saves order and item

        //make orderResponseDto to return
        OrderResponseDto orderResponseDto = OrderedTransformer.OrderToOrderResponseDto(savedOrder);
        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
        for(Item item : savedOrder.getItems()){
            itemResponseDtos.add(ItemTransformer.ItemToItemResponseDto(item));
        }
        orderResponseDto.setItemResponseDtos(itemResponseDtos);

        //sending mail
        SimpleMailMessage message = new SimpleMailMessage();  //class that is used to send mails
        message.setFrom("prat8971@gmail.com");
        message.setTo(customer.getEmailId());
        message.setSubject("Order Successfully Placed");
        message.setText("Hey "+customer.getName()+"! Your order#"+ordered.getOrderNo()+" worth INR "+ordered.getTotalValue()+ " has been successfully placed. Thank you for your order!");
        emailSender.send(message);

        return orderResponseDto;
    }

    @Override
    public void removeFromCart(RemoveItemRequestDto removeItemRequestDto) throws InvalidCardException, InvalidProductException {
        Customer customer;
        try {
            customer = customerRepository.findById(removeItemRequestDto.getCustomerId()).get();
        }
        catch (Exception e){
            throw new InvalidCardException("Customer id is invalid");
        }
        Product product;
        try {
            product = productRepository.findById(removeItemRequestDto.getProductId()).get();
        }
        catch (Exception e){
            throw new InvalidProductException("Product does not exist.");
        }

        Cart cart = customer.getCart();
        List<Item> items = cart.getItems();
        for(Item item : items){
            if(item.getProduct().equals(product)){
                //deleting item
                items.remove(item);
                itemRepository.delete(item);

                //updating cart
                int newNumberOfItems = cart.getNumberOfItems() - item.getRequiredQuantity();
                cart.setNumberOfItems(newNumberOfItems);
                int newCartTotal = cart.getCartTotal() - (item.getRequiredQuantity() * product.getPrice());
                cart.setCartTotal(newCartTotal);
                cartRepository.save(cart);
                return;
            }
        }
    }

    @Override
    public List<ItemResponseDto> getAllItems(int customerId) {
        Customer customer = customerRepository.findById(customerId).get();
        Cart cart = customer.getCart();

        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
        for(Item item : cart.getItems()){
            itemResponseDtos.add(ItemTransformer.ItemToItemResponseDto(item));
        }
        return itemResponseDtos;
    }
}
