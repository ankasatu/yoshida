package id.example.yoshida.service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TemplateConfig {

	private String id;

	private LocalDateTime createdTs;

	private LocalDateTime updatedTs;

	private String name;

	private String source;

	private TemplateTopicConfig topicConfig;

}
