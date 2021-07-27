package com.example.phone.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table("phone_numbers")
public class Phone {
    @Id
    @Column("phone_id")
    private Long phoneId;
    @Column("phone_number")
    private String number;
    @Column("type")
    private PhoneType type;
    @Column("activated")
    private boolean activated;
    @Column("customer_id")
    private Long customerId;
}
