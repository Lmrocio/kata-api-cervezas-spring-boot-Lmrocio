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

