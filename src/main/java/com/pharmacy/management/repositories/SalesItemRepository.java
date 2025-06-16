package com.pharmacy.management.repositories;
import com.pharmacy.management.models.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SalesItemRepository extends JpaRepository<SalesItem, Long> {}
