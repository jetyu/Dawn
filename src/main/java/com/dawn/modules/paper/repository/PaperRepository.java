package com.dawn.modules.paper.repository;

import com.dawn.modules.paper.model.Paper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperRepository extends JpaRepository<Paper, String> {
}
