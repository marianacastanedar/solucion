# Rick and Morty Android App

Aplicación Android desarrollada con Jetpack Compose que implementa una arquitectura limpia con patrón MVI para mostrar personajes y locaciones del universo de Rick and Morty.

## Arquitectura

Este proyecto implementa una **arquitectura limpia con patrón MVI (Model-View-Intent)** organizada en tres capas principales:

### Capas

```
┌─────────────────────────────────────┐
│     PRESENTACIÓN (UI)               │
│  • Screens (Composables)            │
│  • ViewModels (MVI)                 │
│  • Estados y Eventos                │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│     REPOSITORIOS                    │
│  • CharacterRepository              │
│  • LocationRepository               │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│     DATOS                           │
│  • Room Database                    │
│  • DataStore Preferences            │
│  • Mappers                          │
└─────────────────────────────────────┘
```

### Patrón MVI

Cada feature sigue el patrón **MVI (Model-View-Intent)**:

- **Model (State)**: Estado inmutable que representa la UI
  ```kotlin
  data class CharactersScreenState(
      val isLoading: Boolean = true,
      val isError: Boolean = false,
      val data: List<CharacterUi> = listOf()
  )
  ```

- **View (Screen)**: Composables que renderizan el estado
  ```kotlin
  @Composable
  fun CharacterListScreen(
      isLoading: Boolean,
      isError: Boolean,
      characters: List<CharacterUi>,
      onCharacterClick: (Int) -> Unit,
      onRetryClick: () -> Unit
  )
  ```

- **Intent (Events/Actions)**: Acciones del usuario que modifican el estado
  ```kotlin
  sealed interface LoginScreenEvent {
      data class UpdateName(val name: String) : LoginScreenEvent
      data object LogInClick : LoginScreenEvent
  }
  ```

### Estructura de Pantallas

Todas las pantallas siguen la misma estructura de dos pasos:

1. **Route Function**: Define la ruta en el grafo de navegación
   ```kotlin
   fun NavGraphBuilder.characterListRoute(
       onCharacterClick: (Int) -> Unit,
   ) {
       composable<CharacterRoutes.CharacterList> {
           val viewModel: CharactersViewModel = viewModel()
           val state by viewModel.state.collectAsStateWithLifecycle()
           CharacterListScreen(
               isLoading = state.isLoading,
               isError = state.isError,
               characters = state.data,
               onCharacterClick = onCharacterClick,
               onRetryClick = viewModel::fetchData
           )
       }
   }
   ```

2. **Screen Composable**: Funci�n privada que contiene la UI
   ```kotlin
   @Composable
   private fun CharacterListScreen(
       isLoading: Boolean,
       isError: Boolean,
       characters: List<CharacterUi>,
       onCharacterClick: (Int) -> Unit,
       onRetryClick: () -> Unit,
       modifier: Modifier = Modifier
   ) {
       // UI implementation
   }
   ```

Esta separación permite:
- Testear la UI de forma aislada
- Reutilizar composables en diferentes contextos
- Mantener una clara separación entre navegación y UI

## Navegación Type-Safe

El proyecto utiliza **Navigation Compose con serialización de Kotlin** para navegación completamente type-safe.

### Estructura de Rutas

#### 1. Rutas Principales (AppRoutes)
```kotlin
@Serializable
sealed interface AppRoutes {
    @Serializable
    data object Splash : AppRoutes

    @Serializable
    data object Login : AppRoutes

    @Serializable
    data object LoggedFlow : AppRoutes
}
```

#### 2. Rutas del Flujo Autenticado (LoggedFlowRoutes)
```kotlin
@Serializable
sealed interface LoggedFlowRoutes {
    @Serializable
    data object CharactersGraph : LoggedFlowRoutes

    @Serializable
    data object LocationsGraph : LoggedFlowRoutes

    @Serializable
    data object Profile : LoggedFlowRoutes
}
```

#### 3. Rutas de Features

**Characters:**
```kotlin
@Serializable
sealed interface CharacterRoutes {
    @Serializable
    data object CharacterList : CharacterRoutes

    @Serializable
    data class CharacterProfile(val id: Int) : CharacterRoutes
}
```

