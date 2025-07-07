package com.example.stayfit

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stayfit.ui.WorkoutViewModel
import com.example.stayfit.ui.theme.StayFitTheme
import androidx.compose.runtime.*
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import com.example.stayfit.data.WorkoutData
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.aspectRatio
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Canvas
import androidx.compose.ui.draw.shadow
import com.example.stayfit.data.Exercise
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import android.content.Intent
import androidx.compose.runtime.livedata.observeAsState
import com.example.stayfit.data.WorkoutProgress
import androidx.activity.compose.BackHandler
import androidx.compose.material3.AlertDialog
import androidx.activity.compose.LocalActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StayFitTheme {
                WorkoutApp()
            }
        }
    }
}

@Composable
fun WorkoutApp(
    modifier: Modifier = Modifier,
    viewModel: WorkoutViewModel = viewModel()
) {
    var showSplash by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Workout, 1 = BMI
    var currentCategory by remember { mutableStateOf("Classic") }
    var showInstructions by remember { mutableStateOf(false) }
    var showAbout by remember { mutableStateOf(false) }
    var showProgress by remember { mutableStateOf(false) }
    val activity = LocalActivity.current

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000)
        showSplash = false
    }

    // Custom back handling
    BackHandler(enabled = !showSplash) {
        when {
            showInstructions -> showInstructions = false
            showAbout -> showAbout = false
            showProgress -> showProgress = false
            selectedTab != 0 -> selectedTab = 0
            else -> activity?.finish()
        }
    }

    StayFitTheme {
        if (showSplash) {
            SplashScreen()
        } else {
            val workoutInProgress by viewModel.workoutInProgress.collectAsState()
            Scaffold(
                modifier = modifier.fillMaxSize(),
                containerColor = Color.White
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    if (workoutInProgress) {
                        WorkoutNavbar(
                            categoryName = currentCategory,
                            onBack = { viewModel.endWorkout() }
                        )
                    }
                    if (!workoutInProgress && !showInstructions && !showAbout && !showProgress) {
                        Navbar(
                            selectedTab = selectedTab,
                            onTabSelected = { selectedTab = it },
                            onBack = {},
                            showMoreMenu = selectedTab == 0,
                            onInstructionsClick = { showInstructions = true },
                            onAboutClick = { showAbout = true },
                            onProgressClick = { showProgress = true }
                        )
                    }
                    when {
                        workoutInProgress -> {
                            WorkoutScreen(modifier = Modifier.weight(1f), viewModel = viewModel)
                        }
                        showInstructions -> {
                            InstructionsScreen(
                                modifier = Modifier.weight(1f),
                                selectedCategory = currentCategory,
                                onCategoryChange = { category -> currentCategory = category },
                                onBack = { showInstructions = false },
                                onStart = {
                                    showInstructions = false
                                    viewModel.setCategory(currentCategory)
                                    viewModel.startWorkout()
                                }
                            )
                        }
                        showProgress -> {
                            ProgressScreen(onBack = { showProgress = false }, viewModel = viewModel)
                        }
                        showAbout -> {
                            AboutScreen(onBack = { showAbout = false })
                        }
                        selectedTab == 0 -> {
                            WorkoutCategoryScreen(
                                modifier = Modifier.weight(1f),
                                onInstructionsSelected = { category ->
                                    currentCategory = category
                                    showInstructions = true
                                },
                                onStartSelected = { category ->
                                    currentCategory = category
                                    viewModel.setCategory(category)
                                    viewModel.startWorkout()
                                }
                            )
                        }
                        selectedTab == 1 -> {
                            BMICalculatorScreen(modifier = Modifier.background(Color.White).weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1200)
    )
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.7f,
        animationSpec = tween(durationMillis = 1200)
    )
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF57C00)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "StayFit",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp,
            modifier = Modifier
                .alpha(alpha)
                .scale(scale)
        )
    }
}

