package nl.kuba.storage.repository;

import nl.kuba.storage.model.Document;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DocumentRepository extends CrudRepository<Document, Long> {
    List<Document> findByName(final String name);
    List<Document> findByType(final Document.Type type);
}
