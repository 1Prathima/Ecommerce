package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.Seller;
import com.example.ecommerce.enums.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByProductCategory(ProductCategory category);
    List<Product> findByOrderByPriceAsc();
    List<Product> findByOrderByPriceDesc();

    @Query(value = "select * from product p where p.price > :price and p.product_category = :category", nativeQuery = true)
    List<Product> getByPriceAndCategory(int price, String category);
}
