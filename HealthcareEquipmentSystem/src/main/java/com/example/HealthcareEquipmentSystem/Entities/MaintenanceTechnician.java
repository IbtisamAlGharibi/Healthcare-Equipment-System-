package com.example.HealthcareEquipmentSystem.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceTechnician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String phone;
    private String specialization;
    private Boolean isActive;

    @OneToMany(mappedBy="technician", cascade= CascadeType.ALL)
    private List<Maintenance> maintenances;

}
