package com.twitterjdbc;

import java.sql.*;
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
    static int usuarioID = -1;

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
    public static void ejecutarIniciarSesion(Connection con) throws Exception {
        System.out.print("Usuario: ");
        String usuario = SCANNER.nextLine();
        System.out.print("Contraseña: ");
        String password = SCANNER.nextLine();
        iniciarSesion(con, usuario, password);
    }

    public static void iniciarSesion(Connection con, String usuario, String password) throws Exception {
        String query = "SELECT id, password FROM users WHERE username = \"" + usuario + "\";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        resultado.next();
        String passwordEncriptada = resultado.getString(2);
        if (BCrypt.checkpw(password, passwordEncriptada)) {
            System.out.println("La contraseña ingresada es correcta.");
            usuarioID = resultado.getInt(1);
        }
        else {
            throw new Exception("La contraseña no es correcta.");
        }
        resultado.close();
        sentencia.close();
    }

    public static void elegirRegistrarOiniciarSesion(Connection con) throws Exception {
        boolean bandera = true;
        while (bandera) {
            System.out.println("Elige una de estas opciones: ");
            System.out.println("\t[1] Registrar");
            System.out.println("\t[2] Iniciar Sesión");
            System.out.println("\t[0] Salir");
            System.out.print("Acción: ");
            int accion = Integer.parseInt(SCANNER.nextLine());
            System.out.println();
            switch (accion) {
                case 0:
                    System.out.println("Programa cerrado. Hasta luego");
                    bandera = false;
                    break;
                case 1:
                    ejecutarRegistrar(con);
                    System.out.println();
                    Thread.sleep(1000);
                    break;
                case 2:
                    ejecutarIniciarSesion(con);
                    System.out.println();
                    Thread.sleep(1000);
                    break;
                default:
                    System.out.println("El índice introducido no es correcto, inténtalo de nuevo.");
                    break;
            }
        }
    }

    public static Connection conectar(String url, String usuario, String password) throws SQLException {
        return DriverManager.getConnection(url, usuario, password);
    }

    public static void main(String[] args) {
        try {
            con = conectar(URL, USUARIO, PASSWORD);
            elegirRegistrarOiniciarSesion(con);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}