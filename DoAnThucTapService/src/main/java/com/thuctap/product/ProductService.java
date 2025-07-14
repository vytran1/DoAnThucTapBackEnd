package com.thuctap.product;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.thuctap.brand_category.BrandCategoryRepository;
import com.thuctap.common.brandcategory.BrandCategory;
import com.thuctap.common.exceptions.ProductNotFoundException;
import com.thuctap.common.product.Product;
import com.thuctap.common.product_attribute.ProductAttribute;
import com.thuctap.common.product_attribute_value.ProductAttributeValue;
import com.thuctap.common.product_image.ProductImage;
import com.thuctap.common.product_variant.ProductVariant;
import com.thuctap.product.dto.ProductDetailForVariantAggregateDTO;
import com.thuctap.product.dto.ProductFindAllDTO;
import com.thuctap.product.dto.ProductFindAllDTOList;
import com.thuctap.product.dto.ProductImageAggregatorDTO;
import com.thuctap.product.dto.ProductOverviewDTO;
import com.thuctap.product.dto.ProductSaveInformationDTO;
import com.thuctap.product_attribute.ProductAttributeRepository;
import com.thuctap.product_attribute.dto.ProductAttributeForProductDetailDTO;
import com.thuctap.product_attribute.dto.ProductAttributesDTO;
import com.thuctap.product_attribute_value.ProductAttributeValueRepository;
import com.thuctap.product_image.ProductImageRepository;
import com.thuctap.product_image.dto.ProductImageDTO;
import com.thuctap.product_variant.ProductVariantRepository;
import com.thuctap.product_variant.dto.ProductVariantDTO;
import com.thuctap.product_variant.dto.ProductVariantDetailDTO;
import com.thuctap.utility.PageDTO;
import com.thuctap.utility.UtilityGlobal;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private BrandCategoryRepository brandCategoryRepository;
	
	@Autowired
	private ProductAttributeRepository productAttributeRepository;
	
	@Autowired
	private ProductVariantRepository productVariantRepository;
	
	@Autowired
	private ProductAttributeValueRepository productAttributeValueRepository;
	
	@Autowired
	private ProductImageRepository productImageRepository;
	
	
	@Transactional
	public void saveProduct(ProductSaveInformationDTO dto) {
		
		Product savedProduct = saveBasicInformation(dto);
		
		
		
		
		Map<String,ProductAttribute> attributeNameToEntity = saveProductAttributes(savedProduct, dto);
		
		saveProductVariantAndProductVariantValue(dto, savedProduct, attributeNameToEntity);
	}
	
	
	public ProductFindAllDTOList getByPage(Integer pageNum, Integer pageSize, String sortField, String sortDir) {
		
		Pageable pageable = UtilityGlobal.setUpPageRequest(pageNum, pageSize, sortField, sortDir);
		
		
		Page<ProductFindAllDTO> pages = productRepository.findAllWithSkuCode(pageable);
		
		ProductFindAllDTOList list = setUpResult(pages, sortField, sortDir);
		
		return list;
		
	}
	
	
	@Transactional
	public String changeMainImage(Integer id, MultipartFile newImage) {
		String basePath = "C:/DoAnThucTapImages/Product/" + id;
		
		File productFolder = getProductImageFolder(basePath);
		
		
		deleteOldImage(productFolder);
		
		String newFileName = getNewImageName(newImage,"main");
		
		File destFile = new File(productFolder, newFileName);
		
		try {
		       
		        newImage.transferTo(destFile);
		} catch (IOException e) {
		        throw new RuntimeException("Error Changing Main Image", e);
		}
		
		productRepository.updateProductMainImage(id, newFileName);
		
		return newFileName;
	}
	
	public ProductImageDTO addSubImage(Integer id, MultipartFile newImage) {
		String basePath = "C:/DoAnThucTapImages/Product/" + id;
		
		File productFolder = getProductImageFolder(basePath);
		
		String newFileName = getNewImageName(newImage,"sub");
		
		File destFile = new File(productFolder, newFileName);
		
		try {
	        newImage.transferTo(destFile);
		} catch (IOException e) {
	        throw new RuntimeException("Error Changing Main Image", e);
		}
		
		ProductImage productImage = new ProductImage();
		productImage.setName(newFileName);
		productImage.setProduct(new Product(id));
		
		
		ProductImage savedProductImage = productImageRepository.save(productImage);
		
		ProductImageDTO productImageDTO = new ProductImageDTO(savedProductImage.getId(),savedProductImage.getName());
		
		return productImageDTO;
		
	}
	
	
	public void deleteSubImage(Integer subImageId) {
		productImageRepository.deleteById(subImageId);
	}
	
	
	
	public ProductImageAggregatorDTO getAllImageInformation(Integer id) {
		
		String mainImage = productRepository.findMainImageByProductId(id);
		
		List<ProductImageDTO> subImages = productImageRepository.findAllSubImagesByProductId(id);
		
		ProductImageAggregatorDTO dto = new ProductImageAggregatorDTO();
		
		dto.setMainImage(mainImage);
		dto.setSubImages(subImages);
		
		return dto;
	}
	
	
	
	
	private File getProductImageFolder(String basePath) {
		File productFolder = new File(basePath);
		
		if(!productFolder.exists()) {
			productFolder.mkdir();
		}
		
		return productFolder;
	}
	
	private String getNewImageName(MultipartFile newImage,String prefix) {
		 String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		 String originalFilename = newImage.getOriginalFilename();
		 
		 String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		 String newFileName = prefix + timestamp + extension;
		 
		 
		 return newFileName;
	}
	
	private void deleteOldImage(File productFolder) {
		 File[] files = productFolder.listFiles((dir, name) -> name.startsWith("main_"));
		    if (files != null) {
		        for (File file : files) {
		            file.delete();
		        }
		 }
	}
	
	
	public ProductOverviewDTO getProductById(Integer id) throws ProductNotFoundException {
		
		Optional<ProductOverviewDTO> productOPT = productRepository.findProductById(id);
		
		checkProductIsExist(productOPT);
		
		return productOPT.get();
		
	}
	
	
	
	
	
	public ProductDetailForVariantAggregateDTO getVariantDetailByProductId(Integer productId) {
		
		List<ProductVariantDetailDTO> variants = productVariantRepository.findAllVariantByProductId(productId);
		
		List<ProductAttributeForProductDetailDTO> attributes = productAttributeRepository.findAllAttributeByProductId(productId);
		
		ProductDetailForVariantAggregateDTO aggregator = new ProductDetailForVariantAggregateDTO();
		
		aggregator.setAttribute(attributes);
		aggregator.setVariants(variants);
		
		return aggregator;
		
	}
	
	
	private void checkProductIsExist(Optional<ProductOverviewDTO> productOPT) throws ProductNotFoundException {
		if(productOPT.isEmpty()) {
			throw new ProductNotFoundException("Product Is Not Exist In System");
		}
	}
	
	
	private ProductFindAllDTOList setUpResult(Page<ProductFindAllDTO> pages,String sortField, String sortDir) {
		ProductFindAllDTOList list = new ProductFindAllDTOList();
		list.setProducts(pages.getContent());
		
		PageDTO pageInfo = new PageDTO();
		pageInfo.setPageNum(pages.getNumber() + 1);
		pageInfo.setPageSize(pages.getSize());
		pageInfo.setSortField(sortField);
		pageInfo.setSortDir(sortDir);
		pageInfo.setReverseDir(sortDir.equals("asc") ? "desc" : "asc");
		pageInfo.setTotalPages(pages.getTotalPages());
		pageInfo.setTotalItems(pages.getTotalElements());
		
		list.setPage(pageInfo);
		
		return list;
	}
	
	
	
	
	private Product saveBasicInformation(ProductSaveInformationDTO dto) {
		Integer brandId = dto.getBrand();
		
		Integer categoryId = dto.getCategory();
		
		BrandCategory brandCategory = brandCategoryRepository.findByBrandIdAndCategoryId(brandId, categoryId);
		
		Product product = new Product();
		product.setName(dto.getProductName());
		product.setBrandCategory(brandCategory);
		product.setDescription("Default Description");
		Product savedProduct = productRepository.save(product);
		
		return savedProduct;
	}
	
	private Map<String,ProductAttribute> saveProductAttributes(Product savedProduct,ProductSaveInformationDTO dto){
		List<ProductAttributesDTO> attributeDTOs = dto.getAttributes();
		
		Map<String,ProductAttribute> attributeNameToEntity = new HashMap<>();
		int position = 0;
		
		
		for(ProductAttributesDTO attributeDTO : attributeDTOs) {
			
			
			ProductAttribute productAttribute = new ProductAttribute();
			productAttribute.setName(attributeDTO.getName());
			productAttribute.setProduct(savedProduct);
			productAttribute.setPosition(position++);
			ProductAttribute savedProductAttribute = productAttributeRepository.save(productAttribute);
			
			attributeNameToEntity.put(savedProductAttribute.getName(), savedProductAttribute);
			
		}
		
		return attributeNameToEntity;
	}
	
	private void saveProductVariantAndProductVariantValue(ProductSaveInformationDTO dto,Product savedProduct,Map<String,ProductAttribute> attributeNameToEntity) {
		List<ProductVariantDTO> productVariantDTOs = dto.getVariants();
		
		int first = 0;
		
		for(ProductVariantDTO variantDTO : productVariantDTOs) {
			
			ProductVariant productVariant = new ProductVariant();
			
			productVariant.setNameOverride(variantDTO.getName());
			productVariant.setDescriptionOverride(savedProduct.getDescription());
			productVariant.setSku(variantDTO.getSku());
			productVariant.setPrice(dto.getBasePrice());
			productVariant.setProduct(savedProduct);
			
			if(first == 0) {
				productVariant.setIsDefault(true);
				first++;
			}
			
			ProductVariant savedProductVariant = productVariantRepository.save(productVariant);
			
			String[] parts = variantDTO.getName().split("/");
		    List<String> attrValues = Arrays.asList(parts).subList(1, parts.length); 
		    
		    int i = 0;
		    for(ProductAttributesDTO attributeDTO : dto.getAttributes()) {
		    	
		    	String attributeName = attributeDTO.getName();
		    	String value = attrValues.get(i++);
		    	
		    	ProductAttributeValue productAttributeValue = new ProductAttributeValue();
		    	productAttributeValue.setProductAttribute(attributeNameToEntity.get(attributeName));
		    	productAttributeValue.setProductVariant(savedProductVariant);
		    	productAttributeValue.setValue(value);
		    	
		    	
		    	
		    	productAttributeValueRepository.save(productAttributeValue);
		    	
		    	
		    }
			
		}
	}
}
