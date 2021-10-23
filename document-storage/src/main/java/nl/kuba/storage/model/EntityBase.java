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

@AllArgsConstructor
@Getter
@Entity
public abstract class EntityBase {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Basic(optional=false)
    @Column(updatable=false)
    protected final Long id;

    @Basic(optional=false)
    @Column(updatable=false)
    protected final Date created;

    @Column()
    protected Date deleted;
}
