package com.example.HealthcareEquipmentSystem.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LaboratoryStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String department;
    private Boolean isActive;

    @OneToMany(mappedBy = "laboratoryStaff", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();
}
