package models;

import play.db.jpa.JPA;
import javax.persistence.*;

import javax.persistence.TypedQuery;
import java.util.List;


/**
 * Provides Data Access methods for JPA
 */
public class JeopardyDAO implements IGameDAO {

    public static final JeopardyDAO INSTANCE = new JeopardyDAO();

    private JeopardyDAO() { }

    /**
     * Get a given quiz user based on the id
     *
     * @param categoryClass
     * @param id
     * @return
     */
    public JeopardyUser findById(Class<Category> categoryClass, long id) {
        return em().find(JeopardyUser.class, id);
    }

    public JeopardyUser findByUserName(String name) {
        if (name != null && !name.isEmpty()) {
            return getByUserName(name);
        } else {
            return null;
        }
    }

    /**
     * Get a given quiz user based on the name
     * @param name
     * @return
     */
    private JeopardyUser getByUserName(String name) {
        String queryStr = "from JeopardyUser where userName = :userName";
        TypedQuery<JeopardyUser> query = em().createQuery(queryStr,
                JeopardyUser.class).setParameter("userName", name);
        List<JeopardyUser> list = query.getResultList();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }


    /**
     * Save an entity. Throws an error if an entity with the given id already exists
     * @param entity
     * @return
     */
    @Override
    public void persist(BaseEntity entity) {
        em().persist(entity);
    }


    /**
     * If no entity with the given id exists in the DB, a new entity is stored. If there is already
     * an entity with the given id, the entity is updated
     * @param entity
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseEntity> T merge(T entity) {
        return em().merge(entity);
    }

    /**
     * Get an entity of the given type using the id
     * @param id
     * @param entityClazz
     * @param <T>
     * @return
     */
    @Override
    public <T extends BaseEntity> T findEntity(Long id, Class<T> entityClazz) {
        return em().find(entityClazz, id);
    }
    /**
     * Get a list of all entities of a certain type
     *
     * @param entityClazz
     * @param <E>
     * @return
     */
    @Override
    public <E extends BaseEntity> List<E> findEntities(Class<E> entityClazz) {
        List<E> listAll = em().createNamedQuery(entityClazz.getSimpleName()+".findAll", entityClazz).getResultList();
        return listAll;
    }


    public void remove(BaseEntity entity) {
        em().remove(entity);
    }

    /**
     * Get the entity manager
     * @return
     */
    private static EntityManager em() {
        return JPA.em();
    }

}
