package org.example.rzah.ordermanagement.repository;

import org.example.rzah.ordermanagement.domain.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
