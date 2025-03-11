package org.example.test_orm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Material{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Поля название материала не должна быть пустым")
    @Length(min = 1, max = 124, message = "Длина поля название должен минимум 1 максимум 124")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "Производитель не должен быть null")
    private Producer producer;

    @NotNull(message = "Количество материала не должно быть пустым")
    private Integer count;

    @DecimalMin(value = "0.00", message = "Цена не должна быть отрицательной")
    @Digits(integer = 13, fraction = 2)
    @NotNull(message = "Поля цены не должна быть пустым")
    private BigDecimal price;
}
