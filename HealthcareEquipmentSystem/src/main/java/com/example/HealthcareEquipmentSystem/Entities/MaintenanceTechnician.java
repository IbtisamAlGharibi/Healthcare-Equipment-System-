package com.example.HealthcareEquipmentSystem.Entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Entity
@Data
public class MaintenanceTechnician {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String phone;
    private String specialization;
    private Boolean isActive;

    @OneToMany(mappedBy="maintenanceTechnician", cascade= CascadeType.ALL)
    private List<Maintenance> maintenances;
}
