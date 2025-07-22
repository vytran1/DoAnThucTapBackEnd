package com.thuctap.inventory_order;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thuctap.common.exceptions.OrderNotFoundException;
import com.thuctap.common.exceptions.ProductNotFoundException;
import com.thuctap.common.exceptions.ProductVariantSkuNotFoundException;
import com.thuctap.common.exceptions.StatusNotFoundException;
import com.thuctap.common.exceptions.SupplierNotFoundException;
import com.thuctap.inventory_order.dto.InventoryOrderOverviewDTO;
import com.thuctap.inventory_order.dto.InventoryOrderSavingRequestAggregatorDTO;
import com.thuctap.inventory_order.dto.InventoryOrderStatusDTO;
import com.thuctap.inventory_order.dto.QuoteViewResponseDTO;

@RestController
@RequestMapping("/api/orders")
public class InventoryOrderController {

	@Autowired
	private InventoryOrderService service;
	
	
	
	
	@PostMapping("")
	public ResponseEntity<?> saveOrder(@RequestBody InventoryOrderSavingRequestAggregatorDTO requestDTO){
		
			try {
				System.out.println(requestDTO.toString());
				service.saveOrder(requestDTO);
				return ResponseEntity.status(HttpStatus.CREATED).build();
			} catch (ProductVariantSkuNotFoundException e) {
				
				e.printStackTrace();
				Map<String,String> map = new HashMap<>();
				map.put("error",e.getMessage());
				map.put("sku",e.getSku());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
			} catch (StatusNotFoundException e) {
				e.printStackTrace();
				return ResponseEntity.internalServerError().build();
			}
	}
	
	
	@GetMapping("/{id}/overview")
	public ResponseEntity<?> getOverview(@PathVariable("id") Integer id){
		
		try {
			InventoryOrderOverviewDTO result = service.getOverview(id);
			return ResponseEntity.ok(result);
		} catch (OrderNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	@GetMapping("/{id}/status")
	public ResponseEntity<List<InventoryOrderStatusDTO>> getStatus(@PathVariable("id") Integer id){
		return ResponseEntity.ok(service.getStatus(id));
	}
	
	
	@PostMapping("/{id}/quote/supplier/{code}")
	public ResponseEntity<?> quotePrice(@PathVariable("id") Integer id, 
			@PathVariable("code") String code, 
			@RequestParam("file") MultipartFile file,
			@RequestHeader("X-SECRET-KEY") String secretKey){
		
		try {
			service.quotePrice(id, code,file,secretKey);
			return ResponseEntity.ok().build();
		} catch (OrderNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (SupplierNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		
	}
	
	@PostMapping("/{id}/quote/reject/supplier/{code}")
	public ResponseEntity<?> rejectQuotation(
			@PathVariable("id") Integer id,
			@PathVariable("code") String code, 
			@RequestParam("file") MultipartFile file,
			@RequestHeader("X-SECRET-KEY") String secretKey
			){
		
		try {
			service.rejectQuotation(id, code, file, secretKey);
			return ResponseEntity.ok().build();
		} 
		catch (OrderNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		catch (SupplierNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		catch (IllegalStateException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	
	@GetMapping("/{id}/quote")
	public ResponseEntity<?> getQuotationInformation(@PathVariable("id") Integer id){
		
		try {
			QuoteViewResponseDTO result = service.getQuotationInformation(id);
			return ResponseEntity.ok(result);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(e.getMessage());
		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch(IllegalArgumentException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	
	@GetMapping("/{id}/quote/accept_price")
	public ResponseEntity<?> acceptQuotationByWarehouse(@PathVariable("id") Integer id){
		
		try {
			service.acceptQuotationByWarehouse(id);
			return ResponseEntity.ok().build();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (StatusNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@PostMapping("/{id}/quote/reject_price")
	public ResponseEntity<?> rejectQuotationByWarehouse(@PathVariable("id") Integer id){
		
		try {
			service.rejectQuotationByWarehouse(id);
			return ResponseEntity.ok().build();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (StatusNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@PostMapping("/{id}/status/reject")
	public ResponseEntity<?> rejectOrder(@PathVariable("id") Integer id){
		
		try {
			InventoryOrderStatusDTO result = service.rejectOrder(id);
			return ResponseEntity.ok().body(result);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (StatusNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@PostMapping("/{id}/status/paying")
	public ResponseEntity<?> payingOrder(@PathVariable("id") Integer id){
		try {
			InventoryOrderStatusDTO result = service.updatePayingStatus(id);
			return ResponseEntity.ok().body(result);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (StatusNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	
	@PostMapping("/{id}/supplier/{code}/confirm/payed")
	public ResponseEntity<?> confirmPayingBySupplier(@PathVariable("id") Integer id, 
			@PathVariable("code") String code,
			@RequestHeader("X-SECRET-KEY") String secretKey
			){
		
				try {
					InventoryOrderStatusDTO result = service.confirmPayingBySupplier(id, code, secretKey);
					return ResponseEntity.ok().body(result);

				} catch (IllegalStateException e) {
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
				} catch (OrderNotFoundException e) {
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
				} catch (SupplierNotFoundException e) {
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
				} catch (StatusNotFoundException e) {
					e.printStackTrace();
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
				}
	}
	
	@PostMapping("/{id}/supplier/{code}/status/shipping")
	public ResponseEntity<?> updateShippingStatusBySupplier(@PathVariable("id") Integer id, @PathVariable("code") String code,
			@RequestHeader("X-SECRET-KEY") String secretKey){
		
		
		try {
			InventoryOrderStatusDTO result = service.updateShippingStatus(id, code, secretKey);
			return ResponseEntity.ok().body(result);
		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (SupplierNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (StatusNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@PostMapping("/{id}/supplier/{code}/status/arriving")
	public ResponseEntity<?> updateArrivingStatusBySupplier(
			@PathVariable("id") Integer id, 
			@PathVariable("code") String code,
			@RequestParam("file") MultipartFile file,
			@RequestHeader("X-SECRET-KEY") String secretKey
			)
	{
		
		try {
			InventoryOrderStatusDTO result = service.updateArrivingStatus(id, code, secretKey,file);
			return ResponseEntity.ok().body(result);
		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (SupplierNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (StatusNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/{id}/status/finished")
	public ResponseEntity<?> updateFinishStatusByEmployee(@PathVariable("id") Integer orderId){
		
		
		try {
			InventoryOrderStatusDTO result = service.updateFinishStatus(orderId);
			return ResponseEntity.ok().body(result);
		} catch (OrderNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (StatusNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
		
	}
	
}
