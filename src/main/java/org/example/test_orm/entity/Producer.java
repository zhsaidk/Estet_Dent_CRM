package org.example.test_orm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Название не должен быть пустым")
    @Length(min = 2, max = 124, message = "Длина должна быть в пределе от 2 до 124")
    private String name;

    @OneToMany(mappedBy = "producer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Material> materials;
}
