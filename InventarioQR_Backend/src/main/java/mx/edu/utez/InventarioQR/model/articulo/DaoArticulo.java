package mx.edu.utez.InventarioQR.model.articulo;

import javassist.compiler.ast.CondExpr;
import mx.edu.utez.InventarioQR.connection.ConnectionMySQL;
import mx.edu.utez.InventarioQR.model.Respuesta;
import mx.edu.utez.InventarioQR.model.categoria.BeanCategoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoArticulo {
    private Connection con;
    private CallableStatement cstm;
    private ResultSet rs;

    private final String CONSULTAR_ARTICULO = "CALL pdo_consultaPorCodigo(?,?)";
    private final String CONSULTAR_ARTICULOS = "CALL pdo_consulta";
    private final String CONSULTAR_ARTICULO_CATEGORIA = "CALL pdo_consultaPorCategoria(?,?) ";
    private final String INSERTAR_ARTICULO = "CALL insertar_articulo(?, ?, ?, ?, ?, ?)";

    public Respuesta getArticulos() {
        Respuesta respuesta = new Respuesta();
        List<BeanArticulo> beanArticulos = new ArrayList<>();
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall(CONSULTAR_ARTICULOS);
            rs = cstm.executeQuery();
            if (rs.next()) {
                while (rs.next()) {
                    BeanArticulo beanArticulo = new BeanArticulo();
                    BeanCategoria beanCategoria = new BeanCategoria();
                    beanArticulo.setCodigo(rs.getLong("codigo"));
                    beanArticulo.setNombre(rs.getString("nombre"));
                    beanArticulo.setDescripcion(rs.getString("descripcion"));
                    beanCategoria.setNombre(rs.getString("nombre"));
                    //Obtenemos los campos de Categoría y los guardamos en una variable
                    beanArticulo.setCategoriaId(beanCategoria);
                    //Una vez que ya tenemos todos los datos de categoría guardados en una variable esa es la que mandamos
                    beanArticulo.setCantidad(rs.getInt("cantidad"));
                    beanArticulos.add(beanArticulo);
                    respuesta.respuesta = beanArticulos;
                    respuesta.exitoso = true;
                }
            } else {
                respuesta.respuesta = null;
                respuesta.exitoso = false;

            }


        } catch (SQLException e) {
            System.out.println("Ocurrio un error en el metodo getArticulos " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnections(con, cstm, rs);
        }
        return respuesta;
    }

    public Respuesta getArticuloPorCodigo(long codigo) {
        Respuesta respuesta = new Respuesta();
        BeanArticulo beanArticulo = new BeanArticulo();
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall(CONSULTAR_ARTICULO);
            cstm.setLong("p_codigo", codigo);
            cstm.registerOutParameter("p_error", Types.INTEGER);
            rs = cstm.executeQuery();
            int error = cstm.getInt("p_error");
            if (error == 1) {
                respuesta.respuesta = null;
                respuesta.exitoso = false;
            } else {
                if (rs.next()) {
                    BeanCategoria beanCategoria = new BeanCategoria();
                    beanArticulo.setCodigo(rs.getLong("codigo"));
                    beanArticulo.setNombre(rs.getString("nombre"));
                    beanArticulo.setDescripcion(rs.getString("descripcion"));
                    beanCategoria.setId(rs.getInt("id"));
                    beanCategoria.setNombre(rs.getString("nombre"));
                    beanArticulo.setCategoriaId(beanCategoria);
                    beanArticulo.setCantidad(rs.getInt("cantidad"));
                    respuesta.respuesta = beanArticulo;
                    respuesta.exitoso = true;
                }
            }

        } catch (SQLException e) {
            System.out.println("Ocurrio un error en el metodo getArticuloPorCodigo " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnections(con, cstm, rs);
        }
        return respuesta;
    }

    public Respuesta getArticulosPorCategoria(int categoria) {
        Respuesta respuesta = new Respuesta();
        List<BeanArticulo> beanArticulos = new ArrayList<>();
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall(CONSULTAR_ARTICULO_CATEGORIA);
            cstm.setInt("p_categoria", categoria);
            cstm.registerOutParameter("p_errorCategoria", Types.INTEGER);
            rs = cstm.executeQuery();
            int errorCategoria = cstm.getInt("p_errorCategoria");
            if(errorCategoria == 0) {
                while (rs.next()) {
                    BeanArticulo beanArticulo = new BeanArticulo();
                    BeanCategoria beanCategoria = new BeanCategoria();
                    beanArticulo.setCodigo(rs.getLong("codigo"));
                    beanArticulo.setNombre(rs.getString("nombre"));
                    beanArticulo.setDescripcion(rs.getString("descripcion"));
                    beanCategoria.setId(rs.getInt("id"));
                    beanCategoria.setNombre(rs.getString("nombre"));
                    beanArticulo.setCategoriaId(beanCategoria);
                    beanArticulo.setCantidad(rs.getInt("cantidad"));
                    beanArticulos.add(beanArticulo);
                    respuesta.respuesta = beanArticulos;
                    respuesta.exitoso = true;
                }
            }else{
                respuesta.respuesta = null;
                respuesta.exitoso = false;
            }

        } catch (SQLException e) {
            System.out.println("Ocurrio un error en el metodo getArticulosPorCategoria " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnections(con, cstm, rs);
        }
        return respuesta;
    }

    public Respuesta insertarArticulo(long codigo, String nombre, String descripcion, int categoria, int cantidad) {
        Respuesta respuesta = new Respuesta();
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall(INSERTAR_ARTICULO);

            cstm.setLong("p_codigo", codigo);
            cstm.setString("p_nombre", nombre);
            cstm.setString("p_descripcion", descripcion);
            cstm.setInt("p_categoria", categoria);
            cstm.setInt("p_cantidad", cantidad);
            //Parametro de salida (0 ó 1)
            cstm.registerOutParameter("p_errorArticulo", Types.INTEGER);
            cstm.executeUpdate();

            //Obtenemos el valor del parámetro de salida
            int entero = cstm.getInt(6);
            if (entero == 0) {
                respuesta.respuesta = "Registro ingresado exitosamente";
                respuesta.exitoso = true;
            } else {
                respuesta.respuesta = "El codigo ya se encuentra registrado";
                respuesta.exitoso = false;
            }
        } catch (SQLException e) {
            System.out.println("Ocurrio un error en el metodo insertarArticulo " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnections(con, cstm);
        }
        return respuesta;
    }
}
