package com.thuctap.exporting_form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.thuctap.event.EventPublisher;
import com.thuctap.event.RabbitMQConfig;

@Component
public class ExportingFormEventListener {
	
	@Autowired
	private EventPublisher eventPublisher;
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onExportingFormCreated(ExportingFormCreatedEvent event) {
        	eventPublisher.publish(RabbitMQConfig.ROUTING_TRANSFER_CREATED, event.getExportingFormId());
       
    }
}
