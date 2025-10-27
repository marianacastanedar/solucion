package uvg.plataformas.android.lab5

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import uvg.plataformas.android.lab5.ui.theme.Lab5Theme
import androidx.core.net.toUri


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab5Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Lab5(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Lab5(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = null
            )
            Text(
                text = "Actualizacion disponible"
            )
            TextButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, "market://details?id=dev.koalit.lifedots.app".toUri())
                    context.startActivity(intent)
                }
            ) {
                Text(
                    text = "Descargar"
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column{
                Text(
                    text = "Viernes",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "30 Sept",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            OutlinedButton(
                onClick = {}
            ) {
                Text("Terminar jornada")
            }
        }
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ookii",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, "geo:14.604000776704044,-90.48879739255989".toUri())
                            intent.setPackage("com.google.android.apps.maps")
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(
                            Icons.Default.Build,
                            contentDescription = null
                        )
                    }
                }
                Text(
                    text = "Plaza 10, 6a avenida zona 10"
                )
                Text(
                    text = "8am a 10pm"
                )
                Row {
                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Juan Carlos Durini Serrano",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Iniciar")
                    }
                    TextButton(
                        onClick = {
                            Toast.makeText(
                                context,
                                "Poke Bowls\nQQ",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Detalles")
                    }
                }
            }
        }
    }
}