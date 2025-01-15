package com.twitterjdbc.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.Consumer;

public interface PublicationsService {

    public void publicar(String texto, int usuarioID) throws SQLException;

    public void mostrarTusPublicaciones(int usuarioID, Consumer<ResultSet> consumidor) throws SQLException;

    public void eliminarPublicacion(int publicacionId, int usuarioID) throws SQLException;

    public void mostrarTodasLasPublicaciones(Consumer<ResultSet> consumidor) throws SQLException;

    public void mostrarPublicacionesDeUsuariosSeguidos(int usuarioID, Consumer<ResultSet> consumidor) throws SQLException;
}
