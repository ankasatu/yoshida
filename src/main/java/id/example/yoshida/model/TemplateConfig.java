package id.example.yoshida.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "template_config", indexes = { @Index(name = "template_config_ix", columnList = "name") })
@Entity
public class TemplateConfig {

	@Id
	@UuidGenerator
	@Column(name = "id", length = 36, nullable = false)
	private String id;

	@Column(name = "created_ts", updatable = false)
	private LocalDateTime createdTs;

	@Column(name = "updated_ts")
	private LocalDateTime updatedTs;

	@Column(name = "name")
	private String name;

	@Column(name = "source", columnDefinition = "text")
	private String source;

	@JsonIgnore
	@OneToOne(mappedBy = "template")
	@ToString.Exclude
	private TemplateTopicConfig topicConfig;

	@PrePersist
	public void prePersist() {
		setCreatedTs(LocalDateTime.now());
		setUpdatedTs(getCreatedTs());
	}

	@PreUpdate
	public void preUpdate() {
		setUpdatedTs(LocalDateTime.now());
	}

}
