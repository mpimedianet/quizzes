package com.fajar.arabicclub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fajar.arabicclub.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByDeletedFalse();

}
