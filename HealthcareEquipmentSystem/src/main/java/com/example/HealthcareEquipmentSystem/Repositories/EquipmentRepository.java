package com.example.HealthcareEquipmentSystem.Repositories;

import com.example.HealthcareEquipmentSystem.Entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer>  {
    @Query("select e from Equipment e where e.status = 'Available' and e.isActive =:true")
    List<Equipment> getAllEquipment();

    @Query("select e from Equipment e where e.id = :id and e.isActive =:true")
    Equipment findByEquipmentId(@Param("id") Integer id);

    @Query("select e from Equipment e where e.name = :name and e.isActive =:true")
    List<Equipment> findByEquipmentName(@Param("name") String name);

    @Query("select e from Equipment e where e.status = :status and e.isActive =:true")
    List<Equipment> findByStatus(@Param("status") String status);

    @Query("select e from Equipment e where e.status = 'Available' and e.isActive =:true")
    List<Equipment> getAvailableEquipment();
}


