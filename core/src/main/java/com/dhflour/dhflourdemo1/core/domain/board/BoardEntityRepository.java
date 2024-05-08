package com.dhflour.dhflourdemo1.core.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardEntityRepository extends JpaRepository<BoardEntity, Long>, JpaSpecificationExecutor<BoardEntity>, BoardEntityRepositoryCustom {
}
