package com.sqlmurdermystery.casecontent.repository;

import com.sqlmurdermystery.casecontent.model.CaseFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaseFileRepository extends JpaRepository<CaseFile, Long> {
    List<CaseFile> findAllByOrderByDisplayOrderAsc();
}
