package org.example.test_orm.service;

import lombok.RequiredArgsConstructor;
import org.example.test_orm.entity.Producer;
import org.example.test_orm.repository.ProducerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final ProducerRepository producerRepository;

    public List<Producer> getAllProducers() {
        return producerRepository.findAll();
    }

    public void createProducer(Producer producer) {
        producerRepository.save(producer);
    }

    public boolean deleteProducer(Integer id){
        return producerRepository.findById(id)
                .map(prod-> {
                    producerRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
