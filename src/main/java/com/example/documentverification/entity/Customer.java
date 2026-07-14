package com.example.documentverification.entity;

import com.example.documentverification.common.entity.Auditable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 255)
    private String email;
    
    @Column(nullable = false, length = 255)
    private String firstName;
    
    @Column(nullable = false, length = 255)
    private String lastName;
    
    @Column(length = 20)
    private String phoneNumber;
    
    @Column(length = 500)
    private String address;
}