package com.example.HealthcareEquipmentSystem.Controllers;

import com.example.HealthcareEquipmentSystem.DTO.Requests.ReservationRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.ReservationResponseDTO;
import com.example.HealthcareEquipmentSystem.Services.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    ReservationService reservationService;
    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/{staffId}")
    public ResponseEntity<ReservationResponseDTO> createReservation( @Valid @RequestBody ReservationRequestDTO reservationRequestDTO,
                                                                     @PathVariable Integer staffId) {
        return ResponseEntity.ok(reservationService.createReservation(reservationRequestDTO, staffId));
    }

    // Approve a reservation
    @PutMapping("/{reservationId}/approve")
    public ResponseEntity<ReservationResponseDTO> approveReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(reservationService.approveReservation(reservationId));
    }

    // Cancel a reservation
    @PutMapping("/{reservationId}/cancel")
    public ResponseEntity<ReservationResponseDTO> cancelReservation(@PathVariable Integer reservationId){
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    // Get a reservation by its ID
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(reservationService.getReservation(reservationId));
    }

    // Get all reservations
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    // Get all reservations made by a specific laboratory staff
    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByStaff(@PathVariable Integer staffId) {
        return ResponseEntity.ok(reservationService.getReservationsByStaff(staffId));
    }

    // Soft delete a reservation
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<String> deleteReservation(@PathVariable Integer reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.ok("Reservation deleted successfully.");
    }

}
