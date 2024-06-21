package com.dhflour.dhflourdemo1.jpa.domain.push;

import com.dhflour.dhflourdemo1.jpa.domain.board.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PushNotificationEntityRepository extends JpaRepository<PushNotificationEntity, Long>, JpaSpecificationExecutor<BoardEntity>, PushNotificationEntityRepositoryCustom {

}
