package com.example.HealthcareEquipmentSystem.DTO.Requests;

import com.example.HealthcareEquipmentSystem.Entities.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequestDTO {
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String purpose;

    public Reservation toEntity() {
        Reservation reservation = new Reservation();
        reservation.setReservationDate(reservationDate);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setPurpose(purpose);
        return reservation;
    }
}
