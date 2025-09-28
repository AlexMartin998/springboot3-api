package com.ecommerce.api.auth.repository;

import com.ecommerce.api.auth.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


//JpaRepository ya es un Bean @Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    @Query("""
            select r
            from Role r
            where (:q is null or lower(r.name) ilike lower(concat('%', :q, '%')))
              and (:state is null or r.state = :state)
            """)
    Page<Role> findAllFiltered(@Param("q") String q,
                               @Param("state") Boolean state,
                               Pageable pageable);

}
