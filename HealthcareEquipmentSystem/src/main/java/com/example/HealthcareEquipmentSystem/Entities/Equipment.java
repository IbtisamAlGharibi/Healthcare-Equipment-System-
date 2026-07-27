package com.example.HealthcareEquipmentSystem.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String serialNumber;

    private String status;

    private LocalDate purchaseDate;

    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "laboratory_id")
    private Laboratory laboratory;


    @OneToMany(mappedBy = "equipment")
    private List<Reservation> reservations;
    private String description;


/*
    @OneToMany(mappedBy = "equipment")
    private List<Maintenance> maintenances;*/
}





