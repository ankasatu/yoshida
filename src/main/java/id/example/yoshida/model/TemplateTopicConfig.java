package id.example.yoshida.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "template_topic_config",
		indexes = { @Index(name = "template_topic_config_ix", columnList = "category, source_topic, target_topic",
				unique = true) })
@Entity
public class TemplateTopicConfig {

	@Id
	@UuidGenerator
	@Column(name = "id", length = 36, nullable = false)
	private String id;

	@Column(name = "created_ts", updatable = false)
	private LocalDateTime createdTs;

	@Column(name = "category")
	private String category;

	@Column(name = "source_name")
	private String sourceTopic;

	@Column(name = "target_name")
	private String targetTopic;

	@Column(name = "template_config_id", insertable = false, updatable = false)
	private String templateConfigId;

	@OneToOne(fetch = LAZY)
	@JoinColumn(name = "template_config_id", foreignKey = @ForeignKey(name = "template_topic_config_fk"))
	@ToString.Exclude
	private TemplateConfig template;

	@PrePersist
	public void prePersist() {
		setCreatedTs(LocalDateTime.now());
	}

}
