package com.thuctap.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.category.dto.CategoryDropDownList;
import com.thuctap.common.category.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

	@Query("SELECT new com.thuctap.category.dto.CategoryDropDownList(c.id,c.name) FROM Category c")
	public List<CategoryDropDownList> findForDropDownList();
	
}
