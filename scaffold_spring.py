#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import argparse
from pathlib import Path
from datetime import date

# ---------- helpers ----------
def to_pkg_path(pkg: str) -> str:
    return pkg.replace(".", "/")

def camel_to_snake(name: str) -> str:
    out = []
    for i, ch in enumerate(name):
        if ch.isupper() and i != 0:
            out.append("_")
        out.append(ch.lower())
    return "".join(out)

def ucfirst(s: str) -> str:
    return s[:1].upper() + s[1:]

def lcfirst(s: str) -> str:
    return s[:1].lower() + s[1:]

def write_file(path: Path, content: str, overwrite=False):
    path.parent.mkdir(parents=True, exist_ok=True)
    if path.exists() and not overwrite:
        print(f"skip (exists): {path}")
        return False
    path.write_text(content, encoding="utf-8")
    print(f"created: {path}")
    return True

# ---------- templates ----------
def tpl_entity(pkg, feature, entity, id_type):
    table = camel_to_snake(entity)
    return f"""package {pkg}.{feature}.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "{table}")
public class {entity} {{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private {id_type} id;

    // TODO: agrega tus propiedades de dominio aquí
    // private String nombre;
}}
"""

def tpl_repository(pkg, feature, entity, id_type):
    return f"""package {pkg}.{feature}.repository;

import {pkg}.{feature}.entity.{entity};
import org.springframework.data.jpa.repository.JpaRepository;


public interface {entity}Repository extends JpaRepository<{entity}, {id_type}> {{

}}
"""

def tpl_dto_create(pkg, feature, entity):
    return f"""package {pkg}.{feature}.dto;

import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class {entity}CreateDTO {{

    // TODO: campos requeridos para crear

}}
"""

def tpl_dto_update(pkg, feature, entity):
    return f"""package {pkg}.{feature}.dto;

import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class {entity}UpdateDTO {{
    // TODO: campos updatable (sin el id, que va en la ruta)
    // private String nombre;
}}
"""

def tpl_dto_response(pkg, feature, entity, id_type):
    return f"""package {pkg}.{feature}.dto;

import lombok.*;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class {entity}ResponseDTO {{
    private {id_type} id;
    // TODO: refleja lo que expones al cliente
    // private String nombre;
}}
"""

def tpl_page_response(pkg):
    return f"""package {pkg}.shared.dto;

import lombok.*;
import java.util.List;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PageResponse<T> {{
    private List<T> data;
    private long totalElements;
    private int totalPages;
    private int page;
    private int size;
}}
"""

def tpl_service_interface(pkg, feature, entity, id_type):
    return f"""package {pkg}.{feature}.service;

import {pkg}.{feature}.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface {entity}Service {{

    {entity}ResponseDTO create({entity}CreateDTO dto);

    {entity}ResponseDTO update({id_type} id, {entity}UpdateDTO dto);
    void delete({id_type} id);

    {entity}ResponseDTO getById({id_type} id);

    Page<{entity}ResponseDTO> getAll(Pageable pageable);

}}
"""

def tpl_mapper_mapstruct(pkg, feature, entity):
    return f"""package {pkg}.{feature}.mapper;

import {pkg}.{feature}.entity.{entity};
import {pkg}.{feature}.dto.*;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface {entity}Mapper {{

    {entity} toEntity({entity}CreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto({entity}UpdateDTO dto, @MappingTarget {entity} entity);

    {entity}ResponseDTO toResponse({entity} entity);

}}
"""

def tpl_service_impl_mapstruct(pkg, feature, entity, id_type):
    return f"""package {pkg}.{feature}.service.impl;

import {pkg}.{feature}.dto.*;
import {pkg}.{feature}.entity.{entity};
import {pkg}.{feature}.mapper.{entity}Mapper;
import {pkg}.{feature}.repository.{entity}Repository;
import {pkg}.{feature}.service.{entity}Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class {entity}ServiceImpl implements {entity}Service {{

    private final {entity}Repository repository;
    private final {entity}Mapper mapper;

    @Override
    public {entity}ResponseDTO create({entity}CreateDTO dto) {{
        {entity} e = mapper.toEntity(dto);
        e = repository.save(e);
        return mapper.toResponse(e);
    }}

    @Override
    public {entity}ResponseDTO update({id_type} id, {entity}UpdateDTO dto) {{
        {entity} e = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("{entity} not found"));
        mapper.updateEntityFromDto(dto, e);
        e = repository.save(e);
        return mapper.toResponse(e);
    }}

    @Override
    public void delete({id_type} id) {{
        repository.deleteById(id);
    }}

    @Override
    @Transactional(readOnly = true)
    public {entity}ResponseDTO getById({id_type} id) {{
        return repository.findById(id).map(mapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("{entity} not found"));
    }}

    @Override
    @Transactional(readOnly = true)
    public Page<{entity}ResponseDTO> getAll(Pageable pageable) {{
        return repository.findAll(pageable).map(mapper::toResponse);
    }}
}}
"""

