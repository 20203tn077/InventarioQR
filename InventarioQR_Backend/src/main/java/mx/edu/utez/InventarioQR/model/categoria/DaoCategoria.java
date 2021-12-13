package mx.edu.utez.InventarioQR.model.categoria;

import mx.edu.utez.InventarioQR.connection.ConnectionMySQL;
import mx.edu.utez.InventarioQR.model.Respuesta;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DaoCategoria {
    private Connection con;
    private CallableStatement pstm;
    private ResultSet rs;

    private final String CONSULTAR_CATEGORIA = "CALL pdo_consultaCategoria";

    public Respuesta getCategorias() {
        Respuesta respuesta = new Respuesta();
        List<BeanCategoria> beanCategorias = new ArrayList<>();
        try {
            con = ConnectionMySQL.getConnection();
            pstm = con.prepareCall(CONSULTAR_CATEGORIA);
            rs = pstm.executeQuery();

            if (rs.next()) {
                while (rs.next()) {
                    BeanCategoria beanCategoria = new BeanCategoria();
                    beanCategoria.setId(rs.getInt("id"));
                    beanCategoria.setNombre(rs.getString("nombre"));
                    beanCategorias.add(beanCategoria);
                    respuesta.respuesta = beanCategorias;
                    respuesta.exitoso = true;
                }
            }else{
                respuesta.respuesta = null;
                respuesta.exitoso = true;
            }

        } catch (SQLException e) {
            System.out.println("Ocurrio un error en el metodo getCategorias " + e.getMessage());
        } finally {
            ConnectionMySQL.closeConnections(con, pstm, rs);
        }
        return respuesta;
    }
}
