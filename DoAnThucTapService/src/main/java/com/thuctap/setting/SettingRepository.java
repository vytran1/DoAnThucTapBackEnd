package com.thuctap.setting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.setting.Setting;

public interface SettingRepository extends JpaRepository<Setting,Integer> {
	
	
	
	@Query("""
			SELECT s FROM 
			Setting s 
			WHERE s.type = ?1
			""")
	public List<Setting> findByType(String type);
	
}
