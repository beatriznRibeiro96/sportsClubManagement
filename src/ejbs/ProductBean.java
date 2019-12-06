package ejbs;

import dtos.ProductDTO;
import entities.Category;
import entities.Product;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ProductBean {

    @PersistenceContext
    EntityManager em;

    @EJB
    CategoryBean categoryBean;

    public Product create(String description, double price, Category category) {
        try {
            Product product = new Product(description, price, category);

            em.persist(product);

            return product;
        } catch (Exception e) {
            throw new EJBException("ERROR_CREATING_PRODUCT", e);
        }
    }

    public List<ProductDTO> all() {
        try{
            List<Product> products = (List<Product>) em.createNamedQuery("getAllProducts").getResultList();
            List<ProductDTO> productDTOs = new ArrayList<>();

            for (Product product : products) {
                ProductDTO productDTO = new ProductDTO(
                        product.getId(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCategory().getId(),
                        product.getCategory().getDescription(),
                        product.isInvalid(),
                        product.getReplaces() == null ? -1 : product.getReplaces().getId()
                );

                for (Product child : product.getReplacedBy()) {
                    productDTO.getReplacedBy().add(new ProductDTO(
                            child.getId(),
                            child.getDescription(),
                            child.getPrice(),
                            child.getCategory().getId(),
                            child.getCategory().getDescription(),
                            child.isInvalid(),
                            child.getReplaces() == null ? -1 : child.getReplaces().getId()
                        ));
                }

                productDTOs.add(productDTO);
            }

            return productDTOs;
        }catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_PRODUCTS", e);
        }
    }

    public List<Product> getValidProducts() {
        try {
            return (List<Product>) em.createNamedQuery("getValidProducts").getResultList();
        }catch (Exception e) {
            throw new EJBException("ERROR_RETRIEVING_VALID_PRODUCTS", e);
        }
    }

    public Product find(int id) {
        try{
            return em.find(Product.class, id);
        }catch (Exception e) {
            throw new EJBException("ERROR_FINDING_PRODUCT", e);
        }
    }

    public Product update(int idFromProductToUpdate, String description, double price, Category category) {
        try {
            Product productToUpdate = find(idFromProductToUpdate);

            Product product = create(description, price, category);
            if(productToUpdate.getReplaces() != null) { //Verifica se este produto já é substituto de outro
                product.setReplaces(productToUpdate.getReplaces());
                setProductInReplacedBy(product.getId(), productToUpdate.getReplaces().getId());
            } else {
                product.setReplaces(productToUpdate);
                setProductInReplacedBy(product.getId(), productToUpdate.getId());
            }

            productToUpdate.setInvalid(true); //Insere produto com inválido
            categoryBean.unsetProductInCategory(productToUpdate.getId(), category.getId()); //Remove produto da lista produtos da categoria

            em.persist(product);
            em.persist(productToUpdate);

            return product;
        } catch (Exception e) {
            throw new EJBException("ERROR_UPDATING_PRODUCT", e);
        }
    }

    public Product delete(int id) {
        try {
            Product product = find(id);

            categoryBean.unsetProductInCategory(product.getId(), product.getCategory().getId());

            product.setInvalid(true);

            em.persist(product);
            return product;
        } catch (Exception e) {
            throw new EJBException("ERROR_DELETING_PRODUCT", e);
        }
    }
    public void setProductInReplacedBy(int productIDToReplace, int productIDFromListReplacedBy) {
        try {
            Product productToReplace = find(productIDToReplace);
            Product productWithList = find(productIDFromListReplacedBy);

            productWithList.addProductToReplacedBy(productToReplace);
        } catch (Exception e) {
            throw new EJBException("ERROR_ADDING_PRODUCT_IN_LIST_REPLACED_BY", e);
        }
    }

    public void unsetProductInReplacedBy(int productIDToRemoveReplace, int productIDFromListReplacedBy) {
        try {
            Product productToRemoveReplace = find(productIDToRemoveReplace);
            Product productWithList = find(productIDFromListReplacedBy);

            productWithList.removeProductFromReplacedBy(productToRemoveReplace);
        } catch (Exception e) {
            throw new EJBException("ERROR_REMOVING_CATEGORY_IN_LIST_REPLACED_BY", e);
        }
    }
}
