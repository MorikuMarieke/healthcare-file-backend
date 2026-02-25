package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.care_plan.CarePlanResponse;
import com.moriku.healthcare_file_backend.dto.care_plan.CarePlanUpdateRequest;
import com.moriku.healthcare_file_backend.service.CarePlanService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client-profiles/{clientProfileId}/care-plan")
public class CarePlanController {

    private final CarePlanService carePlanService;

    public CarePlanController(CarePlanService carePlanService) {
        this.carePlanService = carePlanService;
    }

    @GetMapping
    public CarePlanResponse get(@PathVariable Long clientProfileId) {
        return carePlanService.getByClientProfileId(clientProfileId);
    }

    @PatchMapping
    public CarePlanResponse updateNotes(
        @PathVariable Long clientProfileId,
        @Valid @RequestBody CarePlanUpdateRequest req
    ) {
        return carePlanService.updateCarePlanNotesByClientProfileId(clientProfileId, req.getNotes());
    }
}