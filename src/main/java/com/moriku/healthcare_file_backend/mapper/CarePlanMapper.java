package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.care_plan.CarePlanResponse;
import com.moriku.healthcare_file_backend.model.CarePlan;

public class CarePlanMapper {

    private CarePlanMapper() {
    }

    public static CarePlanResponse toResponse(CarePlan carePlan) {
        return new CarePlanResponse(
            carePlan.getId(),
            carePlan.getClientProfile().getId(),
            carePlan.getNotes(),
            carePlan.getMedicalHistory()
        );
    }
}