package com.aguevara.difftool.repositories;

import com.aguevara.difftool.entities.DiffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiffRepository extends JpaRepository<DiffEntity, Long> {

    Optional<DiffEntity> findBydiffid(Long diffid);
}
