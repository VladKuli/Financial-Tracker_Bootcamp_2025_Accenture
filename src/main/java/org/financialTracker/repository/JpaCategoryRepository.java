package org.financialTracker.repository;

import org.financialTracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE :title IS NULL OR c.title = :title")
    List<Category> findCategoriesByFilter(@Param("title") String title);
}
