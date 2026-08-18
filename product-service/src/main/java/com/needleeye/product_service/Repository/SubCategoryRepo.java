package com.needleeye.product_service.Repository;

import com.needleeye.product_service.Entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepo extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findByCategory_Id(Long categoryId);
}