**Locations:**
```kotlin
@Serializable
sealed interface LocationRoutes {
    @Serializable
    data object LocationList : LocationRoutes

    @Serializable
    data class LocationDetail(val id: Int) : LocationRoutes
}
```

### Flujo de Navegación

```
App Start
    ↓
[Splash] → Verifica autenticación
    ↓
      Si LoggedOut → [Login]
                        ↓
                     [LoggedFlow]
    
      Si LoggedIn → [LoggedFlow]
                        ↓
                        ↓               
        ↓               ↓               ↓
   [Characters]    [Locations]     [Profile]
        ↓               ↓
    [List]          [List]
        ↓               ↓
   [Profile(id)]   [Detail(id)]
```

### Ventajas de Type-Safe Navigation

- **Seguridad en compile-time**: Errores de navegación detectados al compilar
- **Autocompletado**: IDE sugiere rutas y parámetros disponibles
- **Refactoring seguro**: Cambiar nombres de rutas actualiza todas las referencias
- **Parámetros tipados**: Los IDs se pasan como Int, no como String

## Persistencia de Datos

### Room Database

Base de datos local usando **Room** para almacenar personajes y locaciones.

#### AppDatabase (Singleton)
```kotlin
@Database(
    entities = [CharacterEntity::class, LocationEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun locationDao(): LocationDao
}
```

#### Entities
```kotlin
@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val type: String,
    val dimension: String
)
```

#### DAOs
```kotlin
@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity
}
```

### DataStore Preferences

**DataStore** se utiliza para almacenar preferencias del usuario de forma segura.

```kotlin
class UserPreferences(private val dataStore: DataStore<Preferences>) {
    private val usernameKey = stringPreferencesKey("username")

    val username: Flow<String> = dataStore.data.map { preferences ->
        preferences[usernameKey] ?: ""
    }

    suspend fun updateUsername(name: String) {
        dataStore.edit { preferences ->
            preferences[usernameKey] = name
        }
    }

    suspend fun clearUsername() {
        dataStore.edit { preferences ->
            preferences.remove(usernameKey)
        }
    }
}
```

### Mappers

Los mappers convierten entre entidades de base de datos y modelos de UI:

```kotlin
// Entity → UI Model
fun CharacterEntity.toModel(): CharacterUi {
    return CharacterUi(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        image = this.image
    )
}

// UI Model → Entity
fun CharacterUi.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        image = this.image
    )
}
```

## Repositorios

Los repositorios encapsulan la lógica de acceso a datos:

```kotlin
class CharacterRepository(application: Application) {
    private val database = AppDatabase.getDatabase(application)

    suspend fun getCharacters(): List<CharacterUi> {
        val characterEntities = database.characterDao().getAllCharacters()
        return characterEntities.map { it.toModel() }
    }

    suspend fun getCharacter(id: Int): CharacterUi {
        val characterEntity = database.characterDao().getCharacterById(id)
        return characterEntity.toModel()
    }
}
```

**Ventajas:**
- ViewModels solo interactúan con repositorios, no con la base de datos directamente
- Facilita testing (se pueden mockear los repositorios)
- Centraliza la lógica de transformación de datos

## Features Implementadas

### 1. Splash Screen
- Verifica el estado de autenticación al iniciar la app
- Navega a Login o LoggedFlow según el estado

### 2. Login
- Permite al usuario ingresar su nombre
- Persiste el nombre en DataStore
- Carga datos iniciales (20 personajes y 20 locaciones) en Room
- Navega al flujo autenticado

**Estados:**
```kotlin
data class LoginScreenState(
    val name: String = "",
    val syncing: Boolean = false,
    val finishedSync: Boolean = false
)
```

### 3. Characters Feature

#### Characters List
- Muestra lista de 20 personajes
- Estados: Loading, Error, Success
- Navegación al perfil del personaje al hacer click

#### Character Profile
- Muestra detalles de un personaje específico
- Recibe ID del personaje como parámetro de ruta
- Datos: Nombre, Especie, Estado, Género

