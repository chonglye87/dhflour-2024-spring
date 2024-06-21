package com.dhflour.dhflourdemo1.jpa.service.board;

import com.dhflour.dhflourdemo1.jpa.types.board.BoardRequest;
import com.dhflour.dhflourdemo1.jpa.domain.board.BoardEntity;
import com.dhflour.dhflourdemo1.jpa.types.PageFilter;
import org.springframework.data.domain.Page;

import java.util.Locale;

public interface BoardService {
    // Create
    BoardEntity create(BoardEntity entity);

    BoardEntity create(BoardEntity entity, Long idCategory);

    // Update
    BoardEntity update(BoardEntity entity);

    // Delete
    void delete(BoardEntity entity);

    // get Object
    BoardEntity get(Locale locale, Long id);

    // Pagination
    Page<BoardEntity> page(PageFilter filter);

    BoardEntity toEntity(BoardRequest request);
}
