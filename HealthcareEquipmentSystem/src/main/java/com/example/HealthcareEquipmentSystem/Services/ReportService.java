package com.example.HealthcareEquipmentSystem.Services;

import com.example.HealthcareEquipmentSystem.Controllers.LaboratoryStaffController;
import com.example.HealthcareEquipmentSystem.DTO.Responses.EquipmentResponseDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.LaboratoryStaffResponseDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.MaintenanceResponseDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.TechnicianResponseDTO;
import com.example.HealthcareEquipmentSystem.Entities.*;
import com.example.HealthcareEquipmentSystem.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    private EquipmentRepository equipmentRepository;
    private LaboratoryRepository laboratoryRepository;
    private ReservationRepository reservationRepository;
    private LaboratoryStaffRepository staffRepository;
    private MaintenanceRepository maintenanceRepository;
    private TechnicianRepository technicianRepository;

    @Autowired
    public ReportService(EquipmentRepository equipmentRepository, LaboratoryRepository laboratoryRepository, ReservationRepository reservationRepository, LaboratoryStaffRepository staffRepository, MaintenanceRepository maintenanceRepository, TechnicianRepository technicianRepository) {
        this.equipmentRepository = equipmentRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.reservationRepository = reservationRepository;
        this.staffRepository = staffRepository;
        this.maintenanceRepository = maintenanceRepository;
        this.technicianRepository = technicianRepository;
    }

    public Integer availableEquipment() {
        return equipmentRepository.countByStatus("AVAILABLE");
    }

    public Integer reservedEquipment() {
        return equipmentRepository.countByStatus("RESERVED");
    }

    public Integer maintenanceEquipment() {
        return equipmentRepository.countByStatus("UNDER_MAINTENANCE");
    }

    public List<Object[]> equipmentPerLaboratory() {
        return laboratoryRepository.equipmentPerLaboratory();
    }

    public List<Object[]> reservationsPerLaboratory() {
        return laboratoryRepository.reservationsPerLaboratory();
    }

    public List<Object[]> staffWithMostReservations() {
        return staffRepository.staffWithMostReservations();
    }

    public List<Maintenance> equipmentRepairedThisMonth(LocalDate startDate,
                                                        LocalDate endDate) {

        return maintenanceRepository.findMaintenanceBetweenDates(startDate, endDate);
    }

    public List<Object[]> technicianWithMostCompletedMaintenance() {
        return technicianRepository.technicianWithMostCompletedMaintenance();
    }
}
