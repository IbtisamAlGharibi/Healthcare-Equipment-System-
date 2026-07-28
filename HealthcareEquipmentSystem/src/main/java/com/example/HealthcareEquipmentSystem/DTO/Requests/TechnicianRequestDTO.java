package com.example.HealthcareEquipmentSystem.DTO.Requests;

import com.example.HealthcareEquipmentSystem.Entities.MaintenanceTechnician;
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
    private Integer id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number format")
    private String phone;
    @NotBlank(message = "Specialization cannot be blank")
    private String specialization;
    /*@NotNull(message = "Active status must be specified")
    private Boolean isActive;*/

    public static MaintenanceTechnician toEntity(TechnicianRequestDTO entity){
        MaintenanceTechnician technician=new MaintenanceTechnician();
        technician.setId(entity.getId());
        technician.setName(entity.getName());
        technician.setPhone(entity.getPhone());
        technician.setSpecialization(entity.getSpecialization());
        //technician.setIsActive(entity.getIsActive());

        return technician;
    }
}
