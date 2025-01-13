package com.twitterjdbc.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public interface PublicationsService {
    public void ejecutarPublicar(Scanner scanner, int usuarioID) throws SQLException;

    public void publicar(String texto, int usuarioID) throws SQLException;

    public void mostrarTusPublicaciones(int usuarioID) throws SQLException;

    public void ejecutarEliminarPublicacion(Scanner scanner, int usuarioID) throws SQLException;

    public void eliminarPublicacion(int publicacionId, int usuarioID) throws SQLException;

    public void mostrarTodasLasPublicaciones() throws SQLException;

    public void mostrarPublicacionesDeUsuariosSeguidos(int usuarioID) throws SQLException;
}
