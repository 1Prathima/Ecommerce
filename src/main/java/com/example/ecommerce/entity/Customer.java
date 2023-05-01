package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String name;
    int age;
    String mobNo;
    String emailId;
    String address;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    List<Card> cards = new ArrayList<>(); //card not taken while registering

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    Cart cart;  //initialized when customer is created

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<Ordered> orders = new ArrayList<>();

}
