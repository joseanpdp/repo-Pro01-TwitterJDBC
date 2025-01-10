package com.twitterjdbc;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.twitterjdbc.services.UsersService;
import org.mindrot.jbcrypt.BCrypt;


public class Main {
    static final Scanner SCANNER = new Scanner(System.in);
    static final String URL = "jdbc:mysql://localhost:3306/social_network";
    static final String USUARIO = "root";
    static final String PASSWORD = "root";
    static Connection con;
    static int usuarioID = -1;

    static UsersService usersService;

    /*
        Crear un método tweetear que reciba una conexión y un String con el texto a twittear y que inserte una
        publicación con el userID estático, el texto pasado por pantalla y la fecha actual
     */
    public static void ejecutarPublicar() throws SQLException {
        System.out.print("¿Qué quieres publicar?:\n\t> ");
        String texto = SCANNER.nextLine();
        publicar(texto);
    }

    public static void publicar(String texto) throws SQLException {
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
    public static void mostrarTusPublicaciones() throws SQLException {
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
    public static void ejecutarEliminarPublicacion() throws SQLException {
        System.out.println("-------------------------------------------------------");
        mostrarTusPublicaciones();
        System.out.println("-------------------------------------------------------");
        System.out.print("¿Qué publicacion quieres borrar?: ");
        int publicacionId = Integer.parseInt(SCANNER.nextLine());
        eliminarPublicacion(publicacionId);
    }

    public static void eliminarPublicacion(int publicacionId) throws SQLException {
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
    public static void mostrarTodasLasPublicaciones() throws SQLException {
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
    public static void mostrarTodosLosPerfiles() throws SQLException {
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
        Crear un método estático showOtherProfile que dada una conexión y un nombre de usuario te muestre todos
        los datos de ese usuario.
     */
    public static void ejecutarMostrarUnPerfil() throws Exception {
        System.out.print("Indica el nombre del usuario: ");
        String nombreUsuario = SCANNER.nextLine();
        if (comprobarUsuarioExistenteString(nombreUsuario)) {
                mostrarUnPerfil(nombreUsuario);
        }
        else {
            System.out.println("Este usuario no existe");
        }
    }

    public static boolean comprobarUsuarioExistenteString(String usuario) throws Exception {
        String query = "SELECT username FROM users";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        System.out.println("-------------------------");
        while(resultado.next()) {
            if (resultado.getString(1).equals(usuario)) {
                return true;
            }
        }
        resultado.close();
        sentencia.close();
        return false;
    }

    public static void mostrarUnPerfil(String nombreUsuario) throws SQLException {
        String query = "SELECT id, username, email, description, createDate FROM users WHERE username = \"" + nombreUsuario + "\";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        resultado.next();
        System.out.println("[" + resultado.getString(1) + "] - " + resultado.getString(2) + " | Creada el " + resultado.getString(5));
        System.out.println("\t[" + resultado.getString(3) + "]");
        System.out.println("\t" + resultado.getString(4));
        resultado.close();
        sentencia.close();
    }

    /*
        Crear un método follow que reciba una conexión y un id al que seguir e insertar en la tabla follows que
        el userID sigue al id pasado como argumento.
     */
    public static void ejecutarSeguir() throws Exception {
        System.out.println("----------------------");
        mostrarTodosLosPerfiles();
        System.out.println("----------------------");
        System.out.print("¿A qué usuario quieres seguir?: ");
        int usuarioASeguir = Integer.parseInt(SCANNER.nextLine());
        if (comprobarUsuarioExistenteInt(usuarioASeguir)) {
            if (usuarioASeguir != usuarioID) {
                seguir(usuarioASeguir);
            }
            else {
                System.out.println("No puedes seguirte a ti mismo");
            }
        }
        else {
            System.out.println("Este usuario no existe");
        }
    }

    public static boolean comprobarUsuarioExistenteInt(int usuario) throws Exception {
        String query = "SELECT id FROM users";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        System.out.println("-------------------------");
        while(resultado.next()) {
            if (resultado.getInt(1) == usuario) {
                return true;
            }
        }
        resultado.close();
        sentencia.close();
        return false;
    }

    public static void seguir(int usuarioASeguir) throws Exception {
        String query = "INSERT INTO follows (users_id, userToFollowId) VALUES(?, ?);";
        PreparedStatement sentencia = con.prepareStatement(query);
        sentencia.setInt(1, usuarioID);
        sentencia.setInt(2, usuarioASeguir);
        sentencia.executeUpdate();
        sentencia.close();
        System.out.println("Registrado correctamente");
    }

    /*
        Crear un método showYourFollows que reciba una conexión y muestre por pantalla todos los datos de
        los usuarios que sigues.
     */
    public static void mostrarLosUsuariosQueSigues() throws SQLException {
        String query = "SELECT users.id, users.username, users.email, users.description, users.createDate " +
                       "FROM users JOIN follows ON users.id = follows.userToFollowId WHERE follows.users_id = " +
                       usuarioID + ";";
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
        Crear un método unfollow que reciba una conexión y un id al que seguir y borrar en la tabla follows el
        registro que representa que el userID sigue al id pasado como argumento.
     */
    public static void ejecutarDejarDeSeguir() throws Exception {
        System.out.println("----------------------");
        mostrarLosUsuariosQueSigues();
        System.out.println("----------------------");
        System.out.print("¿A qué usuario quieres dejar de seguir?: ");
        int usuarioADejarDeSeguir = Integer.parseInt(SCANNER.nextLine());
        if (comprobarUsuarioQueSiguesExistenteInt(usuarioADejarDeSeguir)) {
            if (usuarioADejarDeSeguir != usuarioID) {
                dejarDeSeguir(usuarioADejarDeSeguir);
            }
            else {
                System.out.println("No puedes dejarte de seguir a ti mismo");
            }
        }
        else {
            System.out.println("Este usuario no lo sigues o no existe");
        }
    }

    public static boolean comprobarUsuarioQueSiguesExistenteInt(int usuario) throws Exception {
        String query = "SELECT userToFollowId FROM follows WHERE users_id = " + usuarioID + ";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        System.out.println("-------------------------");
        while(resultado.next()) {
            if (resultado.getInt(1) == usuario) {
                return true;
            }
        }
        resultado.close();
        sentencia.close();
        return false;
    }

    public static void dejarDeSeguir(int usuarioADejarDeSeguir) throws Exception {
        String query = "DELETE FROM follows WHERE users_id = ? AND userToFollowId = ?;";
        PreparedStatement sentencia = con.prepareStatement(query);
        sentencia.setInt(1, usuarioID);
        sentencia.setInt(2, usuarioADejarDeSeguir);
        int filasAfectadas = sentencia.executeUpdate();
        if (filasAfectadas > 0) {
            System.out.println("Se ha dejado de seguir al usuario con éxito");
        }
        else {
            System.out.println("Usuario no encontrado");
        }
        sentencia.executeUpdate();
        sentencia.close();
    }

    /*
        Crear un método showYourFollowers que reciba una conexión y muestre por pantalla todos los datos de los
        usuarios que siguen al atributo userID estático.
     */
    public static void mostrarLosUsuariosQueTeSiguen() throws SQLException {
        String query = "SELECT users.id, users.username, users.email, users.description, users.createDate " +
                "FROM users JOIN follows ON users.id = follows.users_id WHERE follows.userToFollowId = " +
                usuarioID + ";";
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
    public static void mostrarPublicacionesDeUsuariosSeguidos() throws SQLException {
        String query = "SELECT publications.id, users.username, publications.text, publications.createDate " +
                       "FROM publications JOIN users ON users.id = publications.userId " +
                                         "JOIN follows ON follows.userToFollowId = users.id " +
                       "WHERE follows.users_id = " + usuarioID + " ORDER BY publications.createDate DESC;";
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



    public static void elegirRegistrarOiniciarSesion() throws Exception {
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
                case 0 -> {
                    System.out.println("Programa cerrado. Hasta luego");
                    bandera = false;
                }
                case 1 -> {
                    usersService.ejecutarRegistrar();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 2 -> {
                    usuarioID = usersService.ejecutarIniciarSesion();
                    System.out.println();
                    Thread.sleep(1000);
                    elegirAccionesDeUsuario();
                    Thread.sleep(1000);
                    System.out.println();
                }
                default -> System.out.println("El índice introducido no es correcto, inténtalo de nuevo.");
            }
        }
    }

    public static void elegirAccionesDeUsuario() throws Exception {
        boolean bandera = true;
        while (bandera) {
            System.out.println("Elige una de estas opciones: ");
            System.out.println("\t[1] Perfil");
            System.out.println("\t[2] Publicar");
            System.out.println("\t[3] Ver tus publicaciones");
            System.out.println("\t[4] Eliminar publicación");
            System.out.println("\t[5] Ver todas las publicaciones");
            System.out.println("\t[6] Ver todos los perfiles");
            System.out.println("\t[7] Ver un perfil");
            System.out.println("\t[8] Seguir");
            System.out.println("\t[9] Mostrar usuarios que sigues");
            System.out.println("\t[10] Dejar de seguir");
            System.out.println("\t[11] Mostrar usuarios que te siguen");
            System.out.println("\t[12] Mostrar publicaciones de usuarios seguidos");
            System.out.println("\t[0] Cerrar sesión");
            System.out.print("Acción: ");
            int accion = Integer.parseInt(SCANNER.nextLine());
            System.out.println();
            switch (accion) {
                case 0 -> {
                    System.out.println("Sesión cerrada con éxito");
                    bandera = false;
                    usuarioID = -1;
                }
                case 1 -> {
                    usersService.mostrarTuPerfil(usuarioID);
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 2 -> {
                    ejecutarPublicar();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 3 -> {
                    mostrarTusPublicaciones();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 4 -> {
                    ejecutarEliminarPublicacion();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 5 -> {
                    mostrarTodasLasPublicaciones();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 6 -> {
                    mostrarTodosLosPerfiles();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 7 -> {
                    ejecutarMostrarUnPerfil();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 8 -> {
                    ejecutarSeguir();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 9 -> {
                    mostrarLosUsuariosQueSigues();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 10 -> {
                    ejecutarDejarDeSeguir();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 11 -> {
                    mostrarLosUsuariosQueTeSiguen();
                    System.out.println();
                    Thread.sleep(1000);
                }
                case 12 -> {
                    mostrarPublicacionesDeUsuariosSeguidos();
                    System.out.println();
                    Thread.sleep(1000);
                }
                default -> System.out.println("El índice introducido no es correcto, inténtalo de nuevo.");
            }
        }
    }

    public static Connection conectar(String url, String usuario, String password) throws SQLException {
        return DriverManager.getConnection(url, usuario, password);
    }

    public static void main(String[] args) {
        try {
            con = conectar(URL, USUARIO, PASSWORD);
            usersService = new UsersService(con);
            elegirRegistrarOiniciarSesion();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}