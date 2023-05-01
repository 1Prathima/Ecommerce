package com.example.ecommerce.service.impl;

import com.example.ecommerce.DTO.RequestDto.OrderRequestDto;
import com.example.ecommerce.DTO.ResponseDto.HighestBillResponseDto;
import com.example.ecommerce.DTO.ResponseDto.ItemResponseDto;
import com.example.ecommerce.DTO.ResponseDto.OrderResponseDto;
import com.example.ecommerce.Exceptions.InvalidCardException;
import com.example.ecommerce.Exceptions.InvalidCustomerException;
import com.example.ecommerce.Exceptions.InvalidProductException;
import com.example.ecommerce.Exceptions.OutOfStockException;
import com.example.ecommerce.Transformer.ItemTransformer;
import com.example.ecommerce.Transformer.OrderedTransformer;
import com.example.ecommerce.entity.*;
import com.example.ecommerce.enums.ProductStatus;
import com.example.ecommerce.repository.CardRepository;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.OrderedRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.OrderedService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderedServiceImpl implements OrderedService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    OrderedRepository orderedRepository;
    @Autowired
    private JavaMailSender emailSender;

    @Override
    public OrderResponseDto placeDirectOrder(OrderRequestDto orderRequestDto) throws InvalidCustomerException, InvalidProductException, InvalidCardException, OutOfStockException {
        //first check for valid customer,product and card
        Customer customer;
        try{
            customer = customerRepository.findById(orderRequestDto.getCustomerId()).get();
        }
        catch (Exception e){
            throw new InvalidCustomerException("Customer id is invalid.");
        }
        Product product;
        try{
            product = productRepository.findById(orderRequestDto.getProductId()).get();
        }
        catch (Exception e){
            throw new InvalidProductException("Product does not exist.");
        }
        Card card = cardRepository.findByCardNo(orderRequestDto.getCardNo());
        if(card==null || card.getCvv()!= orderRequestDto.getCvv() || card.getCustomer()!=customer){
            throw new InvalidCardException("Your card is not valid.");
        }

        //check required quantity, if available make an order
        if(orderRequestDto.getRequiredQuantity() > product.getQuantity()){
            throw new OutOfStockException("The available quantity is only "+ product.getQuantity());
        }
        //decrease the product quantity in database
        product.setQuantity(product.getQuantity() - orderRequestDto.getRequiredQuantity());
        if(product.getQuantity()==0){
            product.setProductStatus(ProductStatus.OUT_OF_STOCK);
        }

        //make an item
        Item item = Item.builder()
                .product(product)
                .requiredQuantity(orderRequestDto.getRequiredQuantity())
                .build();

        //make an order and set parameters
        Ordered ordered = Ordered.builder()
                .customer(customer)
                .orderNo(String.valueOf(UUID.randomUUID()))
                .totalValue(item.getRequiredQuantity() * product.getPrice())
                .build();
        String maskedCardNo = generateMaskedCard(card.getCardNo());
        ordered.setCardUsed(maskedCardNo);
        List<Item> items = ordered.getItems();
        if(items==null){
            items = new ArrayList<>();
        }
        items.add(item);
        ordered.setItems(items);

        item.setOrder(ordered);
        customer.getOrders().add(ordered);

        Ordered savedOrder = orderedRepository.save(ordered);  //saves order and item

        //prepare orderResponseDto to return
        OrderResponseDto orderResponseDto = OrderedTransformer.OrderToOrderResponseDto(savedOrder);
        List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
        for(Item item1 : savedOrder.getItems()){
            itemResponseDtos.add(ItemTransformer.ItemToItemResponseDto(item1));
        }
        orderResponseDto.setItemResponseDtos(itemResponseDtos);

//        sending mail
        SimpleMailMessage message = new SimpleMailMessage();  //class that is used to send mails
        message.setFrom("prat8971@gmail.com");
        message.setTo(customer.getEmailId());
        message.setSubject("Order Successfully Placed");
        message.setText("Hey "+customer.getName()+"! Your order#"+ordered.getOrderNo()+" worth INR "+ordered.getTotalValue()+ " has been successfully placed. Thank you for your order!");
        emailSender.send(message);
        return orderResponseDto;
    }

    @Override
    public Ordered placeOrderForCheckOutCart(Customer customer, Card card) {
        Cart cart = customer.getCart();

        //create order and set parameters
        Ordered ordered = Ordered.builder()
                .customer(customer)
                .orderNo(String.valueOf(UUID.randomUUID()))
                .items(cart.getItems())
                .totalValue(cart.getCartTotal())
                .build();
        String maskedCardNo = generateMaskedCard(card.getCardNo());
        ordered.setCardUsed(maskedCardNo);

//        Ordered savedOrder = orderedRepository.save(ordered); //saves order and item
        return ordered;
    }

    @Override
    public List<OrderResponseDto> getAllOrders(int customerId) {
        Customer customer = customerRepository.findById(customerId).get();
        List<Ordered> orderedList = customer.getOrders();

        List<OrderResponseDto> orderResponseDtos = new ArrayList<>();
        for(Ordered ordered : orderedList){
            List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
            for(Item item : ordered.getItems()){
                itemResponseDtos.add(ItemTransformer.ItemToItemResponseDto(item));
            }
            OrderResponseDto orderResponseDto = OrderedTransformer.OrderToOrderResponseDto(ordered);
            orderResponseDto.setItemResponseDtos(itemResponseDtos);

            orderResponseDtos.add(orderResponseDto);
        }
        return orderResponseDtos;
    }

    @Override
    public List<OrderResponseDto> getRecentOrders(int customerId) {
        //using query
        List<Ordered> orderedList = orderedRepository.getRecentOrders(customerId);

        List<OrderResponseDto> orderResponseDtos = new ArrayList<>();
        for(Ordered ordered : orderedList){
            List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
            for(Item item : ordered.getItems()){
                itemResponseDtos.add(ItemTransformer.ItemToItemResponseDto(item));
            }
            OrderResponseDto orderResponseDto = OrderedTransformer.OrderToOrderResponseDto(ordered);
            orderResponseDto.setItemResponseDtos(itemResponseDtos);

            orderResponseDtos.add(orderResponseDto);
        }
        return orderResponseDtos;

//        By iterating over the list
//        Customer customer = customerRepository.findById(customerId).get();
//        List<Ordered> orderedList = customer.getOrders();
//
//        List<OrderResponseDto> orderResponseDtos = new ArrayList<>();
//        for(int i=orderedList.size()-1;i>=orderedList.size()-2;i--){
//            List<ItemResponseDto> itemResponseDtos = new ArrayList<>();
//            for(Item item : orderedList.get(i).getItems()){
//                itemResponseDtos.add(ItemTransformer.ItemToItemResponseDto(item));
//            }
//            OrderResponseDto orderResponseDto = OrderedTransformer.OrderToOrderResponseDto(orderedList.get(i));
//            orderResponseDto.setItemResponseDtos(itemResponseDtos);
//
//            orderResponseDtos.add(orderResponseDto);
//        }
//        return orderResponseDtos;
    }

    @Override
    public void deleteOrder(int orderId) {
        orderedRepository.deleteById(orderId);
    }

    @Override
    public HighestBillResponseDto getHighestBillOrder() {
        //using query
        Ordered ordered = orderedRepository.getHighestBillOrder();
        HighestBillResponseDto highestBillResponseDto = HighestBillResponseDto.builder()
                .customerName(ordered.getCustomer().getName())
                .orderNo(ordered.getOrderNo())
                .totalValue(ordered.getTotalValue())
                .build();

        return highestBillResponseDto;

//        By iterating over the list
//        Ordered ordered = null;
//        int max = 0;
//        for(Ordered ordered1 : orderedRepository.findAll()){
//            if(ordered1.getTotalValue() > max){
//                max = ordered1.getTotalValue();
//                ordered = ordered1;
//            }
//        }
//        HighestBillResponseDto highestBillResponseDto = HighestBillResponseDto.builder()
//                .customerName(ordered.getCustomer().getName())
//                .orderNo(ordered.getOrderNo())
//                .totalValue(ordered.getTotalValue())
//                .build();
//
//        return highestBillResponseDto;
    }

    public String generateMaskedCard(String cardNo){
        String maskedCardNo = "";
        for(int i=0;i<cardNo.length()-4;i++){
            maskedCardNo += "X";
        }
        maskedCardNo += cardNo.substring(cardNo.length()-4);
        return maskedCardNo;
    }
}
