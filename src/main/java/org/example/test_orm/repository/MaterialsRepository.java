package org.example.test_orm.repository;

import org.example.test_orm.entity.Material;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialsRepository extends JpaRepository<Material, Long> {

    @Query("select m from Material m join fetch m.producer p where lower(m.name) like lower(:name)")
    List<Material> findMaterialsByName(String name);

    @Query("select m from Material m join fetch m.producer p")
    List<Material> findAllWithProducers();

    @EntityGraph(attributePaths = {"producer"})
    Optional<Material> findById(Long id);

    @Query("select m from Material m join fetch m.producer p where p.id = :producerId")
    List<Material> findMaterialsByProducer(Integer producerId);

    @EntityGraph(attributePaths = {"producer"})
    @Query("SELECT m FROM Material m WHERE " +
            "(:producerId IS NULL OR m.producer.id = :producerId) AND " +
            "(:minPrice IS NULL OR m.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR m.price <= :maxPrice) AND " +
            "(:minCount IS NULL OR m.count >= :minCount) AND " +
            "(:maxCount IS NULL OR m.count <= :maxCount)")
    List<Material> findFilteredMaterials(@Param("producerId") Integer producerId,
                                         @Param("minPrice") BigDecimal minPrice,
                                         @Param("maxPrice") BigDecimal maxPrice,
                                         @Param("minCount") Integer minCount,
                                         @Param("maxCount") Integer maxCount);


    Optional<Material> findByName(String name);
}
