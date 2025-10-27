package gt.uvg.lab6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gt.uvg.lab6.ui.theme.Lab6Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab6Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Asi lo habia hecho yo, pero no es la mejor forma de hacerlo ya que compose
                    // dibujaria TODA la lista de cero al agregar un elemento
                    // var history by remember { mutableStateOf(listOf<HistoryItem>()) }

                    // Esta es la mejor forma de hacerlo, ya que aqui no reescribimos la lista
                    // y compose solo dibuja el nuevo elemento y recicla el resto de la lista
                    val history = remember { mutableStateListOf<HistoryItem>()}
                    ScreenContent(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        history = history,
                        onHistoryAdd = { item ->
                            history.add(item)
                        },
                        onResetClick = {
                            history.clear()
                        }
                    )
                }
            }
        }
    }
}

/**
 * Representa un elemento del historial de cambios del contador.
 * 
 * @param isIncrement true si el cambio fue un incremento, false si fue un decremento
 * @param number el valor del contador después del cambio
 */
data class HistoryItem(
    val isIncrement: Boolean,
    val number: Int
)

/**
 * Componente principal de la pantalla que muestra la interfaz del contador.
 * 
 * Incluye:
 * - Botones de incremento y decremento
 * - Visualización del valor actual
 * - Estadísticas del historial
 * - Grilla con el historial de cambios
 * - Botón de reinicio
 * 
 * @param history lista de elementos del historial
 * @param onHistoryAdd callback para agregar un nuevo elemento al historial
 * @param onResetClick callback para reiniciar el contador y limpiar el historial
 * @param modifier modificador para el componente
 */
@Composable
fun ScreenContent(
    history: List<HistoryItem>,
    onHistoryAdd: (HistoryItem) -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Juan Carlos Durini",
                style = MaterialTheme.typography.headlineMedium
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        onHistoryAdd(
                            HistoryItem(
                                isIncrement = false,
                                /*
                                    Obtiene el último valor, pero si está vacía retorna null.
                                    Si retorna null, lo deja como 0 y a eso, le resta 1
                                 */
                                number = (history.lastOrNull()?.number ?: 0) - 1
                            )
                        )
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = null
                    )

                }
                Text(
                    /*
                        Crea una lista SOLO con los numeros y obtiene el ultimo.
                        Si la lista esta vacia, retorna null, y si retorna null, deja 0
                     */
                    text = (history.map { it.number }.lastOrNull()?: 0).toString(),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = {
                        onHistoryAdd(
                            HistoryItem(
                                isIncrement = true,
                                /*
                                    Obtiene el último valor, pero si está vacía retorna null.
                                    Si retorna null, lo deja como 0 y a eso, le suma 1
                                 */
                                number = (history.lastOrNull()?.number ?: 0) + 1
                            )
                        )
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                       Icons.Default.Add,
                        contentDescription = null
                    )

                }
            }
            HorizontalDivider()
            ItemStats(
                name = "Total incrementos:",
                value = history.map { it.isIncrement }.count { it },
                modifier = Modifier.fillMaxWidth()
            )
            ItemStats(
                name = "Total decrementos:",
                value = history.map { it.isIncrement }.count { !it },
                modifier = Modifier.fillMaxWidth()
            )
            ItemStats(
                name = "Valor máximo:",
                value = history.maxOfOrNull { it.number } ?: 0, // Si la lista está vacía, sería null y luego, si es null, sera 0
                modifier = Modifier.fillMaxWidth()
            )
            ItemStats(
                name = "Valor mínimo:",
                value = history.minOfOrNull { it.number } ?: 0, // Si la lista está vacía, sería null y luego, si es null, sera 0
                modifier = Modifier.fillMaxWidth()
            )
            ItemStats(
                name = "Total cambios:",
                value = history.size,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Historial:",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(history) {
                    ChangeItem(
                        isIncrement = it.isIncrement,
                        number = it.number
                    )
                }
            }
        }
        Button(
            onClick = onResetClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "Reiniciar")
        }
    }
}

/**
 * Componente que representa un elemento individual del historial.
 * Muestra el número con un fondo de color que indica si fue incremento (verde) o decremento (rojo).
 * 
 * @param isIncrement true si fue incremento, false si fue decremento
 * @param number el valor a mostrar
 */
@Composable
private fun ChangeItem(
    isIncrement: Boolean,
    number: Int
) {
    Text(
        text = number.toString(),
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.background(
            color = if (isIncrement) Color.Green else Color.Red,
            shape = RoundedCornerShape(8.dp)
        )
    )
}

/**
 * Componente que muestra una estadística con su nombre y valor en formato de fila.
 * 
 * @param name el nombre de la estadística
 * @param value el valor de la estadística
 * @param modifier modificador para el componente
 */
@Composable
private fun ItemStats(
    name: String,
    value: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.Bold
        )
        Text(text = value.toString())
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    Lab6Theme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ScreenContent(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                history = listOf(
                    HistoryItem(
                        isIncrement = true,
                        number = 1
                    ),
                    HistoryItem(
                        isIncrement = true,
                        number = 2
                    ),
                    HistoryItem(
                        isIncrement = true,
                        number = 3
                    ),
                    HistoryItem(
                        isIncrement = false,
                        number = 2
                    ),
                    HistoryItem(
                        isIncrement = true,
                        number = 3
                    ),
                    HistoryItem(
                        isIncrement = true,
                        number = 4
                    ),
                ),
                onHistoryAdd = {},
                onResetClick = {}
            )
        }
    }
}