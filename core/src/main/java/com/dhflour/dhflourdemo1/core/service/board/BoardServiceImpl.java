package com.dhflour.dhflourdemo1.core.service.board;

import com.dhflour.dhflourdemo1.core.domain.board.BoardEntity;
import com.dhflour.dhflourdemo1.core.domain.board.BoardEntityRepository;
import com.dhflour.dhflourdemo1.core.domain.board.BoardEntitySpecifications;
import com.dhflour.dhflourdemo1.core.domain.category.CategoryEntity;
import com.dhflour.dhflourdemo1.core.domain.category.CategoryEntityRepository;
import com.dhflour.dhflourdemo1.core.types.error.BadRequestException;
import com.dhflour.dhflourdemo1.core.types.error.UpdateErrorException;
import com.dhflour.dhflourdemo1.core.types.pagination.PageFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService {

    private final String ENTITY_NAME = BoardEntity.class.getName();

    @Autowired
    private BoardEntityRepository boardEntityRepository;

    @Autowired
    private CategoryEntityRepository categoryEntityRepository;

    @Override
    @Transactional // 쓰기 전용 트랙젝션
    public BoardEntity create(BoardEntity entity) {
        log.debug("entity : {}", entity);
        if (entity.getCategories() != null) {
            log.debug("entity.getCategories() : {}", entity.getCategories());
        }
        // Validation 처리
        return boardEntityRepository.save(entity);
    }

    @Override
    @Transactional
    public BoardEntity create(BoardEntity entity, Long idCategory) {
        CategoryEntity categoryEntity = categoryEntityRepository.findById(idCategory).orElse(null);
        if (categoryEntity != null)
            entity.setCategories(Set.of(categoryEntity));
        return boardEntityRepository.save(entity);
    }

    @Override
    @Transactional // 쓰기 전용 트랙젝션
    public BoardEntity update(BoardEntity entity) {
        if (entity.getId() == null) {
            throw new BadRequestException();
        }
        // Validation 처리
        return boardEntityRepository.findById(entity.getId())
                .map(original -> {
                    BeanUtils.copyProperties(entity, original, "id");
                    return boardEntityRepository.save(original);
                }).orElseThrow(() -> new UpdateErrorException(entity.getId(), ENTITY_NAME));
    }

    @Override
    @Transactional // 쓰기 전용 트랙젝션
    public void delete(BoardEntity entity) {
        boardEntityRepository.findById(entity.getId())
                .ifPresent(boardEntity -> {
                    boardEntityRepository.delete(boardEntity);
                });
    }

    @Override
    @Transactional(readOnly = true) // 읽기 전용 트랙젝션
    public BoardEntity get(Locale locale, Long id) {
        return boardEntityRepository.findById(id).map(boardEntity -> {
            // locale 다국어 처리
            return boardEntity;
        }).orElse(null);
    }

    @Override
    @Transactional(readOnly = true) // 읽기 전용 트랙젝션
    public Page<BoardEntity> page(PageFilter filter) {
        Specification<BoardEntity> spec = Specification.where(null);

        if (filter.getQuery() != null && !filter.getQuery().isEmpty()) {
            spec = spec.and(BoardEntitySpecifications.titleContains(filter.getQuery()));
        }
        Page<BoardEntity> page = boardEntityRepository.findAll(spec, filter.getPageable());
        return page;
    }

    //
    //성능 최적화:
    //- 읽기 전용 트랜잭션은 데이터베이스에 대한 작업이 조회(Select)에 국한되므로, 데이터베이스는 데이터를 수정하기 위한 잠금 또는 추가적인 리소스 할당을 수행할 필요가 없습니다. 이는 데이터베이스의 부하를 줄이고, 전체적인 조회 성능을 향상시킬 수 있습니다.
    //- 일부 데이터베이스 관리 시스템(DBMS)에서는 읽기 전용 쿼리에 최적화된 쿼리 플랜을 제공할 수도 있습니다.
    //데이터 무결성 보장:
    //- readOnly = true로 설정함으로써, 해당 트랜잭션 내에서는 데이터가 변경되지 않습니다. 이는 우발적인 데이터 변경이나 오염을 방지하여 데이터의 일관성과 무결성을 유지하는 데 도움을 줍니다.
    //트랜잭션 관리의 명확성:
    //- 트랜잭션의 읽기 전용 여부를 명시적으로 선언함으로써 개발자들이 코드의 의도를 더 명확하게 이해할 수 있으며, 유지보수 시에도 해당 메소드의 동작 방식을 쉽게 파악할 수 있습니다.
}
