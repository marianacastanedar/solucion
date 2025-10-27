package com.gt.uvg.lab7

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gt.uvg.lab7.ui.theme.Lab7Theme

@Composable
fun ScreenContentFiltradoPorUno(
    notifications: List<Notification>,
    filter: NotificationType?,
    onFilterChange: (NotificationType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Text("Tipos de notificaciones")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = filter == NotificationType.GENERAL,
                onClick = { onFilterChange(NotificationType.GENERAL) },
                label = {
                    Text("Informativas")
                }
            )

            FilterChip(
                selected = filter == NotificationType.NEW_MEETING,
                onClick = { onFilterChange(NotificationType.NEW_MEETING) },
                label = {
                    Text("Capacitaciones")
                }
            )
        }
        OutlinedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            LazyColumn {
                items(notifications.filter { filter == null || it.type == filter }) {
                    NotificationItem(
                        notification = it,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ScreenPreviewFiltradoPorUno() {
    Lab7Theme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text("Notificaciones")
                    }
                )
            }
        ) { innerPadding ->
            val notifications by remember {
                mutableStateOf(generateFakeNotifications())
            }
            var filter: NotificationType? by remember {
                mutableStateOf(null)
            }
            ScreenContentFiltradoPorUno(
                notifications = generateFakeNotifications(),
                filter = filter,
                onFilterChange = { filterClicked ->
                    if (filterClicked == filter) {
                        filter = null
                    } else {
                        filter = filterClicked
                    }
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}