@Composable
fun StartScreen(modifier: Modifier = Modifier, onStartClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF57C00)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "StayFit",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                elevation = null
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Start 10-Minute Workout",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutScreen(modifier: Modifier = Modifier, viewModel: WorkoutViewModel) {
    val currentExercise by viewModel.currentExercise.collectAsState()
    val nextExercise by viewModel.nextExercise.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }

    // Handle device/app back button
    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Are you sure to exit workout?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    viewModel.endWorkout()
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            currentExercise?.let { exercise ->
                val nonRestList = viewModel.currentNonRestExercises
                val isRest = exercise.name.equals("Rest", ignoreCase = true)
                val currentIndex = if (!isRest) nonRestList.indexOfFirst { it.name == exercise.name && it.durationInSeconds == exercise.durationInSeconds } else -1
                val total = nonRestList.size
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (!isRest && currentIndex >= 0) "${currentIndex + 1}/$total. ${exercise.name}" else exercise.name,
                        style = if (isRest) {
                            MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp)
                        } else {
                            MaterialTheme.typography.headlineMedium
                        },
                        fontWeight = FontWeight.Bold
                    )
                    if (exercise.imageResourceId != 0) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = exercise.imageResourceId),
                            contentDescription = exercise.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.5f)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Fit
                        )
                    }
                    if (exercise.name.equals("Rest", ignoreCase = true) && nextExercise != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Get ready for: ${nextExercise!!.name}",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        val totalDuration = currentExercise?.durationInSeconds?.takeIf { it > 0 } ?: 1
        val progress = timeLeft.toFloat() / totalDuration.toFloat()

        Box(
            modifier = Modifier
                .size(150.dp)
                .clickable { viewModel.togglePause() },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 12.dp.toPx()
                val center = this.center
                val radius = (size.minDimension - strokeWidth) / 2
                val startAngle = -90f

                drawArc(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    startAngle = startAngle,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                val sweepAngle = 360 * progress

                drawArc(
                    color = Color(0xFFF57C00),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                val angleInRad = Math.toRadians((startAngle + sweepAngle).toDouble())
                val pointCenter = Offset(
                    x = center.x + radius * cos(angleInRad).toFloat(),
                    y = center.y + radius * sin(angleInRad).toFloat()
                )
                drawCircle(
                    color = Color(0xFFF57C00),
                    radius = strokeWidth / 2,
                    center = pointCenter
                )
            }
            Text(
                text = if (isPaused) "Resume" else "$timeLeft\"",
                style = if (isPaused) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp
                ),
                color = Color(0xFFF57C00)
            )
        }
        
        if(!isPaused) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap to Pause",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
        }
        
        if (nextExercise != null) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.skipToNextExercise() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF57C00),
                    contentColor = Color.White
                )
            ) {
                Text("Next")
            }
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun WorkoutCategoryScreen(
    modifier: Modifier = Modifier,
    onInstructionsSelected: (String) -> Unit,
    onStartSelected: (String) -> Unit
) {
    val categories = WorkoutData.workoutsByCategory.keys.toList()
    
    // Debug: Print number of categories
    println("DEBUG: Number of categories: ${categories.size}")
    println("DEBUG: Categories: $categories")
    
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color.White)
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Select Workout Category",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFF57C00),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        items(categories) { category ->
            CategoryCard(
                category = category,
                onInstructions = { onInstructionsSelected(category) },
                onStart = { onStartSelected(category) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "End of list - You should be able to scroll up to see more categories",
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CategoryCard(category: String, onInstructions: () -> Unit, onStart: () -> Unit) {
    val imageRes = when (category) {
        "Classic" -> R.drawable.classic
        "Abs Workout" -> R.drawable.abs_workout
        "Butt Workout" -> R.drawable.butt_workout
        "Leg Workout" -> R.drawable.leg_workout
        "Arm Workout" -> R.drawable.arm_workout
        "Sleepy Time Stretch" -> R.drawable.crunches
        else -> R.drawable.classic
    }
    val description = when (category) {
        "Classic" -> "Scientifically proven to assist weight loss and improve cardiovascular function."
        "Abs Workout" -> "Target your core muscles and build a strong foundation."
        "Butt Workout" -> "Shape and strengthen your hip with focused exercises."
        "Leg Workout" -> "Boost lower body strength and endurance."
        "Arm Workout" -> "Tone and build your arm muscles effectively."
        "Sleepy Time Stretch" -> "Relax and stretch your body for better sleep."
        else -> "Stay fit and healthy with our workouts."
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .shadow(4.dp, RoundedCornerShape(16.dp))
    ) {
        Column {
            Box(modifier = Modifier.height(120.dp).fillMaxWidth()) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = category,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )
                Text(
                    text = category,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }
            Text(
                text = description,
                color = Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                TextButton(onClick = onInstructions) {
                Text(
                    text = "INSTRUCTIONS",
                    color = Color(0xFFF57C00),
                    fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                TextButton(onClick = onStart) {
                    Text(
                        text = "START",
                        color = Color(0xFFF57C00),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun Navbar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onBack: () -> Unit,
    showMoreMenu: Boolean = false,
    onInstructionsClick: (() -> Unit)? = null,
    onAboutClick: (() -> Unit)? = null,
    onProgressClick: (() -> Unit)? = null
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalActivity.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF57C00))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "StayFit",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1f)
            )
            if (showMoreMenu) {
                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "More",
                            tint = Color.White
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        Box(modifier = Modifier.width(220.dp)) {
                            Column {
                                DropdownMenuItem(
                                    text = { Text("Progress") },
                                    onClick = {
                                        menuExpanded = false
                                        onProgressClick?.invoke()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Instructions") },
                                    onClick = {
                                        menuExpanded = false
                                        onInstructionsClick?.invoke()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Share") },
                                    onClick = {
                                        menuExpanded = false
                                        val sendIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, "Check out the StayFit app! Get fit with easy home workouts. [Your Play Store Link Here]")
                                            type = "text/plain"
                                        }
                                        val shareIntent = Intent.createChooser(sendIntent, null)
                                        context?.startActivity(shareIntent)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("About") },
                                    onClick = {
                                        menuExpanded = false
                                        onAboutClick?.invoke()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        TabRow(selectedTab = selectedTab, onTabSelected = onTabSelected)
    }
}

@Composable
fun TabRow(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val tabTitles = listOf("Workout", "BMI Calculator")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF57C00)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabTitles.forEachIndexed { index, title ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(index) }
            ) {
                Text(
                    text = title,
                    color = if (selectedTab == index) Color.White else Color.White.copy(alpha = 0.6f),
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .height(3.dp)
                        .fillMaxWidth(0.6f)
                        .background(if (selectedTab == index) Color.White else Color.Transparent)
                )
            }
        }
    }
}

@Composable
fun BMICalculatorScreen(modifier: Modifier = Modifier) {
    var heightCm by remember { mutableFloatStateOf(0f) }
    var heightFt by remember { mutableIntStateOf(0) }
    var heightIn by remember { mutableIntStateOf(0) }
    var weightKg by remember { mutableFloatStateOf(0f) }
    var useCm by remember { mutableStateOf(true) }
    var bmi by remember { mutableStateOf<Float?>(null) }
    var resultText by remember { mutableStateOf("") }

    fun calculateBMI() {
        val heightM = if (useCm) {
            if (heightCm > 0) heightCm / 100f else 0f
        } else {
            if (heightFt > 0 || heightIn > 0) ((heightFt * 12) + heightIn) * 0.0254f else 0f
        }
        if (heightM > 0 && weightKg > 0) {
            val bmiValue = weightKg / (heightM * heightM)
            bmi = bmiValue
            resultText = when {
                bmiValue < 18.5 -> "Underweight"
                bmiValue < 25 -> "Normal weight"
                bmiValue < 30 -> "Overweight"
                else -> "Obese"
            }
        } else {
            bmi = null
            resultText = ""
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .background(Color.White)
        ) {
            Text(
                text = "BMI Calculator",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFFF57C00),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { useCm = true }) {
                    Text("cm", color = if (useCm) Color(0xFFF57C00) else Color.LightGray)
                }
                TextButton(onClick = { useCm = false }) {
                    Text("ft/in", color = if (!useCm) Color(0xFFF57C00) else Color.LightGray)
                }
            }
            if (useCm) {
                OutlinedTextField(
                    value = if (heightCm == 0f) "" else heightCm.toString(),
                    onValueChange = { heightCm = it.toFloatOrNull() ?: 0f },
                    label = { Text("Height (cm)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            } else {
                Row(modifier = Modifier.fillMaxWidth(0.8f)) {
                    OutlinedTextField(
                        value = if (heightFt == 0) "" else heightFt.toString(),
                        onValueChange = { heightFt = it.toIntOrNull() ?: 0 },
                        label = { Text("ft") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = if (heightIn == 0) "" else heightIn.toString(),
                        onValueChange = { heightIn = it.toIntOrNull() ?: 0 },
                        label = { Text("in") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = if (weightKg == 0f) "" else weightKg.toString(),
                onValueChange = { weightKg = it.toFloatOrNull() ?: 0f },
                label = { Text("Weight (kg)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { calculateBMI() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF57C00),
                    contentColor = Color.White
                )
            ) {
                Text("Calculate BMI")
            }
            Spacer(modifier = Modifier.height(24.dp))
            bmi?.let {
                Text(
                    text = "Your BMI: %.2f".format(it),
                    color = Color(0xFFF57C00),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Text(
                    text = resultText,
                    color = Color(0xFFF57C00),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun WorkoutNavbar(categoryName: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFFF57C00)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_media_previous),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = categoryName,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    StayFitTheme {
        StartScreen(onStartClick = {})
    }
}

@Composable
fun InstructionsScreen(
    modifier: Modifier = Modifier,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    onBack: () -> Unit,
    onStart: () -> Unit
) {
    val categories = WorkoutData.workoutsByCategory.keys.toList()
    val exercises = (WorkoutData.workoutsByCategory[selectedCategory] ?: emptyList())
        .filter { !it.name.equals("Rest", ignoreCase = true) }

    Column(modifier = modifier) {
        // Top App Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF57C00))
                .padding(8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_media_previous),
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "Instructions",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Tab Row for categories
        androidx.compose.material3.ScrollableTabRow(
            selectedTabIndex = categories.indexOf(selectedCategory),
            containerColor = Color(0xFFF57C00),
            contentColor = Color.White,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[categories.indexOf(selectedCategory)]),
                    height = 3.dp,
                    color = Color.White
                )
            }
        ) {
            categories.forEachIndexed { index, category ->
                androidx.compose.material3.Tab(
                    selected = selectedCategory == category,
                    onClick = { onCategoryChange(category) },
                    text = { Text(category.uppercase()) }
                )
            }
        }

        // Exercises List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(Color.White)
                .padding(8.dp)
        ) {
            itemsIndexed(exercises) { idx, exercise ->
                ExerciseInstructionItem(
                    index = idx,
                    total = exercises.size,
                    exercise = exercise
                )
            }
        }
        Button(
            onClick = onStart,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF57C00),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Start Workout")
        }
    }
}

@Composable
fun ExerciseInstructionItem(index: Int, total: Int, exercise: Exercise) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "${index + 1}/$total. ${exercise.name.uppercase()}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (exercise.imageResourceId != 0) {
            Image(
                painter = painterResource(id = exercise.imageResourceId),
                contentDescription = exercise.name,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (exercise.description.isNotBlank()) {
            Text(
                text = exercise.description,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun AboutScreen(onBack: () -> Unit) {
    val scrollState = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Orange Navbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFFF57C00)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_media_previous),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "About",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 56.dp, start = 24.dp, end = 24.dp, bottom = 32.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "StayFit",
                    color = Color(0xFFF57C00),
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(bottom = 28.dp, top = 8.dp)
                )
                Text(
                    text = "ABOUT THIS APP",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 10.dp, start = 4.dp)
                )
                Text(
                    text = "StayFit is your all-in-one fitness companion, designed to make healthy living accessible and enjoyable for everyone. Whether you're a beginner or looking to take your workouts to the next level, StayFit offers a variety of home-based exercises and routines, each crafted by fitness experts to help you achieve real results without the need for equipment or a gym membership.\n\nWith StayFit, you can choose from multiple workout categories, track your progress, and stay motivated with an intuitive, user-friendly interface. Build healthy habits, improve your well-being, and join a growing community of users transforming their lives—one workout at a time.",
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 28.dp, start = 4.dp, end = 4.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "BENEFITS:",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 10.dp, start = 4.dp)
                )
                Text(
                    text = "• No equipment needed\n• Suitable for all fitness levels\n• Track your progress\n• Scientifically designed workouts\n• User-friendly and motivating interface\n• Workout anytime, anywhere",
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 28.dp, start = 4.dp, end = 4.dp)
                )
                Text(
                    text = "INDIVIDUAL EXERCISE TIME:",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 6.dp, top = 8.dp, start = 4.dp)
                )
                Text(
                    text = "50 seconds is set for each exercise in StayFit, allowing you to perform each movement with proper form and intensity. This duration is ideal for building strength and endurance in every session.",
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 22.dp, start = 4.dp, end = 4.dp)
                )
                Text(
                    text = "REST TIME BETWEEN EXERCISES:",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 6.dp, top = 8.dp, start = 4.dp)
                )
                Text(
                    text = "A 10-second rest is provided after each exercise. This short break helps you recover just enough to maintain good performance throughout the workout, while keeping your heart rate up for better results.",
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 22.dp, start = 4.dp, end = 4.dp)
                )
                Text(
                    text = "TOTAL EXERCISE TIME:",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 6.dp, top = 8.dp, start = 4.dp)
                )
                Text(
                    text = "Each StayFit workout is designed to be completed in about 10 minutes. For enhanced benefits, you can repeat the circuit 2–3 times, aiming for at least 20–30 minutes of total exercise.",
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp, end = 4.dp)
                )
                Text(
                    text = "Thanks for using StayFit!",
                    color = Color(0xFFF57C00),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp)
                )
            }
        }
    }
}

