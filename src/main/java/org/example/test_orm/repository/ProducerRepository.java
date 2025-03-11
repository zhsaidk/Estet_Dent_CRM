package org.example.test_orm.repository;

import jakarta.validation.constraints.NotBlank;
import org.example.test_orm.entity.Producer;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Integer> {
    Optional<Producer> findProducerByName(@NotBlank(message = "Название не должен быть пустым") @Length(min = 2, max = 124, message = "Длина должна быть в пределе от 2 до 124") String name);
}
