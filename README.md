# Proyecto de Gestión de Usuarios y Publicaciones

Este proyecto es una aplicación basada en consola que permite a los usuarios 
registrarse, iniciar sesión y gestionar perfiles, publicaciones y establecer
relaciones entre usuarios. 

El proyecto está desarrollado en Java y utiliza una base de datos 
MySQL para almacenar la información llamada **social_network**.

## Características principales

### Gestión de usuarios
- **Registro de usuarios:** Los usuarios pueden registrarse proporcionando los siguientes datos:
  - Un **nombre de usuario**.
  - Un **correo electrónico**.
  - Una **descripción**.
  - Una **contraseña**.
- **Inicio de sesión:** Los usuarios pueden iniciar sesión con los siguientes datos, siempre y cuando el usuario esté registrado:
  - El **nombre de usuario**.
  - La **contraseña**.
- **Perfil de usuario:** Permite ver los detalles anteriormente indicados de tu perfil y los de otros usuarios registrados.

### Publicaciones
- **Crear publicaciones:** Los usuarios pueden publicar una cadena de texto que se almacenará en la base de datos.
- **Ver publicaciones:** Permite a los usuarios ver todas las publicaciones de la aplicación o solo las suyas.
- **Eliminar publicaciones:** Los usuarios pueden eliminar las publicaciones que ellos mismos hayan publicado.

### Relaciones entre usuarios
- **Seguir usuarios:** Los usuarios pueden seguir a otros usuarios mediante sus identificadores.
- **Dejar de seguir usuarios:** Permite dejar de seguir a otros usuarios que previamente se estaban siguiendo.
- **Mostrar usuarios seguidos:** Muestra una lista de los usuarios a los que estás siguiendo.
- **Mostrar seguidores:** Muestra una lista de los usuarios que te están siguiendo.
- **Mostrar publicaciones de usuarios seguidos**: Muestra una lista de las publicaciones de los usuarios a los que sigue el usuario.

## Estructura del código

### Métodos principales

#### Seguimiento de usuarios
- **`ejecutarSeguir()`**: Permite al usuario actual seguir a otro usuario ingresando su ID.
- **`comprobarUsuarioExistenteInt(int usuario)`**: Comprueba si un usuario con un ID específico existe en la base de datos.
- **`seguir(int usuarioASeguir)`**: Inserta un registro en la tabla `follows` indicando que el usuario actual sigue al usuario especificado.
- **`ejecutarDejarDeSeguir()`**: Permite al usuario actual dejar de seguir a otro usuario ingresando su ID.
- **`comprobarUsuarioQueSiguesExistenteInt(int usuario)`**: Comprueba si el usuario especificado está siendo seguido por el usuario actual.
- **`dejarDeSeguir(int usuarioADejarDeSeguir)`**: Elimina un registro de la tabla `follows` que indica que el usuario actual sigue al usuario especificado.

#### Visualización de relaciones
- **`mostrarLosUsuariosQueSigues()`**: Muestra una lista detallada de los usuarios que el usuario actual está siguiendo.
- **`mostrarLosUsuariosQueTeSiguen()`**: Muestra una lista detallada de los usuarios que están siguiendo al usuario actual.

#### Publicaciones de usuarios seguidos
- **`mostrarPublicacionesDeUsuariosSeguidos()`**: Muestra todas las publicaciones realizadas por los usuarios seguidos, ordenadas de más reciente a más antigua.

#### Gestión del flujo
- **`elegirRegistrarOiniciarSesion()`**: Presenta un menú inicial para que el usuario elija entre registrarse, iniciar sesión o salir de la aplicación.
- **`elegirAccionesDeUsuario()`**: Presenta un menú con todas las acciones disponibles para un usuario autenticado.

### Conexión a la base de datos
- **`conectar(String url, String usuario, String password)`**: Establece una conexión con la base de datos MySQL utilizando las credenciales proporcionadas.

### Método principal
- **`main(String[] args)`**: Punto de entrada de la aplicación, gestiona la conexión a la base de datos y llama a los métodos de inicio del programa.

## Requisitos del sistema
- **Java**: JDK 8 o superior.
- **Base de datos**: MySQL.
- **Dependencias**: 
  - MySQL Connector.
  - JBCrypt.

## Configuración
1. Configura la base de datos MySQL y crea las tablas necesarias (`users`, `publications`, `follows`).
2. Modifica las constantes `URL`, `USUARIO` y `PASSWORD` en el código para que coincidan con las credenciales de tu base de datos.
3. Compila y ejecuta el programa.

## Uso
1. Al iniciar la aplicación, elige entre registrarte o iniciar sesión.
2. Una vez autenticado, accede a las opciones para gestionar tu perfil, publicaciones y relaciones con otros usuarios.

## Autor
Desarrollado por *José Antonio Pérez de Prada*.

