# Documentación de la Práctica - API Cervezas

## Preparación de la Base de Datos

### Decisión inicial

Para esta práctica, decidí usar Docker Compose en lugar de instalar MySQL localmente. Esto simplifica el proceso de setup y permite que otros desarrolladores puedan replicar el entorno de forma exacta.

### Proceso realizado

1. **Verificación de Docker Desktop**: Comprobé que tenía Docker Desktop instalado y activo en mi máquina.

2. **Levantamiento de los contenedores**: Ejecuté el comando `docker-compose up -d` desde el directorio raíz del proyecto. El archivo `docker-compose.yml` ya estaba configurado con:
   - Un contenedor de MariaDB que crea automáticamente la base de datos `kata-api`
   - Un contenedor de Adminer para gestionar la base de datos de forma visual
   - Los volúmenes configurados para ejecutar automáticamente los scripts SQL del directorio `initSQL`

3. **Ejecución automática de scripts SQL**: Docker Compose ejecutó automáticamente los scripts SQL en este orden:
   - `01-create-db.sql`: Creó la base de datos `kata-api` con la codificación utf8mb4
   - `beers.sql`: Creó la tabla de cervezas
   - `breweries.sql`: Creó la tabla de cerveceras
   - `categories.sql`: Creó la tabla de categorías
   - `styles.sql`: Creó la tabla de estilos

### Acceso a la base de datos

Una vez levantados los contenedores, la base de datos está disponible en:

- **Host**: localhost
- **Puerto**: 3306
- **Usuario**: root
- **Contraseña**: Super
- **Base de datos**: kata-api

Además, tengo acceso a una interfaz web de Adminer en `http://localhost:8888` para gestionar la base de datos de forma visual si lo necesito.


<img width="1913" height="601" alt="Captura de pantalla 2025-12-14 173936" src="https://github.com/user-attachments/assets/76210ea4-a5fa-4cb8-b262-e5906bccfa8d" />



### Credenciales y conexión

Las credenciales están definidas en el archivo `docker-compose.yml`:
- Usuario: `root`
- Contraseña: `Super`
- Base de datos: `kata-api`


### Comandos útiles

Para levantar los contenedores:
```bash
docker-compose up -d
```

Para ver los logs del contenedor de base de datos:
```bash
docker-compose logs db
```

Para detener los contenedores:
```bash
docker-compose down
```

---

## Creación de la API REST en Spring Boot

### Decisión de tecnología

Decidí usar Spring Boot 3.1.5 como framework principal porque es una herramienta robusta y estándar en la industria Java para crear APIs REST. Utilicé:
- Spring Boot Web para los controladores REST
- Spring Data JPA con Hibernate para el acceso a la base de datos
- Lombok para reducir código boilerplate
- Maven como gestor de dependencias

### Estructura del proyecto

Creé la siguiente estructura organizando el código en capas:

```
src/main/java/com/example/kataapi/
├── KataApiApplication.java      (Clase principal de Spring Boot)
├── entity/                       (Entidades JPA mapeadas a tablas)
│   ├── Beer.java
│   ├── Brewery.java
│   ├── Category.java
│   └── Style.java
├── repository/                   (Interfaces de acceso a datos)
│   ├── BeerRepository.java
│   ├── BreweryRepository.java
│   ├── CategoryRepository.java
│   └── StyleRepository.java
└── controller/                   (Controladores REST)
    ├── BeerController.java
    ├── BreweryController.java
    ├── CategoryController.java
    └── StyleController.java

src/main/resources/
└── application.properties         (Configuración de la aplicación)
```

### Configuración de la base de datos

En el archivo `application.properties` configuré:
- **URL de conexión**: `jdbc:mysql://localhost:3306/kata-api`
- **Usuario**: root
- **Contraseña**: Super
- **Dialect de Hibernate**: MySQL8Dialect
- **DDL Auto**: update (para que Hibernate cree las tablas automáticamente)
- **Context path**: /api (todos los endpoints van bajo `/api`)

