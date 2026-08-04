package com.example.HealthcareEquipmentSystem.DTO.Requests;

import com.example.HealthcareEquipmentSystem.Entities.Equipment;
import com.example.HealthcareEquipmentSystem.Entities.Reservation;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDTO {
    @NotNull(message = "Reservation date is required.")
    @FutureOrPresent(message = "Reservation date cannot be in the past.")
    private LocalDate reservationDate;
    @NotNull(message = "Start time is required.")
    private LocalTime startTime;
    @NotNull(message = "End time is required.")
    private LocalTime endTime;
    @NotBlank(message = "Purpose is required.")
    private String purpose;
    @NotNull(message = "Equipment is required.")
    private Integer equipmentId;

    public Reservation toEntity() {
        Reservation reservation = new Reservation();
        reservation.setReservationDate(reservationDate);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setPurpose(purpose);
        Equipment equipment = new Equipment();
        equipment.setId(equipmentId);
        reservation.setEquipment(equipment);
        return reservation;
    }
}
