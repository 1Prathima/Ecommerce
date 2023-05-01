package com.example.ecommerce.repository;

import com.example.ecommerce.Transformer.CustomerTransformer;
import com.example.ecommerce.entity.Customer;
import com.example.ecommerce.entity.Ordered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByMobNo(String mobNo);
    Customer findByEmailId(String email);

    @Query(value = "select * from customer c where c.age > :age", nativeQuery = true)
    List<Customer> getByAge(int age);


    void deleteByEmailId(String email);
    void deleteByMobNo(String mobNo);


}
