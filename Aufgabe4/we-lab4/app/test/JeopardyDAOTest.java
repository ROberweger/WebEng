package test;

import models.Answer;
import models.Category;
import models.JeopardyDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Obermair on 30.05.2015.
 */
public class JeopardyDAOTest {

    JeopardyDAO jeopardyDAO;
    EntityManager entityManager;

    @Before
    public void preparePersistenceTest() throws Exception {
        jeopardyDAO = JeopardyDAO.getInstance();
        jeopardyDAO.deleteEntites(Answer.class);
    }
    @After
    public void afterTests(){
        if(entityManager != null){
            entityManager.close();
        }
    }
    @Test
    public void testPersist() throws Exception {
        int count = jeopardyDAO.findEntities(Category.class).size();
        Category c = new Category();
        jeopardyDAO.persist(c);
        int count2 = jeopardyDAO.findEntities(Category.class).size();
        assertTrue(count == (count2 - 1));
    }

    @Test
    public void testMerge() throws Exception {
        Category c1 = new Category();
        jeopardyDAO.persist(c1);
        List<Category> c = jeopardyDAO.findEntities(Category.class);
        Category c2 = null;
        if(c != null && !c.isEmpty() &&c.get(0)!= null){
            c2 = c.get(0);
        }
        String text = "new";
        c2.setNameDE(text);
        jeopardyDAO.merge(c2);
        Category c3 = jeopardyDAO.findEntity(c2.getId(), Category.class);
        assertEquals(c3.getNameDE(), text);
    }

    @Test
    public void testFindEntity() throws Exception {
        Category category = new Category();
        category.setNameDE("PC");
        category.setNameEN("PC");
        jeopardyDAO.persist(category);
        List<Category> c = jeopardyDAO.findEntities(Category.class);
        Category c1 = null;
        Category c2 = null;
        if(c != null && !c.isEmpty() &&c.get(0)!= null){
            c1 = c.get(0);
            c2 = jeopardyDAO.findEntity(c1.getId(), Category.class);
            assertTrue(c1.getId() == c2.getId());
        }
        else{
            assertTrue(false);
        }

    }

    @Test
    public void testFindEntities() throws Exception {
        int count = jeopardyDAO.findEntities(Category.class).size();
        Category c1 = new Category();
        Category c2 = new Category();
        jeopardyDAO.persist(c1);
        jeopardyDAO.persist(c2);
        int count2 = jeopardyDAO.findEntities(Category.class).size();
        assertEquals(count, count2-2);

    }
}