def tpl_service_impl_manual(pkg, feature, entity, id_type):
    return f"""package {pkg}.{feature}.service.impl;

import {pkg}.{feature}.dto.*;
import {pkg}.{feature}.entity.{entity};
import {pkg}.{feature}.repository.{entity}Repository;
import {pkg}.{feature}.service.{entity}Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class {entity}ServiceImpl implements {entity}Service {{

    private final {entity}Repository repository;

    private {entity} toEntity({entity}CreateDTO dto) {{
        {entity} e = new {entity}();
        // TODO: mapear campos
        // e.setNombre(dto.getNombre());
        return e;
    }}

    private void updateEntity({entity}UpdateDTO dto, {entity} e) {{
        // TODO: mapear campos actualizables
        // if (dto.getNombre() != null) e.setNombre(dto.getNombre());
    }}

    private {entity}ResponseDTO toResponse({entity} e) {{
        {entity}ResponseDTO dto = new {entity}ResponseDTO();
        dto.setId(e.getId());
        // dto.setNombre(e.getNombre());
        return dto;
    }}

    @Override
    public {entity}ResponseDTO create({entity}CreateDTO dto) {{
        {entity} e = toEntity(dto);
        e = repository.save(e);
        return toResponse(e);
    }}

    @Override
    public {entity}ResponseDTO update({id_type} id, {entity}UpdateDTO dto) {{
        {entity} e = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("{entity} not found"));
        updateEntity(dto, e);
        e = repository.save(e);
        return toResponse(e);
    }}

    @Override
    public void delete({id_type} id) {{
        repository.deleteById(id);
    }}

    @Override
    @Transactional(readOnly = true)
    public {entity}ResponseDTO getById({id_type} id) {{
        return repository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("{entity} not found"));
    }}

    @Override
    @Transactional(readOnly = true)
    public Page<{entity}ResponseDTO> getAll(Pageable pageable) {{
        return repository.findAll(pageable).map(this::toResponse);
    }}
}}
"""

def tpl_controller(pkg, feature, entity, id_type, rest_path):
    return f"""package {pkg}.{feature}.controller;

import {pkg}.{feature}.dto.*;
import {pkg}.{feature}.service.{entity}Service;
import {pkg}.shared.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("{rest_path}")
@RequiredArgsConstructor
public class {entity}Controller {{

    private final {entity}Service service;

    @PostMapping
    public ResponseEntity<{entity}ResponseDTO> create(@RequestBody {entity}CreateDTO dto) {{
        return ResponseEntity.status(201).body(service.create(dto));
    }}

    @PatchMapping("/{{id}}")
    public ResponseEntity<{entity}ResponseDTO> update(@PathVariable {id_type} id, @RequestBody {entity}UpdateDTO dto) {{
        return ResponseEntity.ok(service.update(id, dto));
    }}

    @DeleteMapping("/{{id}}")
    public ResponseEntity<Void> delete(@PathVariable {id_type} id) {{
        service.delete(id);
        return ResponseEntity.noContent().build();
    }}

    @GetMapping("/{{id}}")
    public ResponseEntity<{entity}ResponseDTO> getById(@PathVariable {id_type} id) {{
        return ResponseEntity.ok(service.getById(id));
    }}

    @GetMapping
    public ResponseEntity<PageResponse<{entity}ResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {{
        Page<{entity}ResponseDTO> p = service.getAll(PageRequest.of(page, size));
        PageResponse<{entity}ResponseDTO> resp = PageResponse.<{entity}ResponseDTO>builder()
                .data(p.getContent())
                .totalElements(p.getTotalElements())
                .totalPages(p.getTotalPages())
                .page(page)
                .size(size)
                .build();
        return ResponseEntity.ok(resp);
    }}
}}
"""

# ---------- generation ----------
def gen_entity(base_pkg, feature, entity, id_type, java_root, overwrite):
    p = java_root / feature / "entity" / f"{entity}.java"
    return write_file(p, tpl_entity(base_pkg, feature, entity, id_type), overwrite)

def gen_repository(base_pkg, feature, entity, id_type, java_root, overwrite):
    p = java_root / feature / "repository" / f"{entity}Repository.java"
    return write_file(p, tpl_repository(base_pkg, feature, entity, id_type), overwrite)

