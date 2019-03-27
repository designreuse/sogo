package com.yihexinda.nodeweb.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.yihexinda.data.dto.OrderDto;

@Configuration
@EnableKafka
public class KafkaProducerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  public Map<String, Object> producerConfigs() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

    return props;
  }

  @Bean
  public ProducerFactory<String, OrderDto> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  @Bean
  public KafkaTemplate<String, OrderDto> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

//  @Bean
//  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>>
//  kafkaListenerContainerFactory() {
//    ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
//            new ConcurrentKafkaListenerContainerFactory<>();
//    factory.setConsumerFactory(consumerFactory());
//    factory.setConcurrency(3);
//    factory.getContainerProperties().setPollTimeout(3000);
//    return factory;
//  }
//
//  @Bean
//  public ConsumerFactory<Integer, String> consumerFactory() {
//    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
//  }
//
//  @Bean
//  public Map<String, Object> consumerConfigs() {
//    Map<String, Object> props = new HashMap<>();
//    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//    return props;
//  }
}