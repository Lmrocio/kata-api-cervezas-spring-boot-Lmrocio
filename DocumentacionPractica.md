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

