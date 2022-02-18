# Miarma

Miarma es una api para una red social de gente de Sevilla. Esta red social está basada en imágenes o vídeos, 
por lo que cada publicación que se realiza debe incluir siempre al menos una imagen o vídeo
El back-end ha sido diseñado con Java17 y Spring Hibernate.

## Instalación

Para probar la API de este repositorio, ejecuta los siguientes comandos:
- Clona este repositorio en local:
  ``` git clone https://github.com/Rick429/Miarma.git ```
- Entra en el directorio del proyecto:
  ``` cd Miarma ```
- Lanza el servidor para ver la aplicación en http://localhost:8080/:
``` mvn spring-boot:run ```

## Lenguajes

Para este proyecto se ha usado:
- Java

## Dependencias

Para este proyecto hemos instalado las siguientes dependencias:
- Spring JPA
- H2 Database
- Lombok
- Spring Web
- Spring Security
- Validation

## Postman

Para realizar la prueba de los endpoints importar la colección de postman y usar los archivos 
que se encuentra en la carpeta postman

## Endpoints

### Auth

| Method | Url | Decription |
| ------ | --- | ---------- |
| POST   | /auth/register | registrarse |
| POST   | /auth/login | login |

### Users

| Method | Url | Description |
| ------ | --- | ----------- |
| GET    | /me | Obtiene el perfil del usuario logueado |
| GET    | /profile/{id} | Obtiene un usuario por su id |
| PUT    | /profile/me | Edita el perfil de un usuario |
| POST   | /follow/{nick} | Se crea un petición de seguimiento al usuario con el nick dado |
| POST   | /follow/accept/{id} | Acepta una petición de seguimiento |
| POST   | /follow/decline/{id} | Rechaza una petición de seguimiento |
| GET   | /follow/list | Lista todas las peticiones de seguimiento que tiene el usuario logueado |

### Posts

| Method | Url | Description |
| ------ | --- | ----------- |
| POST   | /post/ | Crear un post |
| PUT    | /post/{id} | Se edita el post con el id dado |
| DELETE | /post/{id} | Se elimina el post con el id dado |
| GET    | /post/public | Se listan todos los posts públicos |
| GET    | /post/{id} | Obtiene un post por su id |
| GET    | /post/user/{nick} | Obtiene todos los posts del usuario con el nick dado |
| GET    | /post/me | Obtiene todos los posts del usuario logueado |

### Comments

| Method | Url | Description |
| ------ | --- | ----------- |
| POST   | /comment/{postId} | Se crea un comentario en el post con el id dado |
| DELETE | /comment/{id} | Se elimina un comentario por su id |

### Like

| Method | Url | Description |
| ------ | --- | ----------- |
| POST   | /like/{postId} | Se crea un like en el post con el id dado |
| DELETE | /like/{id} | Se elimina un like por su id |

## Desarrollador

Este proyecto ha sido realizado por:

| Richard Céspedes Pedrazas |
