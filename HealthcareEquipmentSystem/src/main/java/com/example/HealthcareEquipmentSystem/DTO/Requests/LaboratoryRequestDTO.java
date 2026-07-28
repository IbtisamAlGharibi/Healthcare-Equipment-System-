package com.example.HealthcareEquipmentSystem.DTO.Requests;
import com.example.HealthcareEquipmentSystem.Entities.Laboratory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratoryRequestDTO {

    @NotBlank(message = "Laboratory name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

   @NotBlank(message = "Description is required")
    private String description;

   @NotNull(message = "Active status is required")
   private Boolean isActive;

  public static Laboratory toEntity(LaboratoryRequestDTO laboratoryRequestDTO) {

     Laboratory laboratory = new Laboratory();
     laboratory.setName(laboratoryRequestDTO.getName());
     laboratory.setLocation(laboratoryRequestDTO.getLocation());
     laboratory.setDescription(laboratoryRequestDTO.getDescription());

      return laboratory;
 }
}
