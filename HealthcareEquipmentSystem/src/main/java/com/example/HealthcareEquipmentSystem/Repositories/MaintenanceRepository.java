package com.example.HealthcareEquipmentSystem.Repositories;

import com.example.HealthcareEquipmentSystem.Entities.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {
    @Query("SELECT M FROM Maintenance M WHERE M.status=:status")
    List<Maintenance> findByStatus(@Param("status") String status);
    @Query("select M from Maintenance M where M.id=:id ")
    Maintenance findByMaintenanceId(@Param("id") Integer id);
    @Query("SELECT M FROM Maintenance M WHERE M.technicianId=:id")
    List<Maintenance> findByTechnicianId(@Param("id") Integer id);
    @Query("SELECT M FROM Maintenance M WHERE M.equipmentId=:id")
    List<Maintenance> findByEquipmentId(@Param("id") Integer id);
    @Query("SELECT M FROM Maintenance M WHERE M.maintenanceDate BETWEEN :startDate AND :endDate")
    List<Maintenance> findMaintenanceBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
