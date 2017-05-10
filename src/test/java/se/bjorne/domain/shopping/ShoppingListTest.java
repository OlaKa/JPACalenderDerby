package se.bjorne.domain.shopping;

import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


/**
 * Enkla tester.
 * Vi gör inga rena enhetstester utan testar bara JPA-funktionaliteten.
 */
public class ShoppingListTest {
    EntityManager manager;

    @Before
    public void setUp() throws IOException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
        manager = factory.createEntityManager();
        clear();
    }

    @Test
    public void testNoShoppinglists() throws Exception {
        List<ShoppingList> shoppingLists = getAllShoppingLists();
        assertTrue(shoppingLists.isEmpty());
    }

    @Test
    public void testEmptyShoppinglist() throws Exception {
        ShoppingList shoppingList = new ShoppingList("Lista 1");
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            manager.persist(shoppingList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();
        List<ShoppingList> shoppingLists = getAllShoppingLists();
        assertEquals(1L, shoppingLists.size());
    }

    @Test
    public void testAddingAnItem() throws Exception {
        ShoppingList shoppingList = new ShoppingList("Lista 2");
        shoppingList.getItems().add(new Item("item 1", shoppingList));
        EntityTransaction tx = manager.getTransaction();
        tx.begin();
        try {
            manager.persist(shoppingList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();
        List<ShoppingList> shoppingLists = getAllShoppingLists();
        assertEquals(1L, shoppingLists.size());
        assertEquals(1L, (shoppingLists.get(0)).getItems().size());

    }

    private List<ShoppingList> getAllShoppingLists() {
        Query query = manager.createQuery("SELECT shoplist FROM ShoppingList shoplist", ShoppingList.class);
        return query .getResultList();
    }

    private void clear() {
        clearShoppingList();
    }

    private void clearShoppingList() {
        EntityTransaction tx = manager.getTransaction();
        List<ShoppingList> resultList = getAllShoppingLists();
        tx.begin();
        try {
            for (ShoppingList next : resultList) {
                manager.remove(next);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        tx.commit();
    }
}