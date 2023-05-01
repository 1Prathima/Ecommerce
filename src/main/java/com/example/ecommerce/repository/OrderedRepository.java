package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Ordered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedRepository extends JpaRepository<Ordered, Integer> {
    @Query(value = "select * from ordered o where o.customer_id = :customerId order by o.order_date desc limit 2", nativeQuery = true)
    List<Ordered> getRecentOrders(int customerId);

    @Query(value = "SELECT * FROM ordered ORDER BY total_value DESC LIMIT 1", nativeQuery = true)
    Ordered getHighestBillOrder();
}
