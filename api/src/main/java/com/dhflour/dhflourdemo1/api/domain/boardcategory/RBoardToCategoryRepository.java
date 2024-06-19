package com.dhflour.dhflourdemo1.api.domain.boardcategory;

import com.dhflour.dhflourdemo1.api.domain.category.RBoardCategory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RBoardToCategoryRepository extends ReactiveCrudRepository<RBoardCategory, Long> {
}