### Entidades creadas

Creé cuatro entidades JPA que mapean las tablas de la base de datos usando anotaciones de Jakarta Persistence:
- **Beer**: Representa una cerveza con campos como id, name, breweryId, categoryId, styleId, abv, ibu, description
- **Brewery**: Representa una cervecera con campos como id, name, address1, address2, city, state, country, phone, website
- **Category**: Representa una categoría con campos como id, catName
- **Style**: Representa un estilo con campos como id, catId, styleName

Todas utilizan Lombok para generar getters, setters y constructores automáticamente.

### Repositorios implementados

Creé cuatro repositorios extendiendo `JpaRepository`:
- **BeerRepository**: Para operaciones CRUD completas en cervezas
- **BreweryRepository**: Para operaciones básicas en cerveceras
- **CategoryRepository**: Para operaciones básicas en categorías
- **StyleRepository**: Para operaciones básicas en estilos

Spring Data JPA genera automáticamente la implementación de estos repositorios, proporcionando métodos como `findAll()`, `findById()`, `save()`, `delete()`, etc.

### Controladores REST implementados

#### BeerController (CRUD completo)
- `GET /api/beers` - Obtener todas las cervezas
- `GET /api/beers/{id}` - Obtener una cerveza por ID
- `POST /api/beers` - Crear una nueva cerveza
- `PUT /api/beers/{id}` - Actualizar una cerveza (total o parcial)
- `DELETE /api/beers/{id}` - Eliminar una cerveza

#### BreweryController (Solo lectura)
- `GET /api/breweries` - Obtener todas las cerveceras
- `GET /api/breweries/{id}` - Obtener una cervecera por ID

#### CategoryController (Solo lectura)
- `GET /api/categories` - Obtener todas las categorías
- `GET /api/categories/{id}` - Obtener una categoría por ID

#### StyleController (Solo lectura)
- `GET /api/styles` - Obtener todos los estilos
- `GET /api/styles/{id}` - Obtener un estilo por ID

### Características especiales del PUT

El método PUT en BeerController maneja tanto actualizaciones totales como parciales:
- Si se envían todos los campos, actualiza la cerveza completa
- Si se envían solo algunos campos (nullables), solo actualiza esos campos
- Devuelve 404 si la cerveza no existe
- Devuelve 201 CREATED si se crea exitosamente
- Devuelve 204 NO CONTENT si se elimina exitosamente

### Compilación y ejecución

Para compilar el proyecto:
```bash
mvn clean compile
```

Para ejecutar el proyecto:
```bash
mvn spring-boot:run
```

O compilar a JAR y ejecutar:
```bash
mvn clean package
java -jar target/kata-api-cervezas-1.0.0.jar
```

La aplicación estará disponible en:
```
http://localhost:8080/api
```

Por ejemplo, para obtener todas las cervezas:
```
http://localhost:8080/api/beers
```

### Dependencias principales

El archivo `pom.xml` incluye:
- `spring-boot-starter-web`: Para crear los controladores REST
- `spring-boot-starter-data-jpa`: Para el acceso a la base de datos con Hibernate
- `mysql-connector-java`: Driver para MySQL/MariaDB
- `spring-boot-starter-validation`: Para validaciones
- `lombok`: Para reducir boilerplate de getters/setters

---

## Uso de la API REST

### Punto de acceso base

Todos los endpoints de la API están disponibles bajo:
```
http://localhost:8080/api
```

### Endpoints de cervezas (CRUD completo)

#### 1. Obtener todas las cervezas

**Endpoint:**
```
GET /api/beers
```

**Descripción:** Retorna una lista de todas las cervezas en la base de datos.

