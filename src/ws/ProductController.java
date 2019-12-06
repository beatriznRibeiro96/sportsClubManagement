package ws;

import dtos.ProductDTO;
import ejbs.CategoryBean;
import ejbs.ProductBean;
import entities.Category;
import entities.Product;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/products")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class ProductController {

    @EJB
    private ProductBean productBean;

    @EJB
    private CategoryBean categoryBean;

    ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCategory().getDescription(),
                product.isInvalid(),
                product.getReplaces() == null ? -1 : product.getReplaces().getId()
        );
    }

    List<ProductDTO> toDTOs(List<Product> products) {
        return products.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<ProductDTO> allValidProducts() {
        try {
            return toDTOs(productBean.getValidProducts());
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_VALID_PRODUCTS", e);
        }
    }

    @GET
    @Path("/all")
    public List<ProductDTO> all() {
        try {
            return productBean.all();
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_ALL_PRODUCTS", e);
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        try {
            Product product = productBean.find(id);

            if(product != null) {
                return Response.status(Response.Status.OK).entity(toDTO(product)).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @POST
    @Path("/")
    public Response createNewProduct(ProductDTO productDTO) {
        try {
            Category category = categoryBean.find(productDTO.getCategoryID());

            Product newProduct = productBean.create(
                    productDTO.getDescription(),
                    productDTO.getPrice(),
                    category
            );

            if(newProduct != null) {
                categoryBean.setProductInCategory(newProduct.getId(), category.getId());

                return Response.status(Response.Status.OK).entity(toDTO(newProduct)).build();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") int idFromProductToUpdate, ProductDTO productDTO) {
        try {
            Category category = categoryBean.find(productDTO.getCategoryID());

            Product updatedProduct = productBean.update(
                    idFromProductToUpdate,
                    productDTO.getDescription(),
                    productDTO.getPrice(),
                    category);

            if(updatedProduct != null) {
                categoryBean.setProductInCategory(updatedProduct.getId(), category.getId());

                return Response.status(Response.Status.OK).entity(toDTO(updatedProduct)).build();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") int idFromProductToDelete) {
        try {
            Product product = productBean.delete(idFromProductToDelete);

            if(product != null) {
                return Response.status(Response.Status.OK).entity(toDTO(product)).build();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }
}
