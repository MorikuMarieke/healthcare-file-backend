package com.moriku.healthcare_file_backend.controller;

import com.moriku.healthcare_file_backend.dto.care_plan.CarePlanCreateRequest;
import com.moriku.healthcare_file_backend.dto.care_plan.CarePlanResponse;
import com.moriku.healthcare_file_backend.dto.care_plan.CarePlanUpdateRequest;
import com.moriku.healthcare_file_backend.service.CarePlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/care-plans")
public class CarePlanController {

    private final CarePlanService carePlanService;

    public CarePlanController(CarePlanService carePlanService) {
        this.carePlanService = carePlanService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarePlanResponse create(@Valid @RequestBody CarePlanCreateRequest req) {
        return carePlanService.create(req);
    }

    @GetMapping("/client/{clientProfileId}")
    public CarePlanResponse getByClient(@PathVariable Long clientProfileId) {
        return carePlanService.getByClientProfileId(clientProfileId);
    }

    @PatchMapping("/{carePlanId}")
    public CarePlanResponse updateNotes(
        @PathVariable Long carePlanId,
        @Valid @RequestBody CarePlanUpdateRequest req
    ) {
        return carePlanService.updateCarePlanNotes(carePlanId, req.getNotes());
    }
}