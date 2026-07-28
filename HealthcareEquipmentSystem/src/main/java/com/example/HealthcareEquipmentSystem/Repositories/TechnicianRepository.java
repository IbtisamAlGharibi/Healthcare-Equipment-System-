package com.example.HealthcareEquipmentSystem.Repositories;

import com.example.HealthcareEquipmentSystem.Entities.MaintenanceTechnician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TechnicianRepository extends JpaRepository<MaintenanceTechnician, Integer> {
    @Query("SELECT M FROM MaintenanceTechnicia M WHERE M.isActive=true")
    List<MaintenanceTechnician> findAllByIsActive();
    @Query("SELECT M FROM MaintenanceTechnicia M WHERE M.specialization= :specialization")
    List<MaintenanceTechnician> findBySpecialization(@Param("specialization") String specialization);
    @Query("SELECT M FROM MaintenanceTechnicia M WHERE M.specialization= :specialization AND M.isActive=true")
    List<MaintenanceTechnician> findBySpecializationAndIsActive(@Param("specialization") String specialization, Boolean isActive);
}
