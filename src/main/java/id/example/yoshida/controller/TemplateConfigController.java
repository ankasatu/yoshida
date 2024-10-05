package id.example.yoshida.controller;

import id.example.yoshida.service.TemplateConfigService;
import id.example.yoshida.service.TemplateTopicService;
import id.example.yoshida.service.dto.TemplateConfig;
import id.example.yoshida.service.dto.TemplateConfigItem;
import id.example.yoshida.service.dto.TemplateConfigRequest;
import id.example.yoshida.service.dto.TemplateTopicConfigRequest;
import id.example.yoshida.utils.CommonUtils;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("template/config")
@RequiredArgsConstructor
public class TemplateConfigController {

	private final TemplateConfigService templateConfigService;

	private final TemplateTopicService templateTopicService;

	private final CommonUtils commonUtils;

	@GetMapping("list")
	public ResponseEntity<List<TemplateConfigItem>> getList(@RequestParam(required = false) String query) {
		var result = templateConfigService.getAll(query);
		return ResponseEntity.ok(result);
	}

	@GetMapping("{configId}")
	public ResponseEntity<TemplateConfig> getDetail(@PathVariable String configId) {
		var result = templateConfigService.getDetail(configId);
		return ResponseEntity.ok(result);
	}

	@PostMapping("save")
	public ResponseEntity<Object> update(@Valid @RequestBody TemplateConfigRequest request) {
		templateConfigService.save(request);
		return ResponseEntity.accepted().build();
	}

	@DeleteMapping("{configId}/delete")
	public ResponseEntity<Object> delete(@PathVariable String configId) {
		templateConfigService.delete(configId);
		return ResponseEntity.accepted().build();
	}

	@PostMapping("{configId}/topic/attach")
	public ResponseEntity<TemplateConfig> attachTopic(@PathVariable String configId,
			@Valid @RequestBody TemplateTopicConfigRequest request) {
		templateTopicService.attach(configId, request);
		return ResponseEntity.accepted().build();
	}

	@PostMapping("{configId}/topic/detach")
	public ResponseEntity<TemplateConfig> detachTopic(@PathVariable String configId) {
		templateTopicService.detach(configId);
		return ResponseEntity.accepted().build();
	}

	@PostMapping("playground")
	public ResponseEntity<String> playground(@RequestBody PlaygroundRequest request) {
		return ResponseEntity.ok(templateConfigService.render(request.getName(), request.getData()));
	}

	@Data
	public static class PlaygroundRequest {

		private String name;

		private LinkedHashMap<?, ?> data;

	}

}
