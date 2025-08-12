package com.thuctap.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {
	
	 private final RabbitTemplate rabbitTemplate;
	
	 public EventPublisher(RabbitTemplate rabbitTemplate) {
	        this.rabbitTemplate = rabbitTemplate;
	 }
	 
	 public void publish(String routingKey, Object message) {
	        rabbitTemplate.convertAndSend(
	                RabbitMQConfig.EXCHANGE_EVENTS,
	                routingKey,
	                message
	        );
	 }
	 
}
