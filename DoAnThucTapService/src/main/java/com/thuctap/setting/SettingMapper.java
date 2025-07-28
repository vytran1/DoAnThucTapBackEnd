package com.thuctap.setting;

import com.thuctap.common.setting.Setting;
import com.thuctap.setting.dto.SettingDTO;

public class SettingMapper {
	
	
	public static SettingDTO toDTO(Setting setting) {
		
		SettingDTO dto = new SettingDTO();
		dto.setKey(setting.getKey());
		dto.setValue(setting.getValue());
		dto.setType(setting.getType());
		return dto;
		
	}
	
}
