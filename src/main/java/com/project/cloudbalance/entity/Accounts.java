package com.project.cloudbalance.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Accounts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false ,unique=true)
    private String arn;
    @Column(nullable = false, unique = true)
    private Long accountId;
    @Column(nullable = false, unique = true)
    private String accountName;

}