@Composable
fun ProgressScreen(onBack: () -> Unit, viewModel: WorkoutViewModel = viewModel()) {
    val progressList: List<WorkoutProgress> by viewModel.progressLast7Days.observeAsState(emptyList())

    // Calculate today's totals
    val today = java.util.Calendar.getInstance().apply {
        set(java.util.Calendar.HOUR_OF_DAY, 0)
        set(java.util.Calendar.MINUTE, 0)
        set(java.util.Calendar.SECOND, 0)
        set(java.util.Calendar.MILLISECOND, 0)
    }.timeInMillis
    val todayProgress = progressList.filter {
        val cal = java.util.Calendar.getInstance().apply { timeInMillis = it.date }
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
        cal.set(java.util.Calendar.MINUTE, 0)
        cal.set(java.util.Calendar.SECOND, 0)
        cal.set(java.util.Calendar.MILLISECOND, 0)
        cal.timeInMillis == today
    }
    val totalTime = todayProgress.sumOf { it.durationMinutes }
    val totalExercises = todayProgress.sumOf { it.exercisesCompleted }

    LaunchedEffect(Unit) {
        viewModel.fetchProgressLast7Days()
        viewModel.fetchProgressLast30Days()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        // Orange Navbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color(0xFFF57C00)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(32.dp)) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_media_previous),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Progress",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Total Time: $totalTime min",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Total Exercises: $totalExercises",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Text(
                text = "Last 7 days",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            if (progressList.isEmpty()) {
                Text(
                    text = "No progress yet. Complete a workout to see your stats!",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                // Group progress by normalized day (midnight, device timezone)
                val progressByDay = progressList.groupBy {
                    val cal = java.util.Calendar.getInstance().apply { timeInMillis = it.date }
                    cal.set(java.util.Calendar.HOUR_OF_DAY, 0)
                    cal.set(java.util.Calendar.MINUTE, 0)
                    cal.set(java.util.Calendar.SECOND, 0)
                    cal.set(java.util.Calendar.MILLISECOND, 0)
                    cal.timeInMillis
                }
                progressByDay.entries.sortedByDescending { it.key }.forEach { (dayMillis, dayProgressList) ->
                    val dayLabel = android.text.format.DateFormat.format("MMM dd", dayMillis).toString()
                    val totalDayMinutes = dayProgressList.sumOf { it.durationMinutes }
                    val totalDayExercises = dayProgressList.sumOf { it.exercisesCompleted }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dayLabel,
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1.5f)
                        )
                        if (totalDayMinutes > 0) {
                            Box(
                                modifier = Modifier
                                    .weight(3f)
                                    .height(8.dp)
                                    .background(Color(0xFF2196F3), shape = RoundedCornerShape(4.dp))
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(3f))
                        }
                        Text(
                            text = String.format("%02d min (%d ex)", totalDayMinutes, totalDayExercises),
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}