package com.example.phone.dto;

import com.example.phone.model.PhoneType;
import lombok.Data;

@Data
public class PhoneDto {
    private Long phoneId;
    private String number;
    private boolean activated;
    private Long customerId;
    private PhoneType type;
}
