ForoHub API
ForoHub es una API REST diseñada para gestionar un foro de discusión. Este proyecto es parte del Challenge de Alura Latam, donde se implementan funcionalidades de CRUD (Create, Read, Update, Delete) para tópicos, junto con un sistema de autenticación y persistencia de datos.
Tabla de Contenidos

    Características

    Tecnologías Utilizadas

    Configuración del Entorno

    Documentación de la API

        Autenticación

        Tópicos

    Variables de Postman

✨ Características

    Autenticación de Usuarios: Registro e inicio de sesión con generación de tokens (JWT).

    Gestión de Tópicos: Creación, consulta, actualización y eliminación de hilos de discusión.

    Seguridad: Endpoints protegidos que requieren un token de acceso válido.

    Validaciones: Reglas de negocio aplicadas para evitar datos duplicados o incompletos.

Tecnologías Utilizadas

    Java / Spring Boot (Framework principal)

    Spring Security & JWT (Seguridad y Autenticación)

    PostgreSQL / MySQL (Base de datos persistente)

    Maven (Gestión de dependencias)

Configuración del Entorno

    Clona este repositorio.

    Configura tu base de datos en el archivo application.properties.

    Ejecuta la aplicación desde tu IDE o mediante la terminal:
    Bash

    ./mvnw spring-boot:run

    La API estará disponible en: http://localhost:8080.

Documentación de la API
Autenticación

Permite gestionar el acceso a la plataforma. No requiere token para estos endpoints.
Método	Endpoint	Descripción
POST	/registro	Crea un nuevo usuario en la plataforma.
POST	/login	Autentica un usuario y devuelve un Bearer Token.

    Ejemplo de Body (Login):
    JSON

    {
        "email": "usuario@email.com",
        "contrasena": "123456"
    }

Tópicos

Requiere autenticación mediante un header: Authorization: Bearer {{token}}.
Método	Endpoint	Descripción
GET	/topicos	Lista todos los tópicos registrados.
POST	/topicos	Crea un nuevo tópico.
GET	/topicos/{id}	Obtiene los detalles de un tópico específico.
PUT	/topicos/{id}	Actualiza el título o mensaje de un tópico.
DELETE	/topicos/{id}	Elimina un tópico de forma permanente.