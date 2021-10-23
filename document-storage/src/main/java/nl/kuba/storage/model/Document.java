package nl.kuba.storage.model;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Entity;

@Getter
@Entity
public class Document extends EntityBase {
    /*
    Assumption:
    The actual values do not change and we know them from the beginning.
    If in reality this list changes frequently, an entity (database table) would probably be better.
    */
    public enum Type {
        OTHER,
        PASSPORT,
        IDENTITY_CARD,
        DRIVING_LICENSE,
    }

    private Long userId;
    private String name;
    private String relativeFilePath;
    private Type type;
    private String notes;

    public Document() {
        super(0L, new Date(), null);
    }

    @Builder
    public Document(final Long id, final Date created, final Date deleted, final Long userId, final String name,
            final String relativeFilePath, final Type type, final String notes) {
        super(id == null ? 0L : id, created == null ? new Date() : created, deleted);
        this.userId = userId;
        this.name = name;
        this.relativeFilePath = relativeFilePath;
        this.type = type;
        this.notes = notes;
    }
}
