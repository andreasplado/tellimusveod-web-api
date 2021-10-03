package com.tellimusveod.webapi.respository;

import com.tellimusveod.webapi.dao.entity.JobCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<JobCategoryEntity, Integer> {
}
