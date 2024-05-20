package com.dhflour.dhflourdemo1.core.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardEntityRepository extends JpaRepository<BoardEntity, Long>, JpaSpecificationExecutor<BoardEntity>, BoardEntityRepositoryCustom {


    @Query(nativeQuery = true, value = "select * from Board where title = :title")
    List<BoardEntity> listTest(String title);
}
