package com.twitterjdbc.services;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.Consumer;

public interface UsersService {
    public void mostrarTuPerfil(int usuarioID, Consumer<ResultSet> consumidor) throws SQLException;

    public void mostrarTodosLosPerfiles(int usuarioID, Consumer<ResultSet> consumidor) throws SQLException;

    // public void ejecutarMostrarUnPerfil(Scanner scanner) throws Exception;

    public boolean comprobarUsuarioExistenteString(String usuario) throws Exception;

    public void mostrarUnPerfil(String nombreUsuario, Consumer<ResultSet> consumidor) throws SQLException;

    // public void ejecutarSeguir(Scanner scanner, int usuarioID) throws Exception;

    public boolean comprobarUsuarioExistenteInt(int usuario) throws Exception;


    public void mostrarLosUsuariosQueSigues(int usuarioID) throws SQLException;

    // public void ejecutarDejarDeSeguir(Scanner scanner, int usuarioID) throws Exception;

    // public boolean comprobarUsuarioQueSiguesExistenteInt(int usuario, int usuarioID) throws Exception;

    public void mostrarLosUsuariosQueTeSiguen(int usuarioID) throws SQLException;
}
