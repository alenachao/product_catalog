package com.example.catalog.kafka

import org.springframework.stereotype.Service
import org.springframework.kafka.core.KafkaTemplate

@Service
class KafkaProducer(val kafkaTemplate: KafkaTemplate<String, String>) {
fun sendProductUpdate(message: String) {
        kafkaTemplate.send(AppConstants.TOPIC_NAME,message)
    }
}