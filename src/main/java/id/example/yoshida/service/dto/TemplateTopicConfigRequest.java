package id.example.yoshida.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateTopicConfigRequest {

	private String category;

	private String sourceTopic;

	private String targetTopic;

}
