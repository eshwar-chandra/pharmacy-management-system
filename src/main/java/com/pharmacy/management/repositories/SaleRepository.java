package com.pharmacy.management.repositories;
import com.pharmacy.management.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {}
