package models;



/**
 * Base entity for all JPA classes
 */
@javax.persistence.NamedQueries({
        @javax.persistence.NamedQuery(name = "findAll", query = "SELECT s FROM BaseEntity s"),
        @javax.persistence.NamedQuery(name = "findById", query = "SELECT s FROM BaseEntity s WHERE s.id = :id")
})
@javax.persistence.Entity
public class BaseEntity {

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    protected Long id;

    public Long getId() {
        return id;
    }

    public BaseEntity() {

    }
}
