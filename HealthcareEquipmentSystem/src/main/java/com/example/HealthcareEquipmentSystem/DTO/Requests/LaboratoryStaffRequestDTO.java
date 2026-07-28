package com.example.HealthcareEquipmentSystem.DTO.Requests;

import com.example.HealthcareEquipmentSystem.Entities.LaboratoryStaff;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaboratoryStaffRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Phone number must contain 8-15 digits")
    private String phone;
    @NotBlank(message = "Department is required")
    private String department;

    public LaboratoryStaff toEntity() {
        LaboratoryStaff laboratoryStaff = new LaboratoryStaff();
        laboratoryStaff.setName(name);
        laboratoryStaff.setEmail(email);
        laboratoryStaff.setPhone(phone);
        laboratoryStaff.setDepartment(department);
        return laboratoryStaff;
    }
}
