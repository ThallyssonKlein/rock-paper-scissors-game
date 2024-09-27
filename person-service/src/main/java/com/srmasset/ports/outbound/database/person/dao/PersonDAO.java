package com.srmasset.ports.outbound.database.person.dao;

import com.srmasset.ports.outbound.database.IdentifierType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PersonDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, length = 50, name = "name")
    private String name;

    @Column(nullable = false, length = 14, name = "identifier")
    private String identifier;

    @Column(nullable = false, name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(nullable = false, name = "identifier_type")
    @Enumerated(EnumType.STRING)
    private IdentifierType identifierType;

    @Column(nullable = false, precision = 18, scale = 4, name = "min_monthly_value")
    private BigDecimal minMonthlyValue;

    @Column(nullable = false, precision = 18, scale = 4, name = "max_loan_value")
    private BigDecimal maxLoanValue;
}