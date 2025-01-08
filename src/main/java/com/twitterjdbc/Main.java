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
    public static void ejecutarRegistrar(Connection con) throws Exception {
        System.out.print("Usuario: ");
        String usuario = SCANNER.nextLine();
        System.out.print("Email: ");
        String email = SCANNER.nextLine();
        System.out.print("Contraseña: ");
        String password = SCANNER.nextLine();
        registrar(con, usuario, email, password);
    }

    public static void registrar(Connection con, String usuario, String email, String password) throws Exception {
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

    /*
        Crear un método showYourProfile que reciba una conexión y muestre por pantalla el nombre de
        usuario, email, descripción y fecha de registro del userID estático.
     */

    public static void mostrarTuPerfil(Connection con) throws SQLException {
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

    /*
        Crear un método tweetear que reciba una conexión y un String con el texto a twittear y que inserte una
        publicación con el userID estático, el texto pasado por pantalla y la fecha actual
     */
    public static void ejecutarPublicar(Connection con) throws SQLException {
        System.out.print("¿Qué quieres publicar?:\n\t> ");
        String texto = SCANNER.nextLine();
        publicar(con, texto);
    }

    public static void publicar(Connection con, String texto) throws SQLException {
        String query = "INSERT INTO publications (userId, text, createDate) VALUES(?, ?, ?);";
        PreparedStatement sentencia = con.prepareStatement(query);
        LocalDateTime registerDate = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
        String formatedDate = registerDate.format(dateFormat);
        sentencia.setInt(1, usuarioID);
        sentencia.setString(2, texto);
        sentencia.setString(3, formatedDate);
        sentencia.executeUpdate();
        sentencia.close();
        System.out.println("Publicado correctamente");
    }

    /*
        Crear un método showYourTweets que reciba una conexión y muestre por pantalla el id de la publicación,
        el nombre de usuario de userID, el texto y la fecha en la que se creo la publicación.
     */
    public static void mostrarTusPublicaciones(Connection con) throws SQLException {
        String query = "SELECT publications.id, users.username, publications.text, publications.createDate " +
                       "FROM publications JOIN users ON users.id = publications.userId " +
                       "WHERE publications.userId = " + usuarioID + ";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        while(resultado.next()) {
            System.out.println("[" + resultado.getInt(1) + "] - " + resultado.getString(2) +
                               "\n\t" + resultado.getString(3) + "\n\t-Creada el " +
                               resultado.getString(4) + "-");
        }
        resultado.close();
        sentencia.close();
    }

    /*
        Crear un método deleteTweet que reciba una conexión y un id y que elimine la publicación cuyo id de
        publicación coincida con el id pasado como argumento Y QUE EL ID DEL USUARIO QUE LA PUBLICÓ COINCIDA
        CON EL ATRIBUTO ESTÁTICO userID.
     */
    public static void ejecutarEliminarPublicacion(Connection con) throws SQLException {
        System.out.println("-------------------------------------------------------");
        mostrarTusPublicaciones(con);
        System.out.println("-------------------------------------------------------");
        System.out.print("¿Qué publicacion quieres borrar?: ");
        int publicacionId = Integer.parseInt(SCANNER.nextLine());
        eliminarPublicacion(con, publicacionId);
    }

    public static void eliminarPublicacion(Connection con, int publicacionId) throws SQLException {
        String query = "DELETE FROM publications WHERE id = ? AND userId = ?;";
        PreparedStatement sentencia = con.prepareStatement(query);
        sentencia.setInt(1, publicacionId);
        sentencia.setInt(2, usuarioID);
        int filasAfectadas = sentencia.executeUpdate();
        if (filasAfectadas > 0) {
            System.out.println("Se ha eliminado la publicación con éxito");
        }
        else {
            System.out.println("Publicación no encontrada");
        }
        sentencia.close();
    }

    /*
        Crear un método showTweets que reciba una conexión y muestre por pantalla el nombre del usuario, el texto
        del tweet y la fecha en la que se publicó el tweet de todos los tweets. Deben aparecer en orden de más
        nuevo a más viejo.
     */
    public static void mostrarTodasLasPublicaciones(Connection con) throws SQLException {
        String query = "SELECT publications.id, users.username, publications.text, publications.createDate " +
                "FROM publications JOIN users ON users.id = publications.userId ORDER BY publications.createDate DESC;";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        System.out.println("-------------------------");
        while(resultado.next()) {
            System.out.println("[" + resultado.getInt(1) + "] - " + resultado.getString(2) +
                    "\n\t" + resultado.getString(3) + "\n\t-Creada el " +
                    resultado.getString(4) + "-");
            System.out.println("-------------------------");
        }
        resultado.close();
        sentencia.close();
    }

    /*
        Crear un método showAllProfiles que reciba una conexión y muestra por pantalla id, nombre de usuario,
        email, descripción y fecha de registro de todos los usuarios menos del usuario cuyo id sea idéntico
        al userID estático.
     */
    public static void mostrarTodosLosPerfiles(Connection con) throws SQLException {
        String query = "SELECT id, username, email, description, createDate FROM users WHERE id != " + usuarioID + ";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        System.out.println("-------------------------");
        while(resultado.next()) {
            System.out.println("[" + resultado.getString(1) + "] - " + resultado.getString(2) + " | Creada el " + resultado.getString(5));
            System.out.println("\t[" + resultado.getString(3) + "]");
            System.out.println("\t" + resultado.getString(4));
            System.out.println("-------------------------");
        }
        resultado.close();
        sentencia.close();
    }

    /*
        Crear un método showFollowedTweets que reciba una conexión y muestre por pantalla el nombre del usuario,
        el texto del tweet y la fecha en la que se publicó el tweet de los tweets que hayan publicado las personas
        que sigues. Deben aparecer en orden de más nuevo a más viejo.
     */

    /*
        Crear un método showYourFollows que reciba una conexión y muestre por pantalla todos los datos de
        los usuarios que sigues.
     */
    /*
        Crear un método showYourFollowers que reciba una conexión y muestre por pantalla todos los datos de los
        usuarios que siguen al atributo userID estático.
     */
    /*
        Crear un método estático showOtherProfile que dada una conexión y un nombre de usuario te muestre todos
        los datos de ese usuario.
     */
    /*
        Crear un método follow que reciba una conexión y un id al que seguir e insertar en la tabla follows que
        el userID sigue al id pasado como argumento.
     */
    /*
        Crear un método unfollow que reciba una conexión y un id al que seguir y borrar en la tabla follows el
        registro que representa que el userID sigue al id pasado como argumento.
     */

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
                    elegirAccionesDeUsuario(con);
                    Thread.sleep(1000);
                    System.out.println();
                    break;
                default:
                    System.out.println("El índice introducido no es correcto, inténtalo de nuevo.");
                    break;
            }
        }
    }

    public static void elegirAccionesDeUsuario(Connection con) throws Exception {
        boolean bandera = true;
        while (bandera) {
            System.out.println("Elige una de estas opciones: ");
            System.out.println("\t[1] Perfil");
            System.out.println("\t[2] Publicar");
            System.out.println("\t[3] Ver tus publicaciones");
            System.out.println("\t[4] Eliminar publicación");
            System.out.println("\t[5] Ver todas las publicaciones");
            System.out.println("\t[6] Ver todos los perfiles");
            System.out.println("\t[0] Cerrar sesión");
            System.out.print("Acción: ");
            int accion = Integer.parseInt(SCANNER.nextLine());
            System.out.println();
            switch (accion) {
                case 0:
                    System.out.println("Sesión cerrada con éxito");
                    bandera = false;
                    usuarioID = -1;
                    break;
                case 1:
                    mostrarTuPerfil(con);
                    System.out.println();
                    Thread.sleep(1000);
                    break;
                case 2:
                    ejecutarPublicar(con);
                    System.out.println();
                    Thread.sleep(1000);
                    break;
                case 3:
                    mostrarTusPublicaciones(con);
                    System.out.println();
                    Thread.sleep(1000);
                    break;
                case 4:
                    ejecutarEliminarPublicacion(con);
                    System.out.println();
                    Thread.sleep(1000);
                    break;
                case 5:
                    mostrarTodasLasPublicaciones(con);
                    System.out.println();
                    Thread.sleep(1000);
                    break;
                case 6:
                    mostrarTodosLosPerfiles(con);
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