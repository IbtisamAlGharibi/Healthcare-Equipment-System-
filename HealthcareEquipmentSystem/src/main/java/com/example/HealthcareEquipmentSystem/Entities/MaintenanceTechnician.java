package com.example.HealthcareEquipmentSystem.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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
