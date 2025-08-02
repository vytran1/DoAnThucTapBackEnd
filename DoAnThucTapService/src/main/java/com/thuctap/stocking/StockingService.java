package com.thuctap.stocking;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.thuctap.common.exceptions.VariantNotFoundException;
import com.thuctap.common.product_variant.ProductVariant;
import com.thuctap.common.setting.Setting;
import com.thuctap.product_variant.ProductVariantRepository;
import com.thuctap.setting.SettingRepository;
import com.thuctap.stocking.dto.StockingInventorySearchDTO;
import com.thuctap.stocking.dto.StockingProductSearchDTO;
import com.thuctap.stocking.dto.StockingReportAggregator;
import com.thuctap.stocking.dto.StockingReportDTO;
import com.thuctap.utility.PageDTO;
import com.thuctap.utility.UtilityGlobal;

@Service
public class StockingService {
	
	@Autowired
	private StockingRepository repository;
	
	@Autowired
	private ProductVariantRepository productVariantRepository;
	
	@Autowired
	private SettingRepository settingRepository;
	
	
	public List<StockingProductSearchDTO> getStockingOfProduct(String sku) throws VariantNotFoundException{
		
		checkExistingOfProductVariant(sku);
		
		return repository.findStockingOfProduct(sku);
	}
	
	private void checkExistingOfProductVariant(String sku) throws VariantNotFoundException {
		Optional<ProductVariant> variantOPT = productVariantRepository.findBySkuCode(sku);
		
		if(variantOPT.isEmpty()) {
			throw new VariantNotFoundException("Not Exist Product Variant With The Given Sku code");
		}
		
	}
	
	
	public List<StockingInventorySearchDTO> getStockingOfInventory(Integer id){
		return repository.findStockingOfInventory(id);
	}
	
	
	public StockingReportAggregator getStockingReportOfInventory(
			Integer inventoryId, 
			int pageNum, int pageSize) {
		
		Page<StockingReportDTO> pages;
		Pageable pageable = PageRequest.of(pageNum - 1,pageSize);
		List<Setting> settings = settingRepository.findAll();
		Long totalRaw;
		BigDecimal totalValues;

		if (Objects.equals(inventoryId, null)) {
			pages = repository.getStockingReportAllWarehouses(pageable);
			totalRaw = repository.getTotalStockValueOfAllWarehouses();
		} else {
			pages = repository.getStockingReportOfInventory(inventoryId, pageable);
			totalRaw = repository.getTotalStockValueOfInventory(inventoryId);
		}
		
		totalValues = totalRaw != null ? BigDecimal.valueOf(totalRaw) : BigDecimal.ZERO;

		List<StockingReportDTO> stockings = pages.getContent();

		Integer lowStockThreshold = getLowStockThreshold(settings);

		setStockgStatus(stockings, lowStockThreshold);

		PageDTO pageDTO = UtilityGlobal.buildPageDTO("","", pages);
		
		
		StockingReportAggregator result = new StockingReportAggregator();
		setUpAttributeForReportAggregator(result, stockings, totalValues, pageDTO);
		
		return result;
		
	}
	
	
	private void setUpAttributeForReportAggregator(StockingReportAggregator result,
			List<StockingReportDTO> stockings,
			BigDecimal totalValues,
			PageDTO pageDTO
			) {
		result.setStockings(stockings);
		result.setTotalValues(totalValues);
		result.setPageDTO(pageDTO);
	}
	
	
	private Integer getLowStockThreshold(List<Setting> settings) {
		Integer lowStockThreshold = settings.stream().filter(s -> s.getKey().equals("LOW_STOCK_THRESHOLD"))
				.map(s -> Integer.valueOf(s.getValue())).findFirst().orElse(5);
		return lowStockThreshold;
	}
	
	
	private void setStockgStatus(List<StockingReportDTO> stockings,Integer lowStockThreshold) {
		for (StockingReportDTO dto : stockings) {
			boolean isLow = dto.getQuantity() < lowStockThreshold;
			dto.setLowStock(isLow);
		}
		
	}
	
	
	
	
}
