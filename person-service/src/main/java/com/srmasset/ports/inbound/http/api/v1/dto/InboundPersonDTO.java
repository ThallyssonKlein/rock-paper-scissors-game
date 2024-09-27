package com.srmasset.ports.inbound.http.api.v1.dto;

import com.srmasset.ports.inbound.http.api.v1.validator.ValidDate;
import com.srmasset.ports.inbound.http.api.v1.validator.ValidIdentifier;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InboundPersonDTO {
    @NotNull(message = "Name cannot be null")
    @Max(value = 50, message = "Name cannot be greater than 50 characters")
    private String name;

    @NotNull(message = "Identifier cannot be null")
    @ValidIdentifier
    private String identifier;

    @NotNull(message = "Birth date cannot be null")
    @ValidDate(fieldName = "birthDate")
    private Date birthDate;
}
