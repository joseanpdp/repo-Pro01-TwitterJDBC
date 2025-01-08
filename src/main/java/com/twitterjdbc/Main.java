package com.twitterjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.mindrot.jbcrypt.BCrypt;


public class Main {
    static final Scanner SCANNER = new Scanner(System.in);
    static final String URL = "jdbc:mysql://localhost:3306/social_network";
    static final String USUARIO = "root";
    static final String PASSWORD = "root";
    static Connection con;
    static int userID = -1;

    /*
        Crear un método register que reciba una conexión, un usuario, un email y una contraseña y que encripte
        la contraseña e inserte los datos de usuario, teniendo en cuenta la fecha actual, mostrando un mensaje
        por pantalla del resultado de la operación.
     */
    public static void ejecutarRegistrar(Connection con) throws SQLException {
        System.out.print("Usuario: ");
        String usuario = SCANNER.nextLine();
        System.out.print("Email: ");
        String email = SCANNER.nextLine();
        System.out.print("Contraseña: ");
        String password = SCANNER.nextLine();
        registrar(con, usuario, email, password);
    }

    public static void registrar(Connection con, String usuario, String email, String password) throws SQLException {
        String query = "INSERT INTO users (username, email, password, createDate) VALUES(?, ?, ?, ?);";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        String passwordEncriptada = BCrypt.hashpw(password, BCrypt.gensalt());
        LocalDateTime registerDate = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        String formatedDate = registerDate.format(dateFormat);
        preparedStatement.setString(1, usuario);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, passwordEncriptada);
        preparedStatement.setString(4, formatedDate);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("Registrado correctamente");
    }

    public static Connection conectar(String url, String usuario, String password) throws SQLException {
        return DriverManager.getConnection(url, usuario, password);
    }

    public static void main(String[] args) {
        try {
            con = conectar(URL, USUARIO, PASSWORD);
            ejecutarRegistrar(con);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}