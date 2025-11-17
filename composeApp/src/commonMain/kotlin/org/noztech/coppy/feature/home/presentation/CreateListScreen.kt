package org.noztech.coppy.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import io.github.ismoy.imagepickerkmp.domain.config.ImagePickerConfig
import io.github.ismoy.imagepickerkmp.domain.models.GalleryPhotoResult
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import io.github.ismoy.imagepickerkmp.presentation.ui.components.GalleryPickerLauncher
import io.github.ismoy.imagepickerkmp.presentation.ui.components.ImagePickerLauncher
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import org.noztech.coppy.feature.home.presentation.composables.CreateListTopBar
import org.noztech.coppy.feature.home.presentation.viewmodels.CreateListViewModel

@Composable
fun CreateListScreen(
    navController: NavController,
    id: Long? = null
) {
    val coroutineScope = rememberCoroutineScope()
    val permissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionsController = remember { permissionsControllerFactory.createPermissionsController() }
    BindEffect(permissionsController)

    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel = koinViewModel<CreateListViewModel>()
    val groups by viewModel.groups.collectAsState()
    val selectedGroupId by viewModel.selectedGroupId.collectAsState()
    val saveState by viewModel.saveState.collectAsState()

    val existingItem = id?.let { viewModel.getItemById(it) }

    var name by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var imagePath by remember { mutableStateOf<String?>(null) }

    var showCamera by remember { mutableStateOf(false) }
    var capturedPhoto by remember { mutableStateOf<PhotoResult?>(null) }
    var showGallery by remember { mutableStateOf(false) }
    var selectedImages by remember { mutableStateOf<List<GalleryPhotoResult>>(emptyList()) }

    Button(onClick = { showCamera = true }) {
        Text("Take Photo")
    }

    LaunchedEffect(saveState) {
        when (val state = saveState) {
            is SaveState.Success -> {
                val result = snackbarHostState.showSnackbar(
                    message = "New list created.",
                    withDismissAction = true
                )

                if (result == SnackbarResult.Dismissed) {
                    navController.popBackStack()
                }
            }
            is SaveState.Error -> {
                snackbarHostState.showSnackbar(state.error)
            }
            else -> Unit
        }
    }

    LaunchedEffect(existingItem) {
        existingItem?.let { item ->
            name = item.title
            value = item.value_.orEmpty()
            viewModel.selectGroup(item.groupId)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Main content
        Scaffold(
            topBar = {
                CreateListTopBar(
                    navController,
                    onSaveClick = { viewModel.saveItem(name, value, existingItem?.id) },
                    title = if (existingItem != null) "Edit Item" else "Create New"
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Input for list name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Value") },
                    singleLine = false,
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )

                // Scrollable row for group selection
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(groups) { group ->
                        FilterChip(
                            selected = selectedGroupId == group.id,
                            onClick = { viewModel.selectGroup(group.id) },
                            label = { Text(group.name) },
                            modifier = Modifier.height(36.dp)
                        )
                    }
                }

//                Spacer(Modifier.height(4.dp))
//
//                LazyVerticalGrid(
//                    columns = GridCells.Fixed(2),
//                    modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp),
//                ) {
//                    items(selectedImages) { image ->
//                        AsyncImage(
//                            model = image.uri,
//                            contentDescription = "Selected image",
//                            contentScale = ContentScale.Crop,
//                            modifier = Modifier
//                                .aspectRatio(2f)
//                                .clip(RoundedCornerShape(8.dp))
//                                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
//                        )
//                    }
//                }

//                Spacer(Modifier.height(4.dp))
//
//                Button(
//                    onClick = {
//                        coroutineScope.launch {
//                            try {
//                                permissionsController.providePermission(Permission.CAMERA)
//                                showGallery = true
//                            } catch (deniedAlways: DeniedAlwaysException) {
//                                snackbarHostState.showSnackbar("Camera permission denied. Enable it in settings.")
//                            } catch (denied: DeniedException) {
//                                snackbarHostState.showSnackbar("Camera permission is required to add an image.")
//                            }
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    shape = RoundedCornerShape(12.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//                        contentColor = MaterialTheme.colorScheme.primary
//                    )
//                ) {
//                    Icon(imageVector = Lucide.ImagePlus, contentDescription = "Add Image", modifier = Modifier.size(24.dp))
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(text = "Add Image", color = MaterialTheme.colorScheme.primary)
//                }
            }
        }

        if (showCamera) {
            ImagePickerLauncher(
                config = ImagePickerConfig(
                    onPhotoCaptured = { result ->
                        capturedPhoto = result
                        showCamera = false
                    },
                    onError = { showCamera = false },
                    onDismiss = { showCamera = false }
                )
            )
        }

        if (showGallery) {
            GalleryPickerLauncher(
                onPhotosSelected = { photos ->
                    selectedImages = photos
                    showGallery = false
                },
                onError = { showGallery = false },
                onDismiss = { showGallery = false },
                allowMultiple = true
            )
        }

    }
}