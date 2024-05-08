package com.dhflour.dhflourdemo1.core.service.board;

import com.dhflour.dhflourdemo1.core.domain.board.BoardEntity;
import com.dhflour.dhflourdemo1.core.types.pagination.PageFilter;
import org.springframework.data.domain.Page;

import java.util.Locale;

public interface BoardService {
    // Create
    BoardEntity create(BoardEntity entity);

    // Update
    BoardEntity update(BoardEntity entity);

    // Delete
    void delete(BoardEntity entity);

    // get Object
    BoardEntity get(Locale locale, Long id);

    // Pagination
    Page<BoardEntity> page(PageFilter filter);
}
