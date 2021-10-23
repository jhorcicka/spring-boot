package nl.kuba.storage.repository;

import nl.kuba.storage.model.Document;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface DocumentRepository extends CrudRepository<Document, Long> {
    /**
     * @param name
     * @return Documents matching the given name.
     */
    List<Document> findByName(final String name);

    /**
     * @param type
     * @return Documents matching the given type.
     */
    List<Document> findByType(final Document.Type type);

    /**
     * For a document we allow to update its name and notes. ID, userId, type and path (content)
     * can't be updated. It's always possible to delete the old document and save a new one.
     * @param id
     * @param name
     * @param notes
     */
    @Modifying
    @Query("update Document D set D.name = :name, D.notes = :notes where D.id = :id")
    void updateDocumentById(final Long id, final String name, final String notes);
}
