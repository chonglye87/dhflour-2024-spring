package com.dhflour.dhflourdemo1.api.types.pagination;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "페이지네이션 응답 구조")
public class PaginationResponse<T> {

    @Schema(description = "현재 페이지 번호", example = "0", requiredMode = Schema.RequiredMode.REQUIRED, implementation = Integer.class)
    private int page;

    @Schema(description = "페이지 크기", example = "20", requiredMode = Schema.RequiredMode.REQUIRED, implementation = Integer.class)
    private int size;

    @Schema(description = "전체 요소 수", example = "100", requiredMode = Schema.RequiredMode.REQUIRED, implementation = Long.class)
    private long totalElements;

    @Schema(description = "전체 페이지 수", example = "5", requiredMode = Schema.RequiredMode.REQUIRED, implementation = Integer.class)
    private int totalPages;

    @ArraySchema(arraySchema = @Schema(description = "페이지에 포함된 콘텐츠"))
    private List<T> content;

    public PaginationResponse(Page<T> pageData) {
        this.page = pageData.getNumber();          // Spring Data Page에서 제공하는 페이지 번호는 0부터 시작
        this.size = pageData.getSize();            // 한 페이지에 보여질 요소의 수
        this.totalElements = pageData.getTotalElements(); // 전체 요소의 수
        this.totalPages = pageData.getTotalPages(); // 전체 페이지 수 계산
        this.content = pageData.getContent();      // 현재 페이지의 실제 데이터 리스트
    }
}