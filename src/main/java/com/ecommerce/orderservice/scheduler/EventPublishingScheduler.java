package com.ecommerce.orderservice.scheduler;

import com.ecommerce.orderservice.entity.OutboxEventEntity;
import com.ecommerce.orderservice.producer.EventProducer;
import com.ecommerce.orderservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@Configuration
public class EventPublishingScheduler {

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private EventProducer eventProducer;

    @Scheduled(fixedRate = 20000)
    @Transactional
    public void publishEvent(){
        List<OutboxEventEntity> eventList =  outboxEventRepository.findTop10ByIsProcessedFalseOrderByCreatedAtAsc();

        try {
            if(eventList.size()>0){
                log.info(objectMapper.writeValueAsString(eventList));
                for(OutboxEventEntity event: eventList){
                    eventProducer.publishEvent(event);
                    event.setProcessed(true);
                }
            }else
                log.info("No entry yet");
        } catch (JsonProcessingException e) {

           log.info(e.getMessage());
        }

        outboxEventRepository.saveAll(eventList);

    }
}
