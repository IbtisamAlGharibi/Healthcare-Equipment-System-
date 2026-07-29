package com.example.HealthcareEquipmentSystem.DTO.Requests;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RegisterRequest {
    private String username;
    private String password;
    private String role; // ADMIN, LAB_STAFF, or TECHNICIAN
}