**Ejemplo de respuesta:**
```json
[
  {
    "id": 1,
    "name": "Cerveza de ejemplo",
    "breweryId": 1,
    "catId": 1,
    "styleId": 1,
    "abv": 5.0,
    "ibu": 20.0,
    "srm": 10.0,
    "upc": 123456789,
    "filepath": "/images/cerveza1.jpg",
    "descript": "Cerveza rubia artesanal",
    "addUser": 1,
    "lastMod": "2025-12-14T19:30:00.000+00:00"
  },
  {
    "id": 2,
    "name": "Cerveza Premium",
    "breweryId": 2,
    "catId": 2,
    "styleId": 5,
    "abv": 7.0,
    "ibu": 40.0,
    "srm": 15.0,
    "upc": 987654321,
    "filepath": "/images/cerveza2.jpg",
    "descript": "Cerveza premium con cuerpo robusto",
    "addUser": 2,
    "lastMod": "2025-12-14T19:35:00.000+00:00"
  }
]
```

**Código de estado HTTP:**
- 200 OK: La solicitud fue exitosa

---

#### 2. Obtener una cerveza por ID

**Endpoint:**
```
GET /api/beers/{id}
```

**Parámetros:**
- `id` (path parameter): ID de la cerveza a obtener

**Ejemplo:**
```
GET /api/beers/1
```

**Ejemplo de respuesta:**
```json
{
  "id": 1,
  "name": "Cerveza de ejemplo",
  "breweryId": 1,
  "catId": 1,
  "styleId": 1,
  "abv": 5.0,
  "ibu": 20.0,
  "srm": 10.0,
  "upc": 123456789,
  "filepath": "/images/cerveza1.jpg",
  "descript": "Cerveza rubia artesanal",
  "addUser": 1,
  "lastMod": "2025-12-14T19:30:00.000+00:00"
}
```

**Códigos de estado HTTP:**
- 200 OK: La cerveza fue encontrada
- 404 Not Found: La cerveza con ese ID no existe

---

#### 3. Crear una nueva cerveza

**Endpoint:**
```
POST /api/beers
```

