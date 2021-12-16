package mx.edu.utez.InventarioQR.model.categoria;

import mx.edu.utez.InventarioQR.connection.ConnectionMySQL;
import mx.edu.utez.InventarioQR.model.Respuesta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoCategoria {
    private Connection con;
    private CallableStatement cstm;
    private ResultSet rs;

    private final String CONSULTAR_CATEGORIA = "CALL pdo_consultaCategoria(?)";

    public Respuesta getCategorias() {
        Respuesta respuesta = new Respuesta();
        List<BeanCategoria> beanCategorias = new ArrayList<>();
        try {
            con = ConnectionMySQL.getConnection();
            cstm = con.prepareCall(CONSULTAR_CATEGORIA);
            cstm.registerOutParameter("p_count", Types.INTEGER);
            rs = cstm.executeQuery();

            int count = cstm.getInt("p_count");
            if (count != 0) {
                while (rs.next()) {
                    BeanCategoria beanCategoria = new BeanCategoria();
                    beanCategoria.setId(rs.getInt("id"));
                    beanCategoria.setNombre(rs.getString("nombre"));
                    beanCategorias.add(beanCategoria);
                }
                respuesta.respuesta = beanCategorias;
                respuesta.mensaje = "Consulta exitosa";
                respuesta.exitoso = true;
            }else{
                respuesta.respuesta = null;
                respuesta.mensaje = "No hay categorias registradas";
                respuesta.exitoso = false;
            }

        } catch (SQLException e) {
            System.out.println("Ocurrio un error en el metodo getCategorias " + e.getMessage());
            respuesta.respuesta = null;
            respuesta.mensaje = "Se produjo un error al realizar la consulta";
            respuesta.exitoso = false;
        } finally {
            ConnectionMySQL.closeConnections(con, cstm, rs);
        }
        return respuesta;
    }
}
