package com.dhflour.dhflourdemo1.api.service.board;

import com.dhflour.dhflourdemo1.api.domain.board.RBoard;
import com.dhflour.dhflourdemo1.api.domain.board.RBoardRepository;
import com.dhflour.dhflourdemo1.api.domain.boardcategory.RBoardToCategory;
import com.dhflour.dhflourdemo1.api.domain.boardcategory.RBoardToCategoryRepository;
import com.dhflour.dhflourdemo1.api.domain.category.RBoardCategoryRepository;
import com.dhflour.dhflourdemo1.api.types.pagination.PageFilter;
import com.dhflour.dhflourdemo1.api.utils.ModelMapperUtils;
import com.dhflour.dhflourdemo1.core.types.error.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BoardAPIServiceImpl implements BoardAPIService {

    @Autowired
    private RBoardRepository repository;

    @Autowired
    private RBoardCategoryRepository categoryRepository;

    @Autowired
    private RBoardToCategoryRepository boardToCategoryRepository;

    @Autowired
    private ModelMapperUtils modelMapperUtils;

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<RBoard>> page(PageFilter pageFilter, List<Long> categoryIds) {
        return this.repository.findAllByFilter(pageFilter, categoryIds) // 페이지네이션 정보에 따라 모든 게시판 항목을 조회
                .flatMap(this::fillCategories) // 각 RBoard에 대해 categories를 채움
                .collectList() // 조회된 항목들을 리스트로 수집
                .zipWith(this.repository.count()) // 전체 항목 수와 수집된 리스트를 함께 결합
                .map(p -> new PageImpl<>(p.getT1(), pageFilter.getPageable(), p.getT2())); // 수집된 리스트와 전체 항목 수를 사용해 Page 객체를 생성
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<RBoard> getById(Long id) {
        return repository.findById(id) // 주어진 ID에 해당하는 게시판 항목을 조회
                .flatMap(this::fillCategories) // 각 RBoard에 대해 categories를 채움
                .switchIfEmpty(Mono.error(new NotFoundException())); // 조회된 항목이 없으면 NotFoundException을 발생시킴
    }

    @Override
    @Transactional
    public Mono<RBoard> create(RBoard entity) {
        return repository.save(entity); // 새로운 게시판 항목을 저장하고 저장된 항목을 반환
    }

    @Override
    @Transactional
    public Mono<RBoard> create(RBoard entity, List<Long> categoryIds) {
        return repository.save(entity)
                // flatMap을 사용하여 저장된 RBoard 엔티티를 처리합니다.
                .flatMap(savedBoard -> {
                    // categoryIds 리스트를 RBoardToCategory 객체들의 리스트로 변환합니다.
                    List<RBoardToCategory> boardToCategories = categoryIds.stream()
                            // 각 categoryId에 대해 RBoardToCategory 객체를 생성합니다.
                            .map(categoryId -> {
                                RBoardToCategory boardToCategory = new RBoardToCategory();
                                boardToCategory.setBoardId(savedBoard.getId());
                                boardToCategory.setCategoryId(categoryId);
                                return boardToCategory;
                            }).collect(Collectors.toList());
                    // RBoardToCategory 리스트를 데이터베이스에 저장합니다.
                    return boardToCategoryRepository.saveAll(boardToCategories).collectList()
                            .thenReturn(savedBoard) // 저장이 완료된 후, savedBoard를 반환합니다.
                            .flatMap(this::fillCategories)  // fillCategories 메서드를 호출하여 categories 필드를 채웁니다.
                            .switchIfEmpty(Mono.error(new NotFoundException()));   // 만약 결과가 존재하지 않는다면 NotFoundException을 발생시킵니다.
                });
    }

    @Override
    @Transactional
    public Mono<RBoard> update(Long id, RBoard entity) {
        return repository.findById(id) // 주어진 ID에 해당하는 게시판 항목을 조회
                .flatMap(existingEntity -> { // 조회된 항목이 존재하면
                    modelMapperUtils.mapSkippingFields(entity, existingEntity, "id"); // ID 필드를 제외한 나머지 필드들을 기존 항목에 매핑
                    return repository.save(existingEntity); // 수정된 항목을 저장하고 반환
                })
                .switchIfEmpty(Mono.error(new NotFoundException())); // 조회된 항목이 없으면 NotFoundException을 발생시킴
    }

    @Override
    @Transactional
    public Mono<RBoard> update(Long id, RBoard entity, List<Long> categoryIds) {
        return repository.findById(id)
                .flatMap(existingEntity -> {
                    modelMapperUtils.mapSkippingFields(entity, existingEntity, "id");

                    // 기존의 RBoardToCategory 엔티티를 삭제
                    Mono<Void> deleteOldCategories = boardToCategoryRepository.deleteByBoardId(id);

                    // 새로운 RBoardToCategory 엔티티 생성
                    List<RBoardToCategory> newBoardToCategories = categoryIds.stream()
                            .map(categoryId -> {
                                RBoardToCategory boardToCategory = new RBoardToCategory();
                                boardToCategory.setBoardId(existingEntity.getId());
                                boardToCategory.setCategoryId(categoryId);
                                return boardToCategory;
                            }).collect(Collectors.toList());

                    // 기존 엔티티 업데이트 및 새로운 카테고리 엔티티 저장
                    return deleteOldCategories.then(repository.save(existingEntity))
                            .flatMap(savedBoard -> boardToCategoryRepository.saveAll(newBoardToCategories).collectList()
                                    .thenReturn(savedBoard)
                                    .flatMap(this::fillCategories)
                                    .switchIfEmpty(Mono.error(new NotFoundException())));
                })
                .switchIfEmpty(Mono.error(new NotFoundException())); // 만약 결과가 존재하지 않는다면 NotFoundException을 발생시킵니다.
    }

    @Override
    @Transactional
    public Mono<Void> delete(Long id) {
        return repository.findById(id) // 주어진 ID에 해당하는 RBoard 엔티티를 조회
                .switchIfEmpty(Mono.error(new NotFoundException())) // 조회된 엔티티가 없으면 NotFoundException을 발생시킴
                .flatMap(existingEntity -> {
                    // 관련된 RBoardToCategory 엔티티들을 먼저 삭제
                    Mono<Void> deleteCategories = boardToCategoryRepository.deleteByBoardId(id);

                    // RBoard 엔티티 자체를 삭제
                    Mono<Void> deleteBoard = repository.deleteById(existingEntity.getId());

                    // 두 삭제 작업이 모두 완료되면 Mono 반환
                    return deleteCategories.then(deleteBoard);
                });
    }

    @Override
    @Transactional
    public Mono<Void> deleteAll(List<Long> ids) {
        // 주어진 ID 목록에 해당하는 RBoard 엔티티들을 조회
        return Flux.fromIterable(ids)
                .flatMap(id -> repository.findById(id)
                        .switchIfEmpty(Mono.error(new NotFoundException())) // 만약 엔티티가 존재하지 않으면 NotFoundException 발생
                        .flatMap(existingEntity -> {
                            // 관련된 RBoardToCategory 엔티티들을 먼저 삭제
                            Mono<Void> deleteCategories = boardToCategoryRepository.deleteByBoardId(id);

                            // RBoard 엔티티 자체를 삭제
                            Mono<Void> deleteBoard = repository.deleteById(existingEntity.getId());

                            // 두 삭제 작업이 모두 완료되면 Mono 반환
                            return deleteCategories.then(deleteBoard);
                        })
                )
                .then(); // 모든 삭제 작업이 완료되면 Mono<Void> 반환
    }


    private Mono<RBoard> fillCategories(RBoard board) {
        return this.boardToCategoryRepository.findByBoardId(board.getId())
                .flatMap(boardToCategory ->
                        categoryRepository.findById(boardToCategory.getCategoryId())
                )
                .collectList()
                .map(categories -> {
                    board.setCategories(categories);
                    return board;
                });
    }
}
