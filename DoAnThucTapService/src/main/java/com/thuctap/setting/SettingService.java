package com.thuctap.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thuctap.common.setting.Setting;
import com.thuctap.setting.dto.SettingDTO;

@Service
public class SettingService {

	
	@Autowired
	private SettingRepository settingRepository;
	
	
	public List<SettingDTO> getALL(){
		return settingRepository.findAll().stream().map(SettingMapper::toDTO).toList();
	}
	
}
