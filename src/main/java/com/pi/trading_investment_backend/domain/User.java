package com.pi.trading_investment_backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name ;

    private Double balance = 100000.00;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Portfolio portfolio;


}
