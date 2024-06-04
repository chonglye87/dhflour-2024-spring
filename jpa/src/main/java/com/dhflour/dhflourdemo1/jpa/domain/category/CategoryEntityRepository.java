package com.dhflour.dhflourdemo1.jpa.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryEntityRepository extends JpaRepository<CategoryEntity, Long>, JpaSpecificationExecutor<CategoryEntity>, CategoryEntityRepositoryCustom {

    List<CategoryEntity> findAllByIdIn(List<Long> ids);
}
