package com.test.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.test.model.Fair;

public interface FairRepository extends JpaRepository<Fair, Long>{
	
	Page<Fair> findByFnameContaining(String keyword, Pageable pageable);

}
