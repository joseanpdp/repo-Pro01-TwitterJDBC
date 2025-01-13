package com.twitterjdbc.services;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.Consumer;

public class UsersServiceImpl implements UsersService {
    Connection con;

    public UsersServiceImpl(Connection con) {
        this.con = con;
    }




    /*
        Crear un método showYourProfile que reciba una conexión y muestre por pantalla el nombre de
        usuario, email, descripción y fecha de registro del userID estático.
     */

    public void mostrarTuPerfil(int usuarioID, Consumer<ResultSet> consumidor) throws SQLException {
        String query = "SELECT username, email, description, createDate FROM users WHERE id = " + usuarioID + ";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        resultado.next();
        consumidor.accept(resultado);
        resultado.close();
        sentencia.close();
    }

    /*
        Crear un método showAllProfiles que reciba una conexión y muestra por pantalla id, nombre de usuario,
        email, descripción y fecha de registro de todos los usuarios menos del usuario cuyo id sea idéntico
        al userID estático.
     */
    public void mostrarTodosLosPerfiles(int usuarioID, Consumer<ResultSet> consumidor) throws SQLException {
        String query = "SELECT id, username, email, description, createDate FROM users WHERE id != " + usuarioID + ";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        while (resultado.next()) {
            consumidor.accept(resultado);
        }
        resultado.close();
        sentencia.close();
    }

    public boolean comprobarUsuarioExistenteString(String usuario) throws Exception {
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

    public void mostrarUnPerfil(String nombreUsuario, Consumer<ResultSet> consumidor) throws SQLException {
        String query = "SELECT id, username, email, description, createDate FROM users WHERE username = \"" + nombreUsuario + "\";";
        Statement sentencia = con.createStatement();
        ResultSet resultado = sentencia.executeQuery(query);
        resultado.next();
        consumidor.accept(resultado);
        resultado.close();
        sentencia.close();
    }

    /*
        Crear un método follow que reciba una conexión y un id al que seguir e insertar en la tabla follows que
        el userID sigue al id pasado como argumento.
     */
    /*
    public void ejecutarSeguir(Scanner scanner, int usuarioID) throws Exception {
        System.out.println("----------------------");
        mostrarTodosLosPerfiles(usuarioID);
        System.out.println("----------------------");
        System.out.print("¿A qué usuario quieres seguir?: ");
        int usuarioASeguir = Integer.parseInt(scanner.nextLine());
        if (comprobarUsuarioExistenteInt(usuarioASeguir)) {
            if (usuarioASeguir != usuarioID) {
                seguir(usuarioASeguir, usuarioID);
            }
            else {
                System.out.println("No puedes seguirte a ti mismo");
            }
        }
        else {
            System.out.println("Este usuario no existe");
        }
    }

*/
    public boolean comprobarUsuarioExistenteInt(int usuario) throws Exception {
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



    /*
        Crear un método showYourFollows que reciba una conexión y muestre por pantalla todos los datos de
        los usuarios que sigues.
     */
    public void mostrarLosUsuariosQueSigues(int usuarioID) throws SQLException {
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
    /*
    public void ejecutarDejarDeSeguir(Scanner scanner, int usuarioID) throws Exception {
        System.out.println("----------------------");
        mostrarLosUsuariosQueSigues(usuarioID);
        System.out.println("----------------------");
        System.out.print("¿A qué usuario quieres dejar de seguir?: ");
        int usuarioADejarDeSeguir = Integer.parseInt(scanner.nextLine());
        if (comprobarUsuarioQueSiguesExistenteInt(usuarioADejarDeSeguir, usuarioID)) {
            if (usuarioADejarDeSeguir != usuarioID) {
                dejarDeSeguir(usuarioADejarDeSeguir, usuarioID);
            }
            else {
                System.out.println("No puedes dejarte de seguir a ti mismo");
            }
        }
        else {
            System.out.println("Este usuario no lo sigues o no existe");
        }
    }

    public boolean comprobarUsuarioQueSiguesExistenteInt(int usuario, int usuarioID) throws Exception {
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
    */



    /*
        Crear un método showYourFollowers que reciba una conexión y muestre por pantalla todos los datos de los
        usuarios que siguen al atributo userID estático.
     */
    public void mostrarLosUsuariosQueTeSiguen(int usuarioID) throws SQLException {
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
}
