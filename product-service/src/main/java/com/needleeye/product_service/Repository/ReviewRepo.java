package com.needleeye.product_service.Repository;

import com.needleeye.product_service.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepo extends JpaRepository<Review,Long> {

}
