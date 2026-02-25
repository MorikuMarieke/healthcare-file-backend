package com.moriku.healthcare_file_backend.service;

import com.moriku.healthcare_file_backend.dto.care_plan.CarePlanResponse;
import com.moriku.healthcare_file_backend.exception.ResourceNotFoundException;
import com.moriku.healthcare_file_backend.mapper.CarePlanMapper;
import com.moriku.healthcare_file_backend.model.CarePlan;
import com.moriku.healthcare_file_backend.repository.CarePlanRepository;
import com.moriku.healthcare_file_backend.repository.ClientProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarePlanService {

    private final CarePlanRepository carePlanRepository;
    private final ClientProfileRepository clientProfileRepository;

    public CarePlanService(CarePlanRepository carePlanRepository, ClientProfileRepository clientProfileRepository) {
        this.carePlanRepository = carePlanRepository;
        this.clientProfileRepository = clientProfileRepository;
    }

    @Transactional(readOnly = true)
    public CarePlanResponse getByClientProfileId(Long clientProfileId) {
        CarePlan plan = carePlanRepository.findByClientProfileId(clientProfileId)
            .orElseThrow(() -> new ResourceNotFoundException("CarePlan not found for clientProfileId: " + clientProfileId));

        return CarePlanMapper.toResponse(plan);
    }

    @Transactional
    public CarePlanResponse updateCarePlanNotes(Long carePlanId, String notes) {
        CarePlan plan = carePlanRepository.findById(carePlanId)
            .orElseThrow(() -> new ResourceNotFoundException("CarePlan not found: " + carePlanId));

        plan.setNotes(notes.trim());
        return CarePlanMapper.toResponse(plan);
    }


}