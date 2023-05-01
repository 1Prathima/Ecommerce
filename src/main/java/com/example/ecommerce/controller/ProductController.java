package com.example.ecommerce.controller;

import com.example.ecommerce.DTO.RequestDto.ProductRequestDto;
import com.example.ecommerce.DTO.ResponseDto.ProductResponseDto;
import com.example.ecommerce.Exceptions.InvalidSellerException;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.enums.ProductCategory;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping("/add")
    public ProductResponseDto addProduct(@RequestBody ProductRequestDto productRequestDto) throws InvalidSellerException {
        return productService.addProduct(productRequestDto);
    }

    @GetMapping("/get/{category}")
    public List<ProductResponseDto> getProductsByCategory(@PathVariable("category") ProductCategory category){
        return productService.getProductsByCategory(category);
    }

    // Get all product by seller email id
    @GetMapping("/getProductById")
    public List<ProductResponseDto> getProductById(@RequestParam int sellerId){
        return productService.getProductById(sellerId);
    }

    // delete a product by seller id and product id
    @DeleteMapping("/deleteById")
    public String deleteProduct(@RequestParam int sellerId, @RequestParam int productId){
        productService.deleteProduct(sellerId, productId);
        return "Product deleted successfully";
    }

    // return top 5 cheapest products
    @GetMapping("/getCheapest/{n}")
    public List<ProductResponseDto> getCheapest(@PathVariable("n") int n){
        return productService.getCheapest(n);
    }

    // return top 5 costliest products
    @GetMapping("/getCostliest/{n}")
    public List<ProductResponseDto>  getCostliest(@PathVariable("n") int n){
        return productService.getCostliest(n);
    }

    // return all out of stock products
    @GetMapping("/getOutOfStock")
    public List<ProductResponseDto> getOutOfStock(){
        return productService.getOutOfStock();
    }

    // return all available products
    @GetMapping("/getAvailable")
    public List<ProductResponseDto> getAvailable(){
        return productService.getAvailable();
    }

    // return all products that have quantity less than 10
    @GetMapping("/getByQuantity")
    public List<ProductResponseDto> getByQuantity(@RequestParam int n){
        return productService.getByQuantity(n);
    }

    // return the cheapest product in a particular category
    @GetMapping("/cheapestInCategory")
    public ProductResponseDto cheapestInCategory(){
        return productService.cheapestInCategory();
    }

    // return the costliest product in a particular category
    @GetMapping("/costliestInCategory")
    public ProductResponseDto costliestInCategory(){
        return productService.costliestInCategory();
    }

    @GetMapping("/get/{price}/{category}")
    public List<ProductResponseDto> getByPriceAndCategory(@PathVariable("price") int price, @PathVariable("category") String category){
        return productService.getByPriceAndCategory(price, category);
    }

    @PutMapping("/update")
    public String updateById(@RequestParam int productId,@RequestParam String productName){
        productService.updateById(productId,productName);
        return "Product name updated";
    }
}
