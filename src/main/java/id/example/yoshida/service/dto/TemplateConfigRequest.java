package id.example.yoshida.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateConfigRequest {

	private String id;

	private String name;

	private String source;

}
