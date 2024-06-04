package com.dhflour.dhflourdemo1.jpa.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity>, UserEntityRepositoryCustom {

    Optional<UserEntity> findOneByEmail(String email); // 이메일로 회원을 찾는 쿼리
}
