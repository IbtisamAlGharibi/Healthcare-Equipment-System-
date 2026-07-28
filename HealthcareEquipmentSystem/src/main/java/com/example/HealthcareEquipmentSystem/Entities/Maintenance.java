package com.example.HealthcareEquipmentSystem.Entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

@Entity
@Data
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private LocalDate maintenanceDate;
    private String description;
    private String status;
    private Integer technicianId;
    private Integer equipmentId;

    @ManyToOne
    @JoinColumn(name = "tech_id")
    private MaintenanceTechnician technician;

    /*@ManyToOne
    @JoinColumn(name = "equip_id")
    private Equipment equipment;*/
}
