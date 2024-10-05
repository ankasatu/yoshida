package id.example.yoshida.service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TemplateConfigItem {

	private String id;

	private LocalDateTime createdTs;

	private LocalDateTime updatedTs;

	private String name;

}
