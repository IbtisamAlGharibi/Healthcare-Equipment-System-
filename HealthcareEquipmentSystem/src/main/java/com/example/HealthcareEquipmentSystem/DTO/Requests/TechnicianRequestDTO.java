package com.example.HealthcareEquipmentSystem.DTO.Requests;

import com.example.HealthcareEquipmentSystem.Entities.MaintenanceTechnician;
import com.example.HealthcareEquipmentSystem.Entities.Role;
import com.example.HealthcareEquipmentSystem.Entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TechnicianRequestDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number format")
    private String phone;
    @NotBlank(message = "Specialization cannot be blank")
    private String specialization;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;

    public static MaintenanceTechnician toEntity(TechnicianRequestDTO dto){
        MaintenanceTechnician technician=new MaintenanceTechnician();
        technician.setName(dto.getName());
        technician.setPhone(dto.getPhone());
        technician.setSpecialization(dto.getSpecialization());
        // Raw password here — the service encodes it with PasswordEncoder before saving.
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(Role.TECHNICIAN);
        technician.setUser(user);
        return technician;
    }
}
