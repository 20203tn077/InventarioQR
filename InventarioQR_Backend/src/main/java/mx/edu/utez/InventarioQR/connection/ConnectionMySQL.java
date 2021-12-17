package mx.edu.utez.InventarioQR.connection;

import java.sql.*;

public class ConnectionMySQL {

    public static Connection getConnection() throws SQLException {
        String host = "127.0.0.1";
        String port = "3306";
        String database = "InventarioQR";
        String useSSL = "false";
        String timezone = "UTC";
        String allowPublicKeyRetrieval="true";
        String url = String.format("jdbc:mysql://%s:%s/%s?useSSL=%s&serverTimezone=%s&allowPublicKeyRetrieval=%s", host, port, database, useSSL, timezone, allowPublicKeyRetrieval);
        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        return DriverManager.getConnection(url, "nekashii", "210129");
    }

    public static void closeConnections(Connection con, CallableStatement cstm, ResultSet rs){
        try{
            if(rs != null){ rs.close(); }

            if(cstm != null){ cstm.close(); }

            if(con != null){ con.close(); }

        }catch(SQLException e){ }
    }

    public static void closeConnections(Connection con, CallableStatement cstm){
        try{
            if(cstm != null){ cstm.close(); }

            if(con != null){ con.close(); }

        }catch(SQLException e){ }
    }

    public static void main(String[] args) {
        try {
            Connection con = getConnection();
            System.out.println("Conexión correcta");

        }catch (SQLException e) {
            System.out.println("Conexión fallida " + e.getMessage());
        }
    }
}
