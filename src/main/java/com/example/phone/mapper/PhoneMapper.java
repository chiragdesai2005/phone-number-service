package com.example.phone.mapper;

import com.example.phone.dto.PhoneDto;
import com.example.phone.model.Phone;
import org.mapstruct.Mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@Mapper(componentModel = "spring", injectionStrategy = CONSTRUCTOR)
public interface PhoneMapper {

    PhoneDto toDto(Phone phone);

    Phone fromDto(Phone phone);
}
