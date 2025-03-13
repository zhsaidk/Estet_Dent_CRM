package org.example.test_orm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.test_orm.entity.Material;
import org.example.test_orm.repository.MaterialsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialsRepository materialsRepository;

    public List<Material> getAllMaterials() {
        return materialsRepository.findAllWithProducers();
    }

    public void copyMaterialProperties(Material material, Material newMaterial) {
        newMaterial.setName(material.getName());
        newMaterial.setProducer(newMaterial.getProducer());
        newMaterial.setCount(newMaterial.getCount());
        newMaterial.setPrice(newMaterial.getPrice());
    }

    public Material create(Material material) {
        return materialsRepository.save(material);
    }

    public List<Material> getMaterialsByName(String word) {
        return materialsRepository.findMaterialsByName("%" + word + "%");
    }

    public Material getMaterialById(Long id) {
        return materialsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material not found"));
    }

    @Transactional
    public void updateMaterial(Long id, Material from) {
        materialsRepository.findById(id)
                .map(material -> {
                    copyMaterialProperties(from, material);
                    return materialsRepository.save(material);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material not found"));
    }

    @Transactional
    public boolean updateCount(Long id, Integer count){
          return materialsRepository.findById(id)
                .map(material -> {
                    material.setCount(count);
                    return true;
                })
                  .orElse(false);
    }

    public boolean deleteMaterialById(Long id) {
        return materialsRepository.findById(id)
                .map(material -> {
                    materialsRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

    public List<Material> getFilteredMaterials(Integer producerId, BigDecimal minPrice, BigDecimal maxPrice, Integer minCount, Integer maxCount) {
        return materialsRepository.findFilteredMaterials(producerId, minPrice, maxPrice, minCount, maxCount);
    }

}
