package nl.kuba.storage.model;

import java.util.Date;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
@Entity
@Table(name = "Document")
@SQLDelete(sql = "update Document D set D.deleted = CURRENT_TIMESTAMP where D.id = ?1")
@Where(clause = "deleted is null")
public class Document {
    /*
    Assumption:
    The actual values do not change, and we know them from the beginning.
    If in reality this list changes frequently, an entity (database table) would probably be better.
    */
    public enum Type {
        OTHER,
        PASSPORT,
        IDENTITY_CARD,
        DRIVING_LICENSE,
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Basic(optional=false)
    @Column(updatable=false)
    private Long id;

    @Basic(optional=false)
    @Column(updatable=false)
    private Date created;

    private Date deleted;

    private Long userId;
    private String name;
    private String relativeFilePath;
    private Type type;
    private String notes;
}
