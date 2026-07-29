package com.example.HealthcareEquipmentSystem.Repositories;

import com.example.HealthcareEquipmentSystem.Entities.Equipment;
import com.example.HealthcareEquipmentSystem.Entities.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LaboratoryRepository extends JpaRepository<Laboratory, Integer> {

    @Query("select l from Laboratory l")
    List<Laboratory> getAllLaboratories();

    @Query("select l from Laboratory l where l.id =:id")
    Laboratory findByLaboratoryId(@Param("id") Integer id);

    @Query("select l from Laboratory l where l.name =:name")
    Laboratory findByLaboratoryName(@Param("name") String name);

    @Query("select l from Laboratory l where l.location =:location and l.isActive =true")
    List<Laboratory> findByLocation(@Param("location") String location);

}


