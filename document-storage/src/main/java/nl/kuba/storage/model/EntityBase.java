package nl.kuba.storage.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Basic;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Entity
public abstract class EntityBase {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Basic(optional=false)
    @Column(name="id", insertable=true, updatable=false)
    protected final Long id;
    @Basic(optional=false)
    @Column(name="created", insertable=true, updatable=false)
    protected final Date created;
    @Basic(optional=false)
    @Column(name="deleted", insertable=true, updatable=true)
    protected Date deleted;

    public void delete() {
        this.deleted = new Date();
    }
}
