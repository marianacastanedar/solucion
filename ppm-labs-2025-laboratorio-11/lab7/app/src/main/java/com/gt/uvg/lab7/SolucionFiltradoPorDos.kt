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
fun ScreenContentFiltradoPorDos(
    notifications: List<Notification>,
    filters: List<NotificationType>,
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
                selected = filters.any { it == NotificationType.GENERAL },
                onClick = { onFilterChange(NotificationType.GENERAL) },
                label = {
                    Text("Informativas")
                }
            )

            FilterChip(
                selected = filters.any { it == NotificationType.NEW_MEETING },
                onClick = { onFilterChange(NotificationType.NEW_MEETING) },
                label = {
                    Text("Capacitaciones")
                }
            )
        }
        if (filters.size == 2) {
            Text("Sin notificaciones")
        } else {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyColumn {
                    items(notifications.filter { notification ->
                        filters.isEmpty() ||
                        filters.contains(notification.type)
                    }) {
                        NotificationItem(
                            notification = it,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ScreenPreviewFiltradoPorDos() {
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
            var filters: List<NotificationType> by remember {
                mutableStateOf(listOf())
            }
            ScreenContentFiltradoPorDos(
                notifications = generateFakeNotifications(),
                filters = filters,
                onFilterChange = { filterClicked ->
                    if (filters.contains(filterClicked)) {
                        filters = filters - filterClicked
                    } else {
                        filters = filters + filterClicked
                    }
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}