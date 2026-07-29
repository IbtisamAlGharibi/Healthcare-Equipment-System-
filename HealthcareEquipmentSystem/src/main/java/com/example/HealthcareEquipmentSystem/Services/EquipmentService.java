package com.example.HealthcareEquipmentSystem.Services;

import com.example.HealthcareEquipmentSystem.DTO.Requests.EquipmentRequestDTO;
import com.example.HealthcareEquipmentSystem.DTO.Responses.EquipmentResponseDTO;
import com.example.HealthcareEquipmentSystem.Entities.Equipment;
import com.example.HealthcareEquipmentSystem.Entities.Laboratory;
import com.example.HealthcareEquipmentSystem.Exceptions.BadRequestException;
import com.example.HealthcareEquipmentSystem.Exceptions.ResourceNotFoundException;
import com.example.HealthcareEquipmentSystem.Repositories.EquipmentRepository;
import com.example.HealthcareEquipmentSystem.Repositories.LaboratoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class EquipmentService {
    EquipmentRepository equipmentRepository;
    LaboratoryRepository laboratoryRepository;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository, LaboratoryRepository laboratoryRepository) {
        this.equipmentRepository = equipmentRepository;
        this.laboratoryRepository = laboratoryRepository;
    }


    public EquipmentResponseDTO addEquipment(EquipmentRequestDTO equipmentRequestDTO) {

        Equipment newEquipment = EquipmentRequestDTO.toEntity(equipmentRequestDTO);

        newEquipment.setStatus("Available");
        newEquipment.setIsActive(true);

        if (equipmentRequestDTO.getName() != null) {

            Laboratory laboratory =
                    laboratoryRepository.findByLaboratoryId(
                            equipmentRequestDTO.getLaboratoryId()
                    );
            newEquipment.setLaboratory(laboratory);

        }

        Equipment savedEquipment = equipmentRepository.save(newEquipment);

        return EquipmentResponseDTO.fromEntity(savedEquipment);

    }

    public EquipmentResponseDTO updateEquipment(Integer id, EquipmentRequestDTO equipmentRequestDTO) {

        Equipment updatedEquipment =
                equipmentRepository.findByEquipmentId(id);


        if(updatedEquipment == null){

            throw new ResourceNotFoundException(
                    "Equipment not found with id: " + id
            );

        }
        updatedEquipment.setName(equipmentRequestDTO.getName());

        updatedEquipment.setPurchaseDate(
                equipmentRequestDTO.getPurchaseDate()
        );


        if (equipmentRequestDTO.getName() != null) {

            Laboratory laboratory =
                    laboratoryRepository.findByLaboratoryId(id);

            updatedEquipment.setLaboratory(laboratory);

        }
        Equipment savedEquipment =
                equipmentRepository.save(updatedEquipment);


        return EquipmentResponseDTO.fromEntity(savedEquipment);

    }


    public void deleteEquipment(Integer id) {

        Equipment deletedEquipment =
                equipmentRepository.findByEquipmentId(id);


        if(deletedEquipment == null){

            throw new ResourceNotFoundException(
                    "Equipment not found with id: " + id
            );

        }

        deletedEquipment.setIsActive(false);

        equipmentRepository.save(deletedEquipment);

    }


    public EquipmentResponseDTO changeStatus(Integer id, String newStatus) {

        if(!newStatus.equals("Available") &&
                !newStatus.equals("Reserved") &&
                !newStatus.equals("Maintenance")){


            throw new BadRequestException(
                    "Invalid equipment status"
            );

        }

        Equipment equipment =
                equipmentRepository.findByEquipmentId(id);

        if(equipment == null){

            throw new ResourceNotFoundException(
                    "Equipment not found with id: " + id
            );

        }

        equipment.setStatus(newStatus);


        Equipment savedEquipment =
                equipmentRepository.save(equipment);


        return EquipmentResponseDTO.fromEntity(savedEquipment);

    }

    public List<EquipmentResponseDTO> getAllEquipment() {

        List<Equipment> equipmentList =
                equipmentRepository.getAllEquipment();


        List<EquipmentResponseDTO> responseDTOList =
                new ArrayList<>();

        for (Equipment equipment : equipmentList) {

            responseDTOList.add(
                    EquipmentResponseDTO.fromEntity(equipment)
            );

        }
        return responseDTOList;

    }

    public List<EquipmentResponseDTO> getAvailableEquipment() {


        List<Equipment> equipmentList =
                equipmentRepository.getAvailableEquipment();


        List<EquipmentResponseDTO> responseDTOList =
                new ArrayList<>();

        for (Equipment equipment : equipmentList) {

            responseDTOList.add(
                    EquipmentResponseDTO.fromEntity(equipment)
            );

        }

        return responseDTOList;

    }

    public EquipmentResponseDTO getEquipmentById(Integer id) {

        Equipment equipment =
                equipmentRepository.findByEquipmentId(id);

        if(equipment == null){

            throw new ResourceNotFoundException(
                    "Equipment not found with id: " + id
            );

        }

        return EquipmentResponseDTO.fromEntity(equipment);

    }

}