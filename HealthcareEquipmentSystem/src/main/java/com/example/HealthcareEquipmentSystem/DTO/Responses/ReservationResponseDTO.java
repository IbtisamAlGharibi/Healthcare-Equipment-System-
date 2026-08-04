package com.example.HealthcareEquipmentSystem.DTO.Responses;

import com.example.HealthcareEquipmentSystem.Entities.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDTO {
    private Integer id;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String purpose;
    private String status;
    private Boolean isActive;
    private Integer staffId;
    private String staffName;
    private Integer equipmentId;
    private String equipmentName;


    public static ReservationResponseDTO fromEntity(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setReservationDate(reservation.getReservationDate());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setPurpose(reservation.getPurpose());
        dto.setStatus(reservation.getStatus());
        dto.setIsActive(reservation.getIsActive());
        if (reservation.getLaboratoryStaff() != null) {
            dto.setStaffId(reservation.getLaboratoryStaff().getId());
            dto.setStaffName(reservation.getLaboratoryStaff().getName());
        }
        if (reservation.getEquipment() != null) {
            dto.setEquipmentId(reservation.getEquipment().getId());
            dto.setEquipmentName(reservation.getEquipment().getName());
        }
        return dto;
    }
}
