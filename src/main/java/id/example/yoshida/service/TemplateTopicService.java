package id.example.yoshida.service;

import id.example.yoshida.model.TemplateTopicConfig;
import id.example.yoshida.service.dto.TemplateTopicConfigRequest;
import org.springframework.kafka.support.SendResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TemplateTopicService {

	void attach(String templateConfigId, TemplateTopicConfigRequest request);

	void detach(String templateConfigId);

	List<TemplateTopicConfig> getAllConfig();

	CompletableFuture<SendResult<String, String>> send(Object data, String topicName);

}