**Headers requeridos:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "Cerveza de ejemplo",
  "breweryId": 1,
  "catId": 1,
  "styleId": 1,
  "abv": 5.0,
  "ibu": 20.0,
  "srm": 10.0,
  "upc": 123456789,
  "filepath": "/images/cerveza1.jpg",
  "descript": "Cerveza rubia artesanal",
  "addUser": 1
}
```

**Campos opcionales:** Todos los campos pueden omitirse, excepto que si se omiten tendrán valores por defecto (0 para números, cadena vacía para strings).

**Ejemplo de respuesta:**
```json
{
  "id": 100,
  "name": "Cerveza de ejemplo",
  "breweryId": 1,
  "catId": 1,
  "styleId": 1,
  "abv": 5.0,
  "ibu": 20.0,
  "srm": 10.0,
  "upc": 123456789,
  "filepath": "/images/cerveza1.jpg",
  "descript": "Cerveza rubia artesanal",
  "addUser": 1,
  "lastMod": "2025-12-14T19:50:00.000+00:00"
}
```

**Códigos de estado HTTP:**
- 201 Created: La cerveza fue creada exitosamente
- 500 Internal Server Error: Hubo un error al crear la cerveza

---

#### 4. Actualizar una cerveza (PUT - actualizaciones totales y parciales)

**Endpoint:**
```
PUT /api/beers/{id}
```

**Parámetros:**
- `id` (path parameter): ID de la cerveza a actualizar

**Headers requeridos:**
```
Content-Type: application/json
```

**Actualización parcial** (solo algunos campos):
```json
{
  "abv": 6.0,
  "ibu": 30.0
}
```

**Actualización total** (todos los campos):
```json
{
  "name": "Cerveza Premium",
  "breweryId": 2,
  "catId": 2,
  "styleId": 5,
  "abv": 7.0,
  "ibu": 40.0,
  "srm": 15.0,
  "upc": 987654321,
  "filepath": "/images/cerveza2.jpg",
  "descript": "Cerveza premium con cuerpo robusto",
  "addUser": 2
}
```

**Ejemplo de respuesta:**
```json
{
  "id": 1,
  "name": "Cerveza Premium",
  "breweryId": 2,
  "catId": 2,
  "styleId": 5,
  "abv": 7.0,
  "ibu": 40.0,
  "srm": 15.0,
  "upc": 987654321,
  "filepath": "/images/cerveza2.jpg",
  "descript": "Cerveza premium con cuerpo robusto",
  "addUser": 2,
  "lastMod": "2025-12-14T19:55:00.000+00:00"
}
```

**Códigos de estado HTTP:**
- 200 OK: La cerveza fue actualizada exitosamente
- 404 Not Found: La cerveza con ese ID no existe

---

#### 5. Eliminar una cerveza

**Endpoint:**
```
DELETE /api/beers/{id}
```

**Parámetros:**
- `id` (path parameter): ID de la cerveza a eliminar

**Ejemplo:**
```
DELETE /api/beers/1
```

**Códigos de estado HTTP:**
- 204 No Content: La cerveza fue eliminada exitosamente
- 404 Not Found: La cerveza con ese ID no existe

---

### Endpoints de cerveceras (solo lectura)

#### 1. Obtener todas las cerveceras

**Endpoint:**
```
GET /api/breweries
```

**Descripción:** Retorna una lista de todas las cerveceras.

**Códigos de estado HTTP:**
- 200 OK: La solicitud fue exitosa

---

#### 2. Obtener una cervecera por ID

**Endpoint:**
```
GET /api/breweries/{id}
```

**Parámetros:**
- `id` (path parameter): ID de la cervecera

**Códigos de estado HTTP:**
- 200 OK: La cervecera fue encontrada
- 404 Not Found: La cervecera con ese ID no existe

---

### Endpoints de categorías (solo lectura)

#### 1. Obtener todas las categorías

**Endpoint:**
```
GET /api/categories
```

**Códigos de estado HTTP:**
- 200 OK: La solicitud fue exitosa

---

#### 2. Obtener una categoría por ID

**Endpoint:**
```
GET /api/categories/{id}
```

**Parámetros:**
- `id` (path parameter): ID de la categoría

**Códigos de estado HTTP:**
- 200 OK: La categoría fue encontrada
- 404 Not Found: La categoría con ese ID no existe

---

### Endpoints de estilos (solo lectura)

#### 1. Obtener todos los estilos

**Endpoint:**
```
GET /api/styles
```

**Códigos de estado HTTP:**
- 200 OK: La solicitud fue exitosa

---

#### 2. Obtener un estilo por ID

**Endpoint:**
```
GET /api/styles/{id}
```

**Parámetros:**
- `id` (path parameter): ID del estilo

**Códigos de estado HTTP:**
- 200 OK: El estilo fue encontrado
- 404 Not Found: El estilo con ese ID no existe

---

## Pruebas de la API

Para probar la API, utilicé la herramienta Postman junto con Newman, que permite ejecutar colecciones de Postman desde la línea de comandos y generar reportes HTML automatizados.

### Colección de pruebas

La colección contiene todas las operaciones CRUD para cervezas, así como peticiones GET para obtener cerveceras, categorías y estilos.

### Ejecución de pruebas

Ejecuté las pruebas usando Newman con el siguiente comando:

```bash
newman run KataCervezas.postman_collection.json -r html
```

Este comando genera un reporte HTML con el resultado de todas las pruebas realizadas.

### Reporte de evidencias

El reporte HTML completo con todas las respuestas de la API está disponible en:

[Reporte de pruebas - Kata Cervezas API](https://lmrocio.github.io/kata-api-cervezas-spring-boot-Lmrocio/newman/Kata%20Cervezas%20API-2025-12-14-19-38-49-056-0.html)

Este reporte contiene:
- Resultado de cada petición (éxito o fallo)
- Código de estado HTTP retornado
- Tiempo de respuesta
- Headers de respuesta
- Body de respuesta en formato JSON
- Información de validaciones ejecutadas

Todas las pruebas de lectura (GET) y operaciones CRUD en cervezas fueron exitosas, demostrando que la API funciona correctamente y cumple con los requisitos de la práctica.

