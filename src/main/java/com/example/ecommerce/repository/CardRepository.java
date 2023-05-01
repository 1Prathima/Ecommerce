package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Card;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    @Query(value = "select * from card c where c.card_type = :cardType", nativeQuery = true)
    List<Card> getByCardType(String cardType);

    @Query(value = "select * from card c where c.card_type = :cardType and c.expiry_date > :expiryDate", nativeQuery = true)
    List<Card> getByExpiryDate(String cardType, Date expiryDate);

    Card findByCardNo(String cardNo);
}
