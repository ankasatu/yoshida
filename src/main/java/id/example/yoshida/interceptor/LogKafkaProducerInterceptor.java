package id.example.yoshida.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;
import java.util.Objects;

@Slf4j
public class LogKafkaProducerInterceptor implements ProducerInterceptor<String, String> {

	@Override
	public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
		log.info("begin publish to topic: {}. partition: {}, header: {}, value: {}", producerRecord.topic(),
				producerRecord.partition(), producerRecord.headers(), producerRecord.value());
		return producerRecord;
	}

	@Override
	public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
		boolean success = Objects.isNull(e);

		if (success) {
			log.info("success publish to topic: {}", recordMetadata.topic());
		}
		else {
			log.error("failed publish to topic: {}", recordMetadata.topic(), e);
		}
	}

	@Override
	public void close() {
		// nothing
	}

	@Override
	public void configure(Map<String, ?> map) {
		// nothing
	}

}
