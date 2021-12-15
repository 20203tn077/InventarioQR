package mx.edu.utez.InventarioQR.controller;

import mx.edu.utez.InventarioQR.model.Respuesta;
import mx.edu.utez.InventarioQR.model.articulo.BeanArticulo;
import mx.edu.utez.InventarioQR.model.articulo.DaoArticulo;
import mx.edu.utez.InventarioQR.model.categoria.DaoCategoria;

import javax.jws.WebParam;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/InventarioQR")
public class Service {
    @GET
    @Path("/articulo")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta getArticulo() {
       Respuesta respuesta = new Respuesta();
        try {
            respuesta = new DaoArticulo().getArticulos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    @GET
    @Path("/articulo/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta getArticuloPorCodigo(@PathParam("codigo") long  codigo) {
        Respuesta respuesta = new Respuesta();
        try {
            respuesta = new DaoArticulo().getArticuloPorCodigo(codigo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta;
    }
    @GET
    @Path("/categoria")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta getCategorias() {
        Respuesta respuesta = new Respuesta();
        try {
            respuesta = new DaoCategoria().getCategorias();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    @GET
    @Path("/articulo/categoria/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta getArticuloPorCategoria(@PathParam("id") int id) {
        Respuesta respuesta = new Respuesta();
        try {
            respuesta = new DaoArticulo().getArticulosPorCategoria(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta;
    }

    @POST
    @Path("/articulo")// http://localhost:8080/ventas/employee
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Respuesta insertarArticulo(@WebParam BeanArticulo beanArticulo) {
        Respuesta respuesta = new Respuesta();
        try {
            respuesta = new DaoArticulo().insertarArticulo(beanArticulo.getCodigo(), beanArticulo.getNombre(), beanArticulo.getDescripcion(), beanArticulo.getCategoriaId().getId(), beanArticulo.getCantidad());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta;
    }
}
