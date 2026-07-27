package com.example.HealthcareEquipmentSystem.Services;

import com.example.HealthcareEquipmentSystem.DTO.Requests.ReservationRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.ReservationResponseDTO;
import com.example.HealthcareEquipmentSystem.Entities.LaboratoryStaff;
import com.example.HealthcareEquipmentSystem.Entities.Reservation;
import com.example.HealthcareEquipmentSystem.Repositories.LaboratoryStaffRepository;
import com.example.HealthcareEquipmentSystem.Repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    ReservationRepository reservationRepository;
    LaboratoryStaffRepository laboratoryStaffRepository;
    @Autowired
    public ReservationService(ReservationRepository reservationRepository, LaboratoryStaffRepository   laboratoryStaffRepository) {
        this.reservationRepository = reservationRepository;
        this.laboratoryStaffRepository = laboratoryStaffRepository;
    }

    public ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequestDTO, Integer staffId) {
        LaboratoryStaff laboratoryStaff = laboratoryStaffRepository.findByLaboratoryStaffId(staffId);
        Reservation newReservation = reservationRequestDTO.toEntity();
        newReservation.setLaboratoryStaff(laboratoryStaff);
        newReservation.setStatus("Pending");
        Reservation savedReservation = reservationRepository.save(newReservation);
        return ReservationResponseDTO.fromEntity(savedReservation);
    }
    public ReservationResponseDTO approveReservation(Integer staffId) {
        Reservation reservation = reservationRepository.findByLaboratoryStaffId(staffId);
        reservation.setStatus("Approved");
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponseDTO.fromEntity(savedReservation);
    }
    public ReservationResponseDTO cancelReservation(Integer staffId) {
        Reservation reservation = reservationRepository.findByLaboratoryStaffId(staffId);
        reservation.setStatus("Cancelled");
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponseDTO.fromEntity(savedReservation);
    }
    public ReservationResponseDTO getReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findReservationById(reservationId);
        return ReservationResponseDTO.fromEntity(reservation);
    }
    public List<ReservationResponseDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.getAllReservation();
        List<ReservationResponseDTO> response = new ArrayList<>();
        for (Reservation reservation : reservations) {
            response.add(ReservationResponseDTO.fromEntity(reservation));
        }
        return response;
    }

    public List<ReservationResponseDTO> getReservationsByStaff(Integer staffId) {
        List<Reservation> reservations = reservationRepository.findReservationsByLaboratoryStaffId(staffId);
        List<ReservationResponseDTO> response = new ArrayList<>();
        for (Reservation reservation : reservations) {
            response.add(ReservationResponseDTO.fromEntity(reservation));
        }
        return response;
    }

    public void deleteReservation(Integer id){
        Reservation deletedReservation = reservationRepository.findReservationById(id);
        deletedReservation.setIsActive(false);
        reservationRepository.save(deletedReservation);
    }
}
