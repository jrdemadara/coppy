package org.noztech.coppy.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import org.koin.compose.viewmodel.koinViewModel
import org.noztech.coppy.feature.home.presentation.composables.CreateGroupBottomSheet
import org.noztech.coppy.feature.home.presentation.composables.GroupTopBar
import org.noztech.coppy.feature.home.presentation.viewmodels.GroupViewModel

@Composable
fun GroupScreen(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel = koinViewModel<GroupViewModel>()
    val groupsWithCount by viewModel.groupsWithCount.collectAsState()
    val saveState by viewModel.saveState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val totalCount by remember(groupsWithCount) {
        derivedStateOf { groupsWithCount.sumOf { it.second.toInt() } }
    }
    var showSheet by remember { mutableStateOf(false) }

    LaunchedEffect(saveState) {
        when (val state = saveState) {
            is SaveState.Success -> snackbarHostState.showSnackbar(state.message.toString())
            is SaveState.Error -> snackbarHostState.showSnackbar(state.error)
            else -> {}
        }
    }

    Scaffold(
        topBar = { GroupTopBar(navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                GroupRow(
                    name = "All",
                    count = totalCount,
                    onClick = { /* open all */ }
                )
            }

            items(groupsWithCount) { (group, count) ->
                GroupRow(
                    name = group.name.capitalize(Locale.current),
                    count = count.toInt(),
                    onClick = { /* open group */ }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { showSheet = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // optional height
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Lucide.Plus,
                        contentDescription = "Add Image",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "New Group",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        if (showSheet) {
            CreateGroupBottomSheet(
                showSheet = showSheet,
                onDismiss = { showSheet = false },
                onSave = { name ->
                    viewModel.createGroup(name)
                    showSheet = false
                }
            )
        }
    }
}

@Composable
private fun GroupRow(
    name: String,
    count: Int,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = count.toString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}