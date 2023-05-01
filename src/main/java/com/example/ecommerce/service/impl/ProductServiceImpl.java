package com.example.ecommerce.service.impl;

import com.example.ecommerce.DTO.RequestDto.ProductRequestDto;
import com.example.ecommerce.DTO.ResponseDto.ProductResponseDto;
import com.example.ecommerce.Exceptions.InvalidSellerException;
import com.example.ecommerce.Transformer.ProductTransformer;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.Seller;
import com.example.ecommerce.enums.ProductCategory;
import com.example.ecommerce.enums.ProductStatus;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.repository.SellerRepository;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    SellerRepository sellerRepository;

    @Override
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) throws InvalidSellerException {
        Seller seller;
        try{
            seller = sellerRepository.findById(productRequestDto.getSellerId()).get();
        }
        catch (Exception e){
            throw new InvalidSellerException("Seller does not exist");
        }

        Product product = ProductTransformer.ProductRequestDtoToProduct(productRequestDto);
        product.setSeller(seller);

        //adding product to product list of seller
        seller.getProducts().add(product);
        sellerRepository.save(seller);

        //prepare response Dto
        return ProductTransformer.ProductToProductResponseDto(product);

    }

    @Override
    public List<ProductResponseDto> getProductsByCategory(ProductCategory category) {
        List<Product> products = productRepository.findByProductCategory(category);

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product : products){
            productResponseDtos.add(ProductTransformer.ProductToProductResponseDto(product));
        }
        return productResponseDtos;
    }

    @Override
    public List<ProductResponseDto> getProductById(int sellerId) {
        Seller seller = sellerRepository.findById(sellerId).get();
        List<Product> products = seller.getProducts();

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product : products){
            productResponseDtos.add(ProductTransformer.ProductToProductResponseDto(product));
        }
        return productResponseDtos;
    }

    @Override
    public void deleteProduct(int sellerId, int productId) {
        Seller seller = sellerRepository.findById(sellerId).get();
        List<Product> products = seller.getProducts();
        for(Product product : products){
            if(product.getId() == productId){
                seller.getProducts().remove(product);
            }
        }
        productRepository.deleteById(productId);
    }

    @Override
    public List<ProductResponseDto> getCheapest(int n) {
        List<Product> products = productRepository.findByOrderByPriceAsc();

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(int i=0;i<n;i++){
            productResponseDtos.add(ProductTransformer.ProductToProductResponseDto(products.get(i)));
        }
        return productResponseDtos;
    }

    @Override
    public List<ProductResponseDto> getCostliest(int n) {
        List<Product> products = productRepository.findByOrderByPriceDesc();

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(int i=0;i<n;i++){
            productResponseDtos.add(ProductTransformer.ProductToProductResponseDto(products.get(i)));
        }
        return productResponseDtos;
    }

    @Override
    public List<ProductResponseDto> getOutOfStock() {
        List<Product> products = productRepository.findAll();

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product : products){
            if(product.getProductStatus().equals(ProductStatus.OUT_OF_STOCK)){
                productResponseDtos.add(ProductTransformer.ProductToProductResponseDto(product));
            }
        }
        return productResponseDtos;
    }

    @Override
    public List<ProductResponseDto> getAvailable() {
        List<Product> products = productRepository.findAll();

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product : products){
            if(product.getProductStatus().equals(ProductStatus.AVAILABLE)){
                productResponseDtos.add(ProductTransformer.ProductToProductResponseDto(product));
            }
        }
        return productResponseDtos;
    }

    @Override
    public List<ProductResponseDto> getByQuantity(int n) {
        List<Product> products = productRepository.findAll();

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product : products){
            if(product.getQuantity() < n){
                productResponseDtos.add(ProductTransformer.ProductToProductResponseDto(product));
            }
        }
        return productResponseDtos;
    }

    @Override
    public ProductResponseDto cheapestInCategory() {
        List<Product> products = productRepository.findByOrderByPriceAsc();
        return ProductTransformer.ProductToProductResponseDto(products.get(0));
    }

    @Override
    public ProductResponseDto costliestInCategory() {
        List<Product> products = productRepository.findByOrderByPriceDesc();
        return ProductTransformer.ProductToProductResponseDto(products.get(0));
    }

    @Override
    public List<ProductResponseDto> getByPriceAndCategory(int price, String category) {
        List<Product> products = productRepository.getByPriceAndCategory(price, category);

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for(Product product : products){
            productResponseDtos.add(ProductTransformer.ProductToProductResponseDto(product));
        }
        return productResponseDtos;
    }

    @Override
    public void updateById(int productId, String productName) {
        Product product = productRepository.findById(productId).get();
        product.setName(productName);
        productRepository.save(product);
    }
}
