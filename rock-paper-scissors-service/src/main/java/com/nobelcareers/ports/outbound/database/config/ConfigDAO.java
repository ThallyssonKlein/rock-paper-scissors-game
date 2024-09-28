package com.nobelcareers.ports.outbound.database.config;

import com.nobelcareers.ports.outbound.database.IdentifierType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "config")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ConfigDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, precision = 18, scale = 4, name = "max_loan_value")
    private BigDecimal maxLoanValue;

    @Column(nullable = false, precision = 18, scale = 4, name = "min_monthly_value")
    private BigDecimal minMonthlyValue;

    @Column(nullable = false, name = "identifier_type")
    @Enumerated(EnumType.STRING)
    private IdentifierType identifierType;
}