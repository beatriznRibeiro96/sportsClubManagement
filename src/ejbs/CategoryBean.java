package ejbs;

import entities.Category;
import entities.Product;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Singleton
public class CategoryBean {

    @PersistenceContext
    EntityManager em;

    @EJB
    ProductBean productBean;

    public Category create(String description) {
        try {
            Category category = new Category(description);

            em.persist(category);

            return category;
        } catch (Exception e) {
            throw new EJBException("ERROR_CREATING_CATEGORY", e);
        }
    }

    public List<Category> all() {
        try{
            List<Category> categories = (List<Category>) em.createNamedQuery("getAllCategories").getResultList();
            return categories;
        }catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_CATEGORIES", e);
        }
    }

    public List<Category> getValidCategories() {
        try {
            return (List<Category>) em.createNamedQuery("getValidCategories").getResultList();
        }catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_VALID_CATEGORIES", e);
        }
    }

    public Category find(int id) {
        try{
            return em.find(Category.class, id);
        }catch (Exception e) {
            throw new EJBException("ERROR_FINDING_CATEGORY", e);
        }
    }

    public Category update(int idFromCategoryToUpdate, String description) {
        try{
            Category category = find(idFromCategoryToUpdate);

            category.setDescription(description);

            em.persist(category);

            return category;
        }catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_CATEGORY", e);
        }
    }

    public Category delete(int id) {
        try{
            Category category = find(id);

            if(category.getProducts().size() > 0) {
                category.setInvalid(true);

                em.persist(category);
                return category;
            }

            em.remove(category);
            return null;
        }catch (Exception e) {
            throw new EJBException("ERROR_DELETING_CATEGORY", e);
        }
    }

    public void setProductInCategory(int productID, int categoryID) {
        try {
            Category category = find(categoryID);
            Product product = productBean.find(productID);

            category.addProduct(product);
        } catch (Exception e) {
            throw new EJBException("ERROR_ADDING_PRODUCT_IN_CATEGORY", e);
        }
    }

    public void unsetProductInCategory(int productID, int categoryID) {
        try {
            Category category = find(categoryID);
            Product product = productBean.find(productID);

            category.removeProduct(product);
        } catch (Exception e) {
            throw new EJBException("ERROR_REMOVING_PRODUCT_IN_CATEGORY", e);
        }
    }
}
