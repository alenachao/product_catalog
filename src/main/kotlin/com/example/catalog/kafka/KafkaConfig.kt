package com.example.catalog.kafka

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.apache.kafka.clients.admin.NewTopic


@Configuration
class KafkaConfig {
    @Bean
    fun topicCreate(): NewTopic {
        return TopicBuilder.name(AppConstants.TOPIC_NAME).build()
    }
}