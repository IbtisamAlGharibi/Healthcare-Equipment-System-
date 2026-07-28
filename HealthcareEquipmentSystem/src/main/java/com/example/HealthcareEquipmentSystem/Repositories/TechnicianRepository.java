package com.example.HealthcareEquipmentSystem.Repositories;

import com.example.HealthcareEquipmentSystem.Entities.MaintenanceTechnician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TechnicianRepository extends JpaRepository<MaintenanceTechnician, Integer> {
    @Query("SELECT T FROM MaintenanceTechnicia T WHERE T.isActive=true")
    List<MaintenanceTechnician> findAllByIsActive();
    @Query("SELECT T FROM MaintenanceTechnicia T WHERE T.specialization= :specialization")
    List<MaintenanceTechnician> findBySpecialization(@Param("specialization") String specialization);
    @Query("SELECT T FROM MaintenanceTechnicia T WHERE T.specialization= :specialization AND T.isActive=true")
    List<MaintenanceTechnician> findBySpecializationAndIsActive(@Param("specialization") String specialization, Boolean isActive);
}
