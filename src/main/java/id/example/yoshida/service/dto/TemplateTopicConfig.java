package id.example.yoshida.service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TemplateTopicConfig {

	private String id;

	private LocalDateTime createdTs;

	private String category;

	private String sourceTopic;

	private String targetTopic;

}
