package com.thuctap.inventory_order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.thuctap.event.EventPublisher;
import com.thuctap.event.RabbitMQConfig;
import com.thuctap.exporting_form.ExportingFormCreatedEvent;

@Component
public class InventoryOrderEventListener {
	
	@Autowired
	private EventPublisher eventPublisher;
	
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onInventoryOrderCreated(InventoryOrderCreatedEvent event) {
        	eventPublisher.publish(RabbitMQConfig.ROUTING_ORDER_CREATED,event.getInventoryOrderId());
       
    }
	
}
