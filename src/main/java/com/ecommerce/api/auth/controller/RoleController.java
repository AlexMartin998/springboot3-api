package com.ecommerce.api.auth.controller;

import com.ecommerce.api.auth.dto.RoleCreateDTO;
import com.ecommerce.api.auth.dto.RoleResponseDTO;
import com.ecommerce.api.auth.dto.RoleUpdateDTO;
import com.ecommerce.api.auth.service.RoleService;
import com.ecommerce.api.shared.constants.PaginationConstants;
import com.ecommerce.api.shared.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @PostMapping
    public ResponseEntity<RoleResponseDTO> create(@RequestBody RoleCreateDTO dto) {
        return ResponseEntity.status(201).body(service.create(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> update(@PathVariable Long id, @RequestBody RoleUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<RoleResponseDTO>> getAll(
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SIZE) int size) {
        Page<RoleResponseDTO> p = service.getAll(PageRequest.of(page, size));
        PageResponse<RoleResponseDTO> resp = PageResponse.<RoleResponseDTO>builder()
                .data(p.getContent())
                .totalElements(p.getTotalElements())
                .totalPages(p.getTotalPages())
                .page(page)
                .size(size)
                .build();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<RoleResponseDTO>> getRolesFiltered(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Boolean state,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = PaginationConstants.DEFAULT_SIZE) int size
    ) {
        Page<RoleResponseDTO> p = service.getSearch(q, state, PageRequest.of(page, size));
        PageResponse<RoleResponseDTO> resp = PageResponse.<RoleResponseDTO>builder()
                .data(p.getContent())
                .totalElements(p.getTotalElements())
                .totalPages(p.getTotalPages())
                .page(page)
                .size(size)
                .build();
        return ResponseEntity.ok(resp);
    }

}