### 4. Locations Feature

#### Locations List
- Muestra lista de 20 locaciones
- Estados: Loading, Error, Success
- Navegación al detalle de localización al hacer click

#### Location Detail
- Muestra detalles de una localización específica
- Recibe ID de localización como parámetro de ruta
- Datos: ID, Nombre, Tipo, Dimensión

### 5. Profile
- Muestra información del usuario (nombre y carné)
- Opción de cerrar sesión
- Al hacer logout, limpia DataStore y navega a Login

## Componentes Reutilizables

### LoadingLayout
```kotlin
@Composable
fun LoadingLayout(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text("Cargando...")
    }
}
```

### ErrorLayout
```kotlin
@Composable
fun ErrorLayout(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "Error",
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Error al obtener información")
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onRetryClick) {
            Text("Reintentar")
        }
    }
}
```

## Flujo de Datos Completo

### Ejemplo: Login → Ver Personaje

```
1. Login Screen
   ↓ Usuario ingresa nombre
   ↓ LoginViewModel.onLoginClick()
      ↓ Guarda username en DataStore
      ↓ Obtiene 20 personajes de CharacterDb
      ↓ Convierte a CharacterEntity con mappers
      ↓ Inserta en Room via characterDao
      ↓ Hace lo mismo con locaciones
      ↓ Navega a LoggedFlow

2. Characters List Screen
   ↓ CharactersViewModel.fetchData()
      ↓ CharacterRepository.getCharacters()
         ↓ characterDao.getAllCharacters()
         ↓ Convierte a CharacterUi con mappers
         ↓ Actualiza state.data
            ↓ UI renderiza LazyColumn

3. Usuario hace click en personaje
   ↓ Navega a CharacterProfile(id = 2)
      ↓ CharacterProfileViewModel.fetchData(2)
         ↓ CharacterRepository.getCharacter(2)
            ↓ characterDao.getCharacterById(2)
            ↓ Convierte a CharacterUi
            ↓ Actualiza state.data
               ↓ UI renderiza detalles
```

## Conceptos Clave

### 1. MVI Pattern
- **Un solo estado inmutable** por pantalla
- **Eventos** representan acciones del usuario
- **ViewModels** transforman eventos en nuevos estados
- **UI** renderiza según el estado actual

### 2. Type-Safe Navigation
- Las rutas son **objetos Kotlin**, no strings
- Los parámetros son **tipados** (Int, String, etc.)
- **Compile-time safety**: errores detectados al compilar
- Usa `@Serializable` para rutas y parámetros

### 3. Separation of Concerns
- **Screens**: Solo UI, no lógica de negocio
- **ViewModels**: Manejo de estado y lógica de UI
- **Repositories**: Acceso a datos
- **DAOs**: Queries de base de datos
- **Mappers**: Transformaciones de datos

### 4. Reactive Programming
- **StateFlow**: Flujo de estado observable
- **collectAsStateWithLifecycle**: Observa cambios respetando lifecycle
- **viewModelScope**: Corrutinas ligadas al lifecycle del ViewModel
- **suspend functions**: Operaciones asíncronas

### 5. Dependency Injection (Manual)
- AppDatabase es **singleton**
- ViewModels reciben `Application` como dependencia
- Repositories reciben `Application` para obtener database
- DataStore se inyecta usando extension property

## Buenas Prácticas Implementadas

- Separación clara de responsabilidades
- Estados inmutables en ViewModels
- Componentes UI reutilizables
- Navegación type-safe
- Manejo apropiado de estados (Loading/Error/Success)
- Uso de corrutinas para operaciones asíncronas
- Mappers para separar capas de datos y UI
- DataStore para preferencias (mejor que SharedPreferences)
- Room para persistencia estructurada
- Lifecycle-aware components

## Notas

- Los datos son **mock data** (no hay API real)
- La carga de datos simula éxito/error aleatoriamente (50/50)
- Los delays están implementados para simular latencia de red
- Las imágenes de personajes usan URLs del mock data

---
