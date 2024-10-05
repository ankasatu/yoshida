package id.example.yoshida.model.repository;

import id.example.yoshida.model.TemplateConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateConfigRepository extends JpaRepository<TemplateConfig, String> {

	Optional<TemplateConfig> findByIdOrName(String id, String name);

}
