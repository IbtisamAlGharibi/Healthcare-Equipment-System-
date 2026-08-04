package com.example.HealthcareEquipmentSystem.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate maintenanceDate;
    private String description;
    private String status;
    private Integer technicianId;
    private Integer equipmentId;
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "tech_id")
    private MaintenanceTechnician technician;

    @ManyToOne
    @JoinColumn(name = "equip_id")
    private Equipment equipment;
}
