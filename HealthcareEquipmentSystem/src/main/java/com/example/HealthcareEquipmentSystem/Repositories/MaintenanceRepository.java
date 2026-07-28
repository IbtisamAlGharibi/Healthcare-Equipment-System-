package com.example.HealthcareEquipmentSystem.Repositories;

import com.example.HealthcareEquipmentSystem.Entities.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {
    @Query("SELECT S FROM MaintenanceTechnicia M WHERE M.specialization= :specialization")
    List<Maintenance> findByStatus(String status);
    List<Maintenance> findByTechnicianId(Integer id);
    List<Maintenance> findByEquipmentId(Integer id);
    @Query("SELECT M FROM Maintenance M WHERE M.maintenanceDate BETWEEN :startDate AND :endDate")
    List<Maintenance> findMaintenanceBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
