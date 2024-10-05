package id.example.yoshida.model.repository;

import id.example.yoshida.model.TemplateTopicConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateTopicConfigRepository extends JpaRepository<TemplateTopicConfig, String> {

}