def gen_dtos(base_pkg, feature, entity, id_type, java_root, overwrite):
    c = java_root / feature / "dto" / f"{entity}CreateDTO.java"
    u = java_root / feature / "dto" / f"{entity}UpdateDTO.java"
    r = java_root / feature / "dto" / f"{entity}ResponseDTO.java"
    s = java_root / "shared" / "dto" / "PageResponse.java"
    a = write_file(c, tpl_dto_create(base_pkg, feature, entity), overwrite)
    b = write_file(u, tpl_dto_update(base_pkg, feature, entity), overwrite)
    d = write_file(r, tpl_dto_response(base_pkg, feature, entity, id_type), overwrite)
    e = write_file(s, tpl_page_response(base_pkg), overwrite)
    return a or b or d or e

def gen_service(base_pkg, feature, entity, id_type, java_root, overwrite, mapstruct):
    si = java_root / feature / "service" / f"{entity}Service.java"
    impl = java_root / feature / "service" / "impl" / f"{entity}ServiceImpl.java"
    a = write_file(si, tpl_service_interface(base_pkg, feature, entity, id_type), overwrite)
    if mapstruct:
        b = write_file(impl, tpl_service_impl_mapstruct(base_pkg, feature, entity, id_type), overwrite)
    else:
        b = write_file(impl, tpl_service_impl_manual(base_pkg, feature, entity, id_type), overwrite)
    return a or b

def gen_mapper(base_pkg, feature, entity, java_root, overwrite):
    p = java_root / feature / "mapper" / f"{entity}Mapper.java"
    return write_file(p, tpl_mapper_mapstruct(base_pkg, feature, entity), overwrite)

def gen_controller(base_pkg, feature, entity, id_type, java_root, overwrite, rest_path):
    p = java_root / feature / "controller" / f"{entity}Controller.java"
    return write_file(p, tpl_controller(base_pkg, feature, entity, id_type, rest_path), overwrite)

# ---------- main ----------
def main():
    ap = argparse.ArgumentParser(description="Spring Boot CRUD scaffolder (Python).")
    ap.add_argument("--base-package", required=True, help="e.g. com.adrian.bookstoreapi")
    ap.add_argument("--feature", required=True, help="Submódulo/feature, p.ej. users")
    ap.add_argument("--entity", required=True, help="Nombre de la entidad, p.ej. Usuario")
    ap.add_argument("--id-type", default="Long", help="Tipo de ID (default: Long)")
    ap.add_argument("--output", default=".", help="Raíz del proyecto (default: .)")
    ap.add_argument("--mapstruct", action="store_true", help="Genera mapper con MapStruct + ServiceImpl compatible")
    ap.add_argument("--overwrite", action="store_true", help="Sobrescribir archivos existentes")
    ap.add_argument("--rest-path", default=None, help="Ruta base REST (default: /api/v1/<feature>)")
    ap.add_argument("--only", default="full",
                    help="Qué generar: full | entity | repository | service | controller | dto | mapper | "
                         "múltiples separados por coma, ej: 'service,controller'")
    args = ap.parse_args()

    base_pkg = args.base_package.strip().rstrip(".")
    feature = args.feature.strip().replace("-", "_")
    entity = ucfirst(args.entity.strip())
    id_type = args.id_type.strip()
    rest_path = args.rest_path or f"/api/v1/{feature}"

    java_root = Path(args.output) / "src" / "main" / "java" / to_pkg_path(base_pkg)
    res_root  = Path(args.output) / "src" / "main" / "resources"
    res_root.mkdir(parents=True, exist_ok=True)

    # normalizamos modos
    tokens = [t.strip().lower() for t in args.only.split(",") if t.strip()]
    if "full" in tokens or not tokens:
        tokens = ["entity", "repository", "dto", "service", "controller"] + (["mapper"] if args.mapstruct else [])

    # crear lo pedido (si no existe carpeta, la crea; si archivo existe, se salta)
    changed = False
    for token in tokens:
        if token == "entity":
            changed |= gen_entity(base_pkg, feature, entity, id_type, java_root, args.overwrite)
        elif token == "repository":
            changed |= gen_repository(base_pkg, feature, entity, id_type, java_root, args.overwrite)
        elif token == "dto":
            changed |= gen_dtos(base_pkg, feature, entity, id_type, java_root, args.overwrite)
        elif token == "service":
            changed |= gen_service(base_pkg, feature, entity, id_type, java_root, args.overwrite, args.mapstruct)
        elif token == "controller":
            changed |= gen_controller(base_pkg, feature, entity, id_type, java_root, args.overwrite, rest_path)
        elif token == "mapper":
            changed |= gen_mapper(base_pkg, feature, entity, java_root, args.overwrite)
        else:
            print(f"warn: token desconocido '{token}', omitido.")

    print("✓ done." if changed else "✓ nothing to do (todo existía).", date.today())

if __name__ == "__main__":
    main()
