package com.example.HealthcareEquipmentSystem.Repositories;

import com.example.HealthcareEquipmentSystem.Entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("select r from Reservation r where r.id=:id ")
    Reservation findReservationById(@Param("id") Integer id);
    @Query("select r from Reservation r where r.laboratoryStaff.id=:staffId ")
    Reservation findByLaboratoryStaffId(@Param("staffId") Integer staffId);
    @Query("select r from Reservation r where r.laboratoryStaff.id=:staffId ")
    List<Reservation> findReservationsByLaboratoryStaffId(@Param("staffId") Integer staffId);
    @Query("select r from Reservation r")
    List<Reservation> getAllReservation();
    @Query("select r from Reservation r where r.equipment.id=:equipmentId ")
    List<Reservation> findByEquipmentId(@Param("equipmentId") Integer equipmentId);
    @Query("select r from Reservation r where r.status=:status ")
    List<Reservation> findByStatus(@Param("status") String  status);
    @Query("select r from Reservation r where r.reservationDate=:reservationDate ")
    List<Reservation> findByReservationDate(@Param("reservationDate") LocalDate reservationDate);
    @Query("select r from Reservation r where r.status='Approved'")
    List<Reservation> getApprovedReservations();
    @Query("SELECT r FROM Reservation r WHERE r.laboratoryStaff.id =:staffId AND r.status =:status")
    List<Reservation> findByLaboratoryStaffIdAndStatus(@Param("staffId") Integer staffId, @Param("status") String status);
    //newOne
    @Query("""
        SELECT r.laboratoryStaff.name, COUNT(r)
        FROM Reservation r
        JOIN r.equipment e
        GROUP BY r.laboratoryStaff.name
        ORDER BY COUNT(r) DESC
        """)
    List<Reservation> staffWithMostReservations();
}
