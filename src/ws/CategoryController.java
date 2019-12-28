package ws;

import dtos.CategoryDTO;
import ejbs.CategoryBean;
import entities.Category;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/categories")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class CategoryController {

    @EJB
    private CategoryBean categoryBean;

    CategoryDTO toDTO(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getDescription(),
                category.isInvalid()
        );
    }

    List<CategoryDTO> toDTOs(List<Category> categories) {
        return categories.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GET
    @Path("/")
    public List<CategoryDTO> all() {
        try {
            return toDTOs(categoryBean.all());
        } catch (Exception e) {
            throw new EJBException("ERROR_GET_CATEGORIES", e);
        }
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        try {
            Category category = categoryBean.find(id);

            if(category != null) {
                return Response.status(Response.Status.OK).entity(toDTO(category)).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @POST
    @Path("/")
    public Response createNewCategory(CategoryDTO categoryDTO) {
        try {
            Category newCategory = categoryBean.create(categoryDTO.getDescription());

            if(newCategory != null) {
                return Response.status(Response.Status.OK).entity(toDTO(newCategory)).build();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCategory(@PathParam("id") int idFromCategoryToUpdate, CategoryDTO categoryDTO) {
        try {
            Category category = categoryBean.update(idFromCategoryToUpdate, categoryDTO.getDescription());

            if(category != null) {
                return Response.status(Response.Status.OK).entity(toDTO(category)).build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") int id) {
        try {
            Category category = categoryBean.delete(id);

            if(category != null) {
                return Response.status(Response.Status.OK).entity(toDTO(category)).build();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }
}
