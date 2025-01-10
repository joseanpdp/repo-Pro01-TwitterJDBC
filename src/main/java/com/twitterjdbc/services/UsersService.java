package com.twitterjdbc.services;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class UsersService {
    final Scanner SCANNER = new Scanner(System.in);
    Connection con;

    public UsersService(Connection con) {
        this.con = con;
    }

     /*
        Crear un método register que reciba una conexión, un usuario, un email y una contraseña y que encripte
        la contraseña e inserte los datos de usuario, teniendo en cuenta la fecha actual, mostrando un mensaje
        por pantalla del resultado de la operación.
     */
    public void ejecutarRegistrar() throws Exception {
        System.out.print("Usuario: ");
        String usuario = SCANNER.nextLine();
        System.out.print("Email: ");
        String email = SCANNER.nextLine();
        System.out.print("Contraseña: ");
        String password = SCANNER.nextLine();
        registrar(usuario, email, password);
    }

    public void registrar(String usuario, String email, String password) throws Exception {
        String query = "INSERT INTO users (username, email, password, createDate) VALUES(?, ?, ?, ?);";
        PreparedStatement sentencia = con.prepareStatement(query);
        String passwordEncriptada = BCrypt.hashpw(password, BCrypt.gensalt());
        LocalDateTime registerDate = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        String formatedDate = registerDate.format(dateFormat);
        sentencia.setString(1, usuario);
        sentencia.setString(2, email);
        sentencia.setString(3, passwordEncriptada);
        sentencia.setString(4, formatedDate);
        sentencia.executeUpdate();
        sentencia.close();
        System.out.println("Registrado correctamente");
    }

    /*
        Crear un método login que reciba una conexión, un usuario y una contraseña y busque a dicho usuario y
        compruebe que la contraseña hasheada (la que proviene de la base de datos) coincide con la contraseña
        introducida, mostrando un mensaje de éxito. En caso contrario lanzar una excepción.
     */
    public int ejecutarIniciarSesion() throws Exception {
        System.out.print("Usuario: ");
        String usuario = SCANNER.nextLine();
        System.out.print("Contraseña: ");
        String password = SCANNER.nextLine();
        return iniciarSesion(usuario, password);
    }

    public int iniciarSesion(String usuario, String password) throws Exception {
        String query = "SELECT id, password FROM users WHERE username = \"" + usuario + "\";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        resultado.next();
        String passwordEncriptada = resultado.getString(2);
        if (BCrypt.checkpw(password, passwordEncriptada)) {
            System.out.println("La contraseña ingresada es correcta.");
            return resultado.getInt(1);
        }
        resultado.close();
        sentencia.close();
        throw new Exception("La contraseña no es correcta.");
    }

    /*
        Crear un método showYourProfile que reciba una conexión y muestre por pantalla el nombre de
        usuario, email, descripción y fecha de registro del userID estático.
     */

    public void mostrarTuPerfil(int usuarioID) throws SQLException {
        String query = "SELECT username, email, description, createDate FROM users WHERE id = " + usuarioID + ";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        resultado.next();
        System.out.println(resultado.getString(1) + " | Creada el " + resultado.getString(4));
        System.out.println("\t[" + resultado.getString(2) + "]");
        System.out.println("\t" + resultado.getString(3));
        resultado.close();
        sentencia.close();
    }
}
