# 📚 Sistema Classroom

Sistema de gestión de tareas académicas desarrollado en Java con Swing.

---

## 👥 Distribución del equipo

| Integrante | Responsabilidad                              | Paquete(s)                        |
|------------|----------------------------------------------|-----------------------------------|
| **Josué**  | Base de datos, tablas, conexión JDBC          | `bd/`, `database.sql`             |
| **Joel**   | Entidades del modelo y DAOs (backend)         | `modelo/`, `modelo/dao/`          |
| **Marco**  | Validaciones de datos                         | `validaciones/`                   |
| **Danner** | Controladores + parte de la Vista (Docente)   | `controlador/`, `vista/` (Docente)|
| **Cristian**| Vista del Estudiante + integración UI        | `vista/` (Estudiante), `main/`    |

---

## 📋 Requisitos previos

- **Java JDK 25** (o JDK 17+ compatible)
- **MySQL 8.x** (o MariaDB 10.x)
- **Eclipse IDE** con soporte para Java 17+
- **Driver JDBC:** `mysql-connector-j-8.x.x.jar`

---

## ⚙️ Configuración de la base de datos

1. Abra MySQL Workbench o su cliente SQL preferido.
2. Ejecute el script completo:
   ```
   database.sql
   ```
3. Esto creará la base de datos `classroom_db` con tablas y datos de prueba.

**Credenciales por defecto** (editar en `bd/Conexion.java` si son distintas):
```
Host:     localhost
Puerto:   3306
BD:       classroom_db
Usuario:  root
Clave:    (vacía)
```

---

## 🚀 Configuración del proyecto en Eclipse

1. **Importar proyecto:**
   - `File → Import → General → Existing Projects into Workspace`
   - Seleccionar la carpeta `classroom/`

2. **Añadir el driver MySQL:**
   - Descargar `mysql-connector-j-8.x.x.jar` de [dev.mysql.com](https://dev.mysql.com/downloads/connector/j/)
   - Clic derecho al proyecto → `Build Path → Add External Archives…`
   - Seleccionar el `.jar` descargado

3. **Configurar `module-info.java`:**
   - Si hay errores con JPMS, puede eliminar `src/module-info.java`
   - O agregar `requires mysql.connector.j;` si el conector lo expone como módulo

4. **Ejecutar:**
   - Clic derecho en `main/Main.java` → `Run As → Java Application`

---

## 📦 Generar ejecutable (.jar y .exe)

### Paso 1 – Exportar como JAR ejecutable:
1. Clic derecho al proyecto → `Export → Java → Runnable JAR File`
2. `Launch configuration:` Main - classroom
3. `Export destination:` `classroom.jar`
4. `Library handling:` **Package required libraries into generated JAR**
5. Clic en **Finish**

### Paso 2 – Convertir a .exe (Windows):
Usando [**Launch4j**](https://launch4j.sourceforge.net/) (gratuito):
1. Descargue e instale Launch4j
2. Configure:
   - `Output file:` `Classroom.exe`
   - `Jar:` ruta al `classroom.jar`
   - `Min JRE version:` `17.0.0`
3. Clic en el ícono de engranaje para generar el `.exe`

---

## 🗂️ Estructura del proyecto

```
classroom/
├── database.sql                    ← Script SQL (Josué)
├── archivos/
│   ├── tareas/                     ← Adjuntos de tareas
│   └── entregas/                   ← Archivos entregados por estudiantes
└── src/
    ├── module-info.java            ← Módulo JPMS (Josué)
    ├── bd/
    │   └── Conexion.java           ← Conexión JDBC (Josué)
    ├── modelo/
    │   ├── Estudiante.java         ← (Joel)
    │   ├── Docente.java
    │   ├── Materia.java
    │   ├── Tarea.java
    │   ├── Entrega.java
    │   ├── Calificacion.java
    │   └── dao/
    │       ├── EstudianteDAO.java  ← (Joel)
    │       ├── DocenteDAO.java
    │       ├── MateriaDAO.java
    │       ├── TareaDAO.java
    │       ├── EntregaDAO.java
    │       └── CalificacionDAO.java
    ├── validaciones/
    │   └── Validaciones.java       ← (Marco)
    ├── controlador/
    │   ├── TareaControlador.java   ← (Danner)
    │   ├── EntregaControlador.java
    │   ├── CalificacionControlador.java
    │   └── EstudianteControlador.java
    ├── vista/
    │   ├── Estilos.java            ← Tema visual (Danner)
    │   ├── VentanaPrincipal.java   ← Ventana principal (Danner)
    │   ├── PanelDocente.java       ← Tabs del Docente (Danner)
    │   ├── PanelEstudiante.java    ← Tabs del Estudiante (Cristian)
    │   └── paneles/
    │       ├── PanelCrearTarea.java        ← HU-1 (Cristian)
    │       ├── PanelCalificarTareas.java   ← HU-3 (Danner)
    │       ├── PanelBuscarEstudiante.java  ← HU-5 (Danner)
    │       ├── PanelMisTareas.java         ← HU-6 (Cristian)
    │       ├── PanelEntregarTarea.java     ← HU-2 (Cristian)
    │       └── PanelMisNotas.java          ← HU-4, HU-8 (Cristian)
    └── main/
        └── Main.java               ← Punto de entrada (integración)
```

---

## 📖 Historias de usuario implementadas

| HU | Funcionalidad               | Rol       | Panel                    |
|----|----------------------------|-----------|--------------------------|
| 1  | Crear/asignar tareas       | Docente   | PanelCrearTarea          |
| 2  | Entregar tareas            | Estudiante| PanelEntregarTarea       |
| 3  | Calificar tareas           | Docente   | PanelCalificarTareas     |
| 4  | Revisar notas              | Estudiante| PanelMisNotas            |
| 5  | Buscar estudiantes         | Docente   | PanelBuscarEstudiante    |
| 6  | Ver tareas asignadas       | Estudiante| PanelMisTareas           |
| 8  | Ver promedio               | Estudiante| PanelMisNotas            |

---

## ⚠️ Notas importantes

- **Sin login:** El sistema no requiere autenticación. Basta con seleccionar
  el rol y usuario en la barra superior de la ventana principal.
- **Tamaño de archivo:** Según la HU-2, el mínimo es **10 MB** y el máximo **1 GB**.
  Para pruebas, este valor está en `Validaciones.java` como constante modificable.
- **Archivos adjuntos:** Se almacenan localmente en `archivos/tareas/` y
  `archivos/entregas/` relativo al directorio de ejecución.
