package com.thuctap.event;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String EXCHANGE_EVENTS = "app.events";
	
	public static final String ROUTING_TRANSFER_CREATED = "transfer.created";
	
	public static final String QUEUE_MAIL_TRANSFER_CREATED = "mail.transfer.created";
	
	@Bean
	public TopicExchange eventExchange() {
	    return new TopicExchange(EXCHANGE_EVENTS);
	}
	
	@Bean
	public Queue mailTransferCreatedQueue() {
		return new Queue(QUEUE_MAIL_TRANSFER_CREATED, true);
	}
	
	@Bean
    public Binding bindingMailTransferCreated() {
        return BindingBuilder.bind(mailTransferCreatedQueue())
                .to(eventExchange())
                .with(ROUTING_TRANSFER_CREATED);
    }
	
	
	
}
