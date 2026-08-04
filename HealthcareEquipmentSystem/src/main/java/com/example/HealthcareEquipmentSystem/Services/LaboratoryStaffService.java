package com.example.HealthcareEquipmentSystem.Services;

import com.example.HealthcareEquipmentSystem.DTO.Requests.LaboratoryStaffRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.LaboratoryStaffResponseDTO;
import com.example.HealthcareEquipmentSystem.Entities.LaboratoryStaff;
import com.example.HealthcareEquipmentSystem.Exceptions.ResourceNotFoundException;
import com.example.HealthcareEquipmentSystem.Repositories.LaboratoryStaffRepository;
import com.example.HealthcareEquipmentSystem.Repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LaboratoryStaffService {
    LaboratoryStaffRepository laboratoryStaffRepository;
    ReservationRepository reservationRepository;

    @Autowired
    public LaboratoryStaffService(LaboratoryStaffRepository laboratoryStaffRepository, ReservationRepository reservationRepository) {
        this.laboratoryStaffRepository = laboratoryStaffRepository;
        this.reservationRepository = reservationRepository;
    }

    public LaboratoryStaffResponseDTO addLaboratoryStaff(LaboratoryStaffRequestDTO laboratoryStaffRequestDTO) {
        LaboratoryStaff newLaboratoryStaff = laboratoryStaffRequestDTO.toEntity();
        newLaboratoryStaff.setIsActive(true);
        LaboratoryStaff savedLaboratoryStaff = laboratoryStaffRepository.save(newLaboratoryStaff);
        return LaboratoryStaffResponseDTO.fromEntity(savedLaboratoryStaff);
    }

    public LaboratoryStaffResponseDTO updateLaboratoryStaff(Integer id, LaboratoryStaffRequestDTO laboratoryStaffRequestDTO) {
        if(!laboratoryStaffRepository.existsById(id)){
            throw new ResourceNotFoundException("laboratory Staff not found with id: " + id);
        }
        LaboratoryStaff updatedLaboratoryStaff = laboratoryStaffRepository.findByLaboratoryStaffId(id);
        updatedLaboratoryStaff.setName(laboratoryStaffRequestDTO.getName());
        updatedLaboratoryStaff.setPhone(laboratoryStaffRequestDTO.getPhone());
        updatedLaboratoryStaff.setEmail(laboratoryStaffRequestDTO.getEmail());
        updatedLaboratoryStaff.setDepartment(laboratoryStaffRequestDTO.getDepartment());
        LaboratoryStaff savedLaboratoryStaff = laboratoryStaffRepository.save(updatedLaboratoryStaff);
        return LaboratoryStaffResponseDTO.fromEntity(savedLaboratoryStaff);
    }

    public void deleteLaboratoryStaff(Integer id){

        if(!laboratoryStaffRepository.existsById(id)){
            throw new ResourceNotFoundException("laboratory Staff not found with id: " + id);
        }
        LaboratoryStaff laboratoryStaff = laboratoryStaffRepository.findByLaboratoryStaffId(id);
        laboratoryStaff.setIsActive(false);
        laboratoryStaffRepository.save(laboratoryStaff);
    }
    public LaboratoryStaffResponseDTO getLaboratoryStaffById(Integer id) {
        LaboratoryStaff laboratoryStaff = laboratoryStaffRepository.findByLaboratoryStaffId(id);
        return LaboratoryStaffResponseDTO.fromEntity(laboratoryStaff);
    }
    public List<LaboratoryStaffResponseDTO> getAllLaboratoryStaff() {
        List<LaboratoryStaff> laboratoryStaffList = laboratoryStaffRepository.findAll();
        List<LaboratoryStaffResponseDTO> responseDTOList = new ArrayList<>();
        for (LaboratoryStaff laboratoryStaff : laboratoryStaffList) {
            responseDTOList.add(LaboratoryStaffResponseDTO.fromEntity(laboratoryStaff));
        }
        return responseDTOList;
    }
    public LaboratoryStaffResponseDTO getLaboratoryStaffByName(String name) {
        LaboratoryStaff laboratoryStaff = laboratoryStaffRepository.findByLaboratoryStaffName(name);
        return LaboratoryStaffResponseDTO.fromEntity(laboratoryStaff);
    }
    public List<LaboratoryStaffResponseDTO> getLaboratoryStaffByDepartment(String department) {
        List<LaboratoryStaff> laboratoryStaffList = laboratoryStaffRepository.findByDepartment(department);
        List<LaboratoryStaffResponseDTO> responseDTOList = new ArrayList<>();
        for (LaboratoryStaff laboratoryStaff : laboratoryStaffList) {
            responseDTOList.add(LaboratoryStaffResponseDTO.fromEntity(laboratoryStaff));
        }
        return responseDTOList;
    }
}
