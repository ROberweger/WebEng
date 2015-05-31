package models;


import javax.persistence.GenerationType;

/**
 * Base entity for all JPA classes
 */

@javax.persistence.Entity
public class BaseEntity {

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    public Long getId() {
        return id;
    }

    public BaseEntity() {

    }
}
