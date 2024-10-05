package id.example.yoshida.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateResult {

	private String result;

	private String sourceEventId;

	private String sourceTopicName;

	private String templateId;

}
