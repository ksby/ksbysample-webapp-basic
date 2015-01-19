package ksbysample.webapp.basic.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class Country {

    @Id
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String continent;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal surfaceArea;

    private Long indepYear;

    @Column(nullable = false)
    private Long population;

    @Column(precision = 2, scale = 1)
    private BigDecimal lifeExpectancy;

    @Column(precision = 8, scale = 2)
    private BigDecimal gNP;

    @Column(precision = 8, scale = 2)
    private BigDecimal gNPOld;

    @Column(nullable = false)
    private String localName;

    @Column(nullable = false)
    private String governmentForm;

    @Column(nullable = false)
    private String headOfState;

    private Long capital;

    @Column(nullable = false)
    private String code2;

}
