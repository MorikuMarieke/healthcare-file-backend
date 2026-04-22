package com.moriku.healthcare_file_backend.mapper;

import com.moriku.healthcare_file_backend.dto.client_profile.ContactDetailsResponse;
import com.moriku.healthcare_file_backend.model.ContactDetails;

public class ContactDetailsMapper {

    private ContactDetailsMapper() {
    }

    public static ContactDetailsResponse toResponse(ContactDetails contactDetails) {
        return new ContactDetailsResponse(
            contactDetails.getClientProfile().getId(),
            contactDetails.getEmail(),
            contactDetails.getPrimaryPhoneNumber(),
            contactDetails.getSecondaryPhoneNumber(),
            contactDetails.getPrimaryPhysician(),
            contactDetails.getFirstEmergencyContactName(),
            contactDetails.getFirstEmergencyContactEmail(),
            contactDetails.getFirstEmergencyContactPhoneNumber(),
            contactDetails.getAddress()
        );
    }
}