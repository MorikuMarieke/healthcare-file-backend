package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.care_plan.CarePlanResponse;
import com.moriku.healthcare_file_backend.exception.ResourceNotFoundException;
import com.moriku.healthcare_file_backend.mapper.CarePlanMapper;
import com.moriku.healthcare_file_backend.model.CarePlan;
import com.moriku.healthcare_file_backend.repository.CarePlanRepository;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CarePlanService {

    private final CarePlanRepository carePlanRepository;

    public CarePlanService(CarePlanRepository carePlanRepository) {
        this.carePlanRepository = carePlanRepository;
    }

    @Transactional(readOnly = true)
    public CarePlanResponse getByClientProfileId(Long clientProfileId) {
        CarePlan plan = carePlanRepository.findByClientProfileId(clientProfileId)
            .orElseThrow(() -> new ResourceNotFoundException("CarePlan not found for clientProfileId: " + clientProfileId));

        return CarePlanMapper.toResponse(plan);
    }

    @Transactional
    public CarePlanResponse updateCarePlanNotesByClientProfileId(Long clientProfileId, String notes) {
        CarePlan carePlan = carePlanRepository.findByClientProfileId(clientProfileId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CarePlan not found"));

        carePlan.setNotes(notes);
        return CarePlanMapper.toResponse(carePlan);
    }


}