# Proyecto de Gestión de Usuarios y Publicaciones

Este proyecto es una aplicación basada en consola que permite a los usuarios registrarse, iniciar sesión y gestionar perfiles, publicaciones y establecer relaciones entre usuarios.

El proyecto está desarrollado en Java y utiliza una base de datos MySQL para almacenar la información llamada **social_network**.

## Características principales

### Gestión de usuarios
- **Registro de usuarios:** Los usuarios pueden registrarse proporcionando los siguientes datos:
  - Un **nombre de usuario**.
  - Un **correo electrónico**.
  - Una **descripción**.
  - Una **contraseña** (encriptada con BCrypt para mayor seguridad).
- **Inicio de sesión:** Los usuarios pueden iniciar sesión con los siguientes datos, siempre y cuando el usuario esté registrado:
  - El **nombre de usuario**.
  - La **contraseña** (verificada con BCrypt).
- **Perfil de usuario:** Permite ver los detalles anteriormente indicados de tu perfil y los de otros usuarios registrados.

### Publicaciones
- **Crear publicaciones:** Los usuarios pueden publicar una cadena de texto que se almacenará en la base de datos junto con la fecha y hora.
- **Ver publicaciones:** Permite a los usuarios ver todas las publicaciones de la aplicación o solo las suyas.
- **Eliminar publicaciones:** Los usuarios pueden eliminar las publicaciones que ellos mismos hayan publicado.

### Relaciones entre usuarios
- **Seguir usuarios:** Los usuarios pueden seguir a otros usuarios mediante sus identificadores.
- **Dejar de seguir usuarios:** Permite dejar de seguir a otros usuarios que previamente se estaban siguiendo.
- **Mostrar usuarios seguidos:** Muestra una lista de los usuarios a los que estás siguiendo.
- **Mostrar seguidores:** Muestra una lista de los usuarios que te están siguiendo.
- **Mostrar publicaciones de usuarios seguidos:** Muestra una lista de las publicaciones de los usuarios a los que sigue el usuario, ordenadas por fecha.

## Estructura del código

### Servicios principales

#### `UsersService`
- **`showYourProfile(int userID, Consumer<ResultSet> consumer)`**: Muestra el perfil del usuario actual.
- **`showAllProfiles(int userID, Consumer<ResultSet> consumer)`**: Muestra los perfiles de todos los usuarios excepto el usuario actual.
- **`userExistsString(String user)`**: Verifica si existe un usuario con el nombre proporcionado.
- **`showAProfile(String username, Consumer<ResultSet> consumer)`**: Muestra el perfil de un usuario específico dado su nombre.
- **`userExistsInt(int user)`**: Verifica si existe un usuario con el ID proporcionado.
- **`showFollowedUsers(int userID, Consumer<ResultSet> consumer)`**: Muestra los usuarios seguidos por el usuario actual.
- **`showYourFollowers(int userID, Consumer<ResultSet> consumer)`**: Muestra los seguidores del usuario actual.

#### `RegisterService`
- **`register(String username, String email, String password)`**: Registra un nuevo usuario con contraseña encriptada y fecha de registro.

#### `PublicationsService`
- **`post(String text, int userID)`**: Permite al usuario crear una publicación.
- **`showYourPublications(int userID, Consumer<ResultSet> consumer)`**: Muestra las publicaciones del usuario actual.
- **`deletePublication(int publicationId, int userID)`**: Permite al usuario eliminar una publicación específica.
- **`showAllPublications(Consumer<ResultSet> consumer)`**: Muestra todas las publicaciones, ordenadas por fecha.
- **`showFollowedTweets(int userID, Consumer<ResultSet> consumer)`**: Muestra las publicaciones de los usuarios seguidos por el usuario actual.

#### `LoginService`
- **`login(String username, String password)`**: Permite al usuario iniciar sesión verificando las credenciales con contraseñas encriptadas.

#### `FollowsService`
- **`userExistsInt(int user)`**: Verifica si existe un usuario con el ID especificado.
- **`userFollowdExistsInt(int user, int userID)`**: Verifica si el usuario actual ya sigue al usuario especificado.
- **`follow(int userToFollow, int userID)`**: Permite al usuario actual seguir a otro usuario.
- **`unfollow(int userToUnfollow, int userID)`**: Permite al usuario actual dejar de seguir a otro usuario.

### Implementaciones
- **`UsersServiceImpl`**: Implementación de las operaciones relacionadas con los usuarios.
- **`RegisterServiceImpl`**: Implementación del registro de usuarios con encriptación de contraseñas.
- **`PublicationsServiceImpl`**: Implementación de la gestión de publicaciones.
- **`LoginServiceImpl`**: Implementación del inicio de sesión.
- **`FollowsServiceImpl`**: Implementación de las relaciones entre usuarios, permitiendo seguir y dejar de seguir a otros usuarios.

### Controladores

Los controladores gestionan la interacción entre el usuario y los servicios. Aquí se describen los principales controladores:

#### `UsersController`
- **`showMyProfile(int userID)`**: Muestra el perfil del usuario actual.
- **`showAllProfiles(int userID)`**: Muestra los perfiles de todos los usuarios, excepto el usuario actual.
- **`showAProfile()`**: Solicita el nombre de usuario e imprime su perfil si existe.
- **`showYourFollows(int userID)`**: Muestra los perfiles de los usuarios seguidos.
- **`showYourFollowers(int userID)`**: Muestra los perfiles de los usuarios que siguen al usuario actual.

#### `RegisterController`
- **`register()`**: Permite registrar un nuevo usuario solicitando nombre, email y contraseña, encriptando la contraseña antes de almacenarla.

#### `PublicationsController`
- **`post(int userID)`**: Solicita un texto y lo publica en nombre del usuario.
- **`showYourTweets(int userID)`**: Muestra las publicaciones realizadas por el usuario.
- **`deletePublication(int userID)`**: Permite al usuario eliminar una de sus publicaciones.
- **`showTweets()`**: Muestra todas las publicaciones en el sistema, ordenadas por fecha.
- **`showFollowedTweets(int userID)`**: Muestra las publicaciones realizadas por los usuarios seguidos.

#### `LoginController`
- **`login()`**: Solicita las credenciales del usuario y valida su autenticidad.

#### `FollowsController`
- **`follow(int userID)`**: Permite al usuario seguir a otro usuario seleccionándolo de una lista.
- **`unfollow(int userID)`**: Permite al usuario dejar de seguir a otro usuario seleccionado de una lista.

### Conexión a la base de datos
- El proyecto utiliza un objeto `Connection` proporcionado al crear instancias de los servicios.
- Las consultas se realizan utilizando `PreparedStatement` y `Statement` para interactuar con la base de datos.

### Seguridad
- Las contraseñas de los usuarios se encriptan utilizando la biblioteca `BCrypt`.

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

