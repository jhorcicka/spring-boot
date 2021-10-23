package nl.kuba.storage.repository;

import nl.kuba.storage.model.Document;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface DocumentRepository extends CrudRepository<Document, Long> {

    /**
     * @param userId
     * @return Documents matching given userId.
     */
    List<Document> findByUserId(final Long userId);

    /**
     * @param userId
     * @param documentId
     * @return Document matching given userId and documentId.
     */
    List<Document> findByUserIdAndId(final Long userId, final Long documentId);

    /**
     * @param userId
     * @param name
     * @return Documents matching the given userId and name.
     */
    List<Document> findByUserIdAndName(final Long userId, final String name);

    /**
     * For a document we allow to update its name, path and notes. ID, userId and type can't be updated.
     * It's always possible to delete the old document and save a new one.
     * @param userId
     * @param documentId
     * @param name
     * @param notes
     */
    @Transactional
    @Modifying
    @Query("update Document D set "
        + "D.name = :name, D.relativeFilePath = :relativeFilePath, D.notes = :notes "
        + "where D.userId = :userId and D.id = :documentId")
    void updateUserDocument(
        final Long userId,
        final Long documentId,
        final String name,
        final String relativeFilePath,
        final String notes);
}
