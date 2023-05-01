package com.example.ecommerce.service.impl;

import com.example.ecommerce.DTO.RequestDto.ItemRequestDto;
import com.example.ecommerce.Exceptions.InvalidCustomerException;
import com.example.ecommerce.Exceptions.InvalidProductException;
import com.example.ecommerce.Exceptions.OutOfStockException;
import com.example.ecommerce.Transformer.ItemTransformer;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.enums.ProductStatus;
import com.example.ecommerce.repository.CustomerRepository;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ItemRepository itemRepository;

    @Override
    public Item addItem(ItemRequestDto itemRequestDto) throws InvalidCustomerException, InvalidProductException, OutOfStockException {
        Customer customer;
        try{
            customer = customerRepository.findById(itemRequestDto.getCustomerId()).get();
        }
        catch (Exception e){
            throw new InvalidCustomerException("Customer id is invalid.");
        }

        Product product;
        try{
            product = productRepository.findById(itemRequestDto.getProductId()).get();
        }
        catch (Exception e){
            throw new InvalidProductException("Product does not exist.");
        }

        if(itemRequestDto.getRequiredQuantity() > product.getQuantity() || product.getProductStatus() != ProductStatus.AVAILABLE){
            throw new OutOfStockException("Product out of stock.");
        }

        Item item = ItemTransformer.ItemRequestDtoToItem(itemRequestDto);
        item.setCart(customer.getCart());
        item.setProduct(product);

        product.getItems().add(item);
//        Product savedProduct = productRepository.save(product); //saves product and item
        //(instead saving only item will also work by doing itemRepository.save(item))
//        int size =  savedProduct.getItems().size();
//        return savedProduct.getItems().get(size-1);  //returns the recently added item
        return itemRepository.save(item);
    }
}
