package com.needleeye.product_service.Repository;

import com.needleeye.product_service.Entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepo extends JpaRepository<Color,Long> {
}
