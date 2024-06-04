//package com.dhflour.dhflourdemo1.api.web.board;
//
//import com.dhflour.dhflourdemo1.jpa.domain.category.CategoryEntity;
//import com.dhflour.dhflourdemo1.core.service.category.CategoryService;
//import com.dhflour.dhflourdemo1.core.types.category.CategoryPaginationResponse;
//import com.dhflour.dhflourdemo1.core.types.category.CategoryRequest;
//import com.dhflour.dhflourdemo1.jpa.types.PageFilter;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.Parameters;
//import io.swagger.v3.oas.annotations.enums.ParameterIn;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Locale;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/v1/category")
//public class CategoryController {
//
//
//    @Autowired
//    private CategoryService categoryService;
//
//
//    @Operation(summary = "[category-1] 카테고리 목록 조회 (Pagination)",
//            description = "게시물 페이징 목록 조회합니다.",
//            operationId = "pageCategory")
//    @Parameters(value = {
//            @Parameter(in = ParameterIn.QUERY, name = "size", description = "Page Size 페이지 크기 (default : 20)", example = "20"),
//            @Parameter(in = ParameterIn.QUERY, name = "page", description = "현재 페이지 0부터 (Current Page)  현재 페이지 (default : 0)", example = "0"),
//            @Parameter(in = ParameterIn.QUERY, name = "sort", description = "정렬 (Sort Page)", example = ""),
//    })
//    @ApiResponse(responseCode = "200", description = "성공적으로 페이지 정보를 불러옴",
//            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
//                    schema = @Schema(implementation = CategoryPaginationResponse.class)))
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> page(@Parameter(hidden = true) @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
//                                  @RequestParam(required = false, defaultValue = "") String startDate,
//                                  @RequestParam(required = false, defaultValue = "") String endDate,
//                                  @RequestParam(required = false, defaultValue = "") String query,
//                                  Locale locale) {
//
//        PageFilter filter = new PageFilter.Builder()
//                .pageable(pageable)
//                .query(query)
//                .startDate(startDate)
//                .endDate(endDate)
//                .locale(locale)
//                .build();
//
//        Page<CategoryEntity> page = categoryService.page(filter);
//        return page != null ? ResponseEntity.ok(new CategoryPaginationResponse(page)) : ResponseEntity.noContent().build();
//    }
//
//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "[category-2] 카테고리 등록",
//            description = "새로운 게시글을 등록합니다.",
//            operationId = "createCategory")
//    @ApiResponse(responseCode = "201", description = "게시글이 성공적으로 등록됨",
//            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
//                    schema = @Schema(implementation = CategoryRequest.class)))
//    public ResponseEntity<CategoryEntity> createCategory(@RequestBody CategoryRequest payload) {
//        CategoryEntity createdCategory = categoryService.create(payload.toEntity());
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
//    }
//
//    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "[category-3] 카테고리 상세 조회",
//            description = "특정 게시글의 상세 정보를 조회합니다.",
//            operationId = "getCategoryById")
//    @ApiResponse(responseCode = "200", description = "게시글 상세 정보 반환",
//            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
//                    schema = @Schema(implementation = CategoryEntity.class)))
//    public ResponseEntity<CategoryEntity> getCategoryById(@PathVariable Long id,
//                                                          Locale locale) {
//        CategoryEntity board = categoryService.get(locale, id);
//        return board != null ? ResponseEntity.ok(board) : ResponseEntity.notFound().build();
//    }
//
//    @DeleteMapping("/{id}")
//    @Operation(summary = "[category-4] 카테고리 삭제",
//            description = "특정 게시글을 삭제합니다.",
//            operationId = "deleteCategory")
//    @ApiResponse(responseCode = "204", description = "게시글이 성공적으로 삭제됨")
//    public ResponseEntity<?> deleteCategory(@PathVariable Long id,
//                                            Locale locale) {
//        CategoryEntity board = categoryService.get(locale, id);
//        if (board != null) {
//            categoryService.delete(board);
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}
