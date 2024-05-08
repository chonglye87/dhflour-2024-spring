package com.dhflour.dhflourdemo1.core.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryEntityRepository extends JpaRepository<CategoryEntity, Long>, JpaSpecificationExecutor<CategoryEntity>, CategoryEntityRepositoryCustom {
}
