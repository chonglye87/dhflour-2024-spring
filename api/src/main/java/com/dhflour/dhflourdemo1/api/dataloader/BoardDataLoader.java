package com.dhflour.dhflourdemo1.api.dataloader;

import com.dhflour.dhflourdemo1.core.domain.board.BoardEntity;
import com.dhflour.dhflourdemo1.core.repository.board.BoardEntityRepository;
import com.dhflour.dhflourdemo1.core.service.board.BoardService;
import com.dhflour.dhflourdemo1.core.service.category.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
@Order(2)
public class BoardDataLoader implements CommandLineRunner {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardEntityRepository boardEntityRepository;

    @Autowired
    private CategoryService categoryService;

    @Override
    public void run(String... args) throws Exception {
        log.debug("::: BoardDataLoader:Run :::");

        if (boardEntityRepository.findAll().isEmpty()) {
            for (int i = 0; i < 100; i++) {
                this.create("제목 : " + i, "<p>내용 " + i + "</p>", i % 2 == 0 ? 1L : 2L);
            }
        }
    }

    private BoardEntity create(String title, String content, Long idCategory) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setTitle(title);
        boardEntity.setContent(content);
        return boardService.create(boardEntity, idCategory);
    }
}
