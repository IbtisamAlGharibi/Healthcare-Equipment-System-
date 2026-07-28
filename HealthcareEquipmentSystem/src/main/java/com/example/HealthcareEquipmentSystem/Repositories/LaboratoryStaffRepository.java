package com.example.HealthcareEquipmentSystem.Repositories;

import com.example.HealthcareEquipmentSystem.Entities.LaboratoryStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LaboratoryStaffRepository extends JpaRepository<LaboratoryStaff, Integer> {
    @Query("select ls from LaboratoryStaff ls where ls.isActive=true")
    List<LaboratoryStaff> getAllStaff();

    @Query("select ls from LaboratoryStaff ls where ls.isActive=true and ls.id =:id")
    LaboratoryStaff findByLaboratoryStaffId(@Param("id") Integer id);

    @Query("select ls from LaboratoryStaff ls where ls.isActive=true and ls.name =:name")
    LaboratoryStaff findByLaboratoryStaffName(@Param("name") String name);

    @Query("select ls from LaboratoryStaff ls where ls.isActive=true and ls.department =:department")
    List<LaboratoryStaff> findByDepartment(@Param("department") String department);

    //new
    @Query("""
        SELECT e.staff.name, COUNT(r)
        FROM Reservation r
        JOIN r.equipment e
        GROUP BY e.staff.name
        ORDER BY COUNT(r) DESC
        """)
    List<Object[]> staffWithMostReservations();
}
