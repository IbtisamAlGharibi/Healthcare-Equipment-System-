package com.example.HealthcareEquipmentSystem.Repositories;

import com.example.HealthcareEquipmentSystem.Entities.MaintenanceTechnician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TechnicianRepository extends JpaRepository<MaintenanceTechnician, Integer> {
    @Query("SELECT T FROM MaintenanceTechnician T WHERE T.isActive=true")
    List<MaintenanceTechnician> findAllByIsActive();
    @Query("SELECT T FROM MaintenanceTechnician T WHERE T.specialization=:specialization")
    List<MaintenanceTechnician> findBySpecialization(@Param("specialization") String specialization);
    @Query("SELECT T FROM MaintenanceTechnician T WHERE T.specialization=:specialization AND T.isActive=true")
    List<MaintenanceTechnician> findBySpecializationAndIsActive(@Param("specialization") String specialization);
    @Query("select T from MaintenanceTechnician T where T.isActive=true and T.id=:id ")
    MaintenanceTechnician findByMaintenanceTechnicianId(@Param("id") Integer id);
    //new
    @Query("""
        SELECT M.technicianId, COUNT(M)
        FROM Maintenance M
        WHERE M.status='COMPLETE'
        GROUP BY M.technicianId
        ORDER BY COUNT(M) DESC
       """)
    List<Object[]> technicianWithMostCompletedMaintenance();
}
