package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BodyWeightLog
import com.example.data.WorkoutLog
import com.example.data.SyncPayload
import com.example.data.CloudSyncService
import com.example.viewmodel.WorkoutViewModel
import com.example.viewmodel.GoogleUser
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

@Composable
fun ProgressScreen(
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val workoutLogs by viewModel.workoutLogs.collectAsState()
    val weightLogs by viewModel.bodyWeightLogs.collectAsState()

    val totalWorkouts = workoutLogs.size
    val totalLiftedWeight = remember(workoutLogs) {
        workoutLogs.sumOf { it.weight * it.sets * it.reps }
    }

    val dateFormat = remember { SimpleDateFormat("MMM dd", Locale.getDefault()) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Google cloud sync dashboard
        item {
            GoogleSyncPanel(viewModel = viewModel)
        }

        // Stats Cards Summary Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Liftoff Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("stats_workouts_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = "Lift Count",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Total Sets",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "$totalWorkouts",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Volume Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("stats_volume_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AllInclusive,
                                contentDescription = "Total Tonnage",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Est. Tonnage",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        val tonnageK = totalLiftedWeight / 1000f
                        Text(
                            text = String.format(Locale.getDefault(), "%.1fK", tonnageK),
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // BODY WEIGHT CHART
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bodyweight_chart_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Size & Mass Tracking",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.Default.ShowChart,
                            contentDescription = "Trends",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "History of user body mass records",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (weightLogs.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Empty scales! Log your weight on the Dashboard screen to stream points.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        // Display Current Weight at the top of the chart section
                        val latestLog = weightLogs.first()
                        val latestKg = latestLog.weight
                        val latestLbs = latestKg * 2.20462262185
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Current Recorded Weight",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = String.format(Locale.getDefault(), "%.1f kg", latestKg),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = String.format(Locale.getDefault(), "(%.1f lbs)", latestLbs),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                        
                        // Drawing Weight Graph
                        WeightHistoryChart(weightLogs = weightLogs.take(8).reversed())
                        
                        // List the recent weight entries underneath
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = "Recent Mass Records (In Lbs & Kg)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        weightLogs.take(5).forEach { log ->
                            val logKg = log.weight
                            val logLbs = logKg * 2.20462262185
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = dateFormat.format(Date(log.dateMillis)),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = String.format(Locale.getDefault(), "%.1f kg", logKg),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = String.format(Locale.getDefault(), "(%.1f lbs)", logLbs),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    IconButton(
                                        onClick = { viewModel.deleteBodyWeightLog(log.id) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete Weight Entry",
                                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // MUSCLE DISTRIBUTION TRAFFIC
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("muscle_volume_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Workout Frequency by Muscle",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    val logsByCategory = remember(workoutLogs) {
                        workoutLogs.groupBy { it.category }
                    }

                    val categories = listOf("Chest", "Back", "Legs", "Shoulders", "Biceps", "Triceps", "Abs")
                    val maxCount = remember(logsByCategory) {
                        val maxVal = logsByCategory.values.map { it.size }.maxOrNull() ?: 1
                        if (maxVal == 0) 1 else maxVal
                    }

                    categories.forEach { cat ->
                        val count = logsByCategory[cat]?.size ?: 0
                        val fraction = count.toFloat() / maxCount

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                modifier = Modifier.width(100.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .fillMaxWidth(fraction)
                                        .clip(RoundedCornerShape(5.dp))
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "$count",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.width(24.dp),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }

        // CREW CREDITS INDEX (Trainer Tanni Saini and Developer Kapil Verma)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("credits_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Old Skool Fitness Team",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Lead Trainer Credits Panel
                    val uriHandler = LocalUriHandler.current
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                uriHandler.openUri("https://www.instagram.com/tanni.saini?igsh=MWM4Y2ttODkyaXRrMg==")
                            }
                            .padding(vertical = 4.dp, horizontal = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = com.example.R.drawable.ic_trainer_tanni),
                            contentDescription = "Trainer Tanni Saini Photo",
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp)),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Tanni Saini",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.OpenInNew,
                                    contentDescription = "Instagram link",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "Head Trainer • @tanni.saini",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable {
                                    try { uriHandler.openUri("tel:+917011323492") } catch (e: java.lang.Exception) {}
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Call icon",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "+91 70113 23492 (Click to Call)",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))

                    // Developer Credits Panel
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                uriHandler.openUri("https://www.instagram.com/kapil_rj_02_alaa?igsh=MTE0aHZiajVkaG0wNw==")
                            }
                            .padding(vertical = 4.dp, horizontal = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = com.example.R.drawable.ic_developer_kapil),
                            contentDescription = "Developer Kapil Verma Photo",
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp)),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Kapil Verma",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.OpenInNew,
                                    contentDescription = "Instagram link",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "Lead Application Developer • @kapil_rj_02_alaa",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }

        // COMPREHENSIVE LIFT FEED HISTORY
        item {
            Text(
                text = "General Training Log History",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (workoutLogs.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No workouts logged yet. Grind time!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        } else {
            items(workoutLogs) { log ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("general_workout_log_${log.id}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = log.exerciseName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            val logWeightInLbs = log.weight * 2.20462262185
                            Text(
                                text = String.format(Locale.getDefault(), "%d Sets x %d Reps @ %.1f kg (%.1f lbs)", log.sets, log.reps, log.weight, logWeightInLbs),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            if (log.notes.isNotEmpty()) {
                                Text(
                                    text = log.notes,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            Text(
                                text = "Registered: ${dateFormat.format(Date(log.dateMillis))}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        IconButton(
                            onClick = { viewModel.deleteWorkoutLog(log.id) },
                            modifier = Modifier.testTag("general_log_delete_btn_${log.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Log",
                                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeightHistoryChart(
    weightLogs: List<BodyWeightLog>,
    modifier: Modifier = Modifier
) {
    val chartLineColor = MaterialTheme.colorScheme.primary
    val gridLineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    val canvasBgColor = MaterialTheme.colorScheme.surface
    val canvasBorderColor = MaterialTheme.colorScheme.outline

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        val dateFormat = remember { SimpleDateFormat("MM/dd", Locale.getDefault()) }

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(canvasBgColor, RoundedCornerShape(12.dp))
                .border(1.dp, canvasBorderColor, RoundedCornerShape(12.dp))
        ) {
            val width = size.width
            val height = size.height

            // Calculate margins
            val leftMargin = 50f
            val rightMargin = 30f
            val topMargin = 30f
            val bottomMargin = 40f

            val chartWidth = width - leftMargin - rightMargin
            val chartHeight = height - topMargin - bottomMargin

            // Simple scales
            val weights = weightLogs.map { it.weight }
            val maxWeight = (weights.maxOrNull() ?: 100.0) + 2.0
            val minWeight = (weights.minOrNull() ?: 50.0) - 2.0
            val weightRange = maxWeight - minWeight

            // Draw horizontal grid lines
            val numGridLines = 3
            for (i in 0..numGridLines) {
                val gridY = topMargin + (chartHeight / numGridLines) * i
                drawLine(
                    color = gridLineColor,
                    start = Offset(leftMargin, gridY),
                    end = Offset(width - rightMargin, gridY),
                    strokeWidth = 2f
                )
            }

            // Draw points
            val numPoints = weightLogs.size
            if (numPoints > 1) {
                val dx = chartWidth / (numPoints - 1)
                val pointsList = weightLogs.mapIndexed { idx, log ->
                    val x = leftMargin + idx * dx
                    val normalizedWeight = (log.weight - minWeight) / weightRange
                    val y = height - bottomMargin - (normalizedWeight * chartHeight).toFloat()
                    Offset(x, y)
                }

                // Draw line path connecting points
                for (i in 0 until pointsList.size - 1) {
                    drawLine(
                        color = chartLineColor,
                        start = pointsList[i],
                        end = pointsList[i + 1],
                        strokeWidth = 6f,
                        cap = StrokeCap.Round
                    )
                }

                // Draw dots and subtle glow halos
                pointsList.forEach { pt ->
                    drawCircle(
                        color = chartLineColor.copy(alpha = 0.25f),
                        radius = 12f,
                        center = pt
                    )
                    drawCircle(
                        color = chartLineColor,
                        radius = 6f,
                        center = pt
                    )
                }
            } else if (numPoints == 1) {
                // If only 1 point, draw center line and single dot
                val pt = Offset(leftMargin + chartWidth / 2f, topMargin + chartHeight / 2f)
                drawCircle(
                    color = chartLineColor,
                    radius = 8f,
                    center = pt
                )
            }
        }

        // Draw date names at bottom in row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            weightLogs.forEach { log ->
                Text(
                    text = dateFormat.format(Date(log.dateMillis)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

// ================= GOOGLE AUTHENTICATION & SECURE CLOUD SYNCING COMPOSABLES =================

fun triggerGoogleSignIn(
    context: android.content.Context,
    viewModel: WorkoutViewModel,
    onFallback: () -> Unit
) {
    val credentialManager = CredentialManager.create(context)
    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId("7011323492-fallback.apps.googleusercontent.com")
        .setAutoSelectEnabled(true)
        .build()

    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    (context as? androidx.activity.ComponentActivity)?.let { activity ->
        activity.lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential
                if (credential is androidx.credentials.CustomCredential && 
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    
                    val email = googleIdTokenCredential.id
                    val name = googleIdTokenCredential.displayName ?: email.substringBefore("@")
                    val photoUrl = googleIdTokenCredential.profilePictureUri?.toString()
                    
                    viewModel.signInUser(email, name, photoUrl)
                } else {
                    onFallback()
                }
            } catch (e: Exception) {
                onFallback()
            }
        }
    } ?: onFallback()
}

@Composable
fun GoogleSyncPanel(
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val syncInProgress by viewModel.syncInProgress.collectAsState()
    val lastSyncTime by viewModel.lastSyncTime.collectAsState()
    val syncErrorMessage by viewModel.syncErrorMessage.collectAsState()
    
    val context = androidx.compose.ui.platform.LocalContext.current
    var showAccountPicker by remember { mutableStateOf(false) }
    var customEmail by remember { mutableStateOf("") }
    var showCustomInput by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("google_sync_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CloudQueue,
                    contentDescription = "Cloud Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Google Account Sync",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            
            if (currentUser == null) {
                // Logged OUT UI
                Text(
                    text = "Sign in with Google to sync your exercise progress and body weight metrics to the secure cloud. Track and restore your data across any device.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Button(
                    onClick = {
                        triggerGoogleSignIn(context, viewModel) {
                            showAccountPicker = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFDADCE0)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("google_sign_in_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Box(Modifier.size(5.dp).clip(RoundedCornerShape(50)).background(Color(0xFFEA4335)))
                            Box(Modifier.size(5.dp).clip(RoundedCornerShape(50)).background(Color(0xFFFBBC05)))
                            Box(Modifier.size(5.dp).clip(RoundedCornerShape(50)).background(Color(0xFF34A853)))
                            Box(Modifier.size(5.dp).clip(RoundedCornerShape(50)).background(Color(0xFF4285F4)))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sign in with Google",
                            color = Color(0xFF3C4043),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            } else {
                // Logged IN UI
                val user = currentUser!!
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile picture initials
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (user.displayName.firstOrNull() ?: 'G').toString().uppercase(),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.displayName,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    TextButton(
                        onClick = { viewModel.signOutUser() },
                        modifier = Modifier.testTag("google_sign_out_button")
                    ) {
                        Text("Sign Out", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                    }
                }
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(8.dp))
                
                // Sync status details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = "Syncing",
                        tint = if (syncInProgress) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (syncInProgress) "Synchronizing with Google Cloud..." else {
                            if (lastSyncTime != null) {
                                val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
                                "Synced: ${sdf.format(Date(lastSyncTime!!))}"
                            } else {
                                "Cloud storage active (not backed up)"
                            }
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (syncErrorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = syncErrorMessage!!,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { viewModel.backupFitnessData() },
                        enabled = !syncInProgress,
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .testTag("google_backup_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = "Backup Icon",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Backup Now", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    
                    OutlinedButton(
                        onClick = { viewModel.restoreFitnessData() },
                        enabled = !syncInProgress,
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .testTag("google_restore_button"),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = "Restore Icon",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Restore Cloud", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
    
    // FALLBACK ACCOUNT SELECTION DIALOG
    if (showAccountPicker) {
        AlertDialog(
            onDismissRequest = { showAccountPicker = false },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(6.dp).clip(RoundedCornerShape(50)).background(Color(0xFFEA4335)))
                        Box(Modifier.size(6.dp).clip(RoundedCornerShape(50)).background(Color(0xFFFBBC05)))
                        Box(Modifier.size(6.dp).clip(RoundedCornerShape(50)).background(Color(0xFF34A853)))
                        Box(Modifier.size(6.dp).clip(RoundedCornerShape(50)).background(Color(0xFF4285F4)))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Choose an account",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "to continue to Old Skool Gym",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val accounts = listOf(
                        "averma92040@gmail.com" to "Abhay Verma",
                        "tanni.saini@gmail.com" to "Tanni Saini"
                    )
                    
                    accounts.forEach { (email, name) ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.signInUser(email, name, null)
                                    showAccountPicker = false
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = name.first().toString(),
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = name,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = email,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                    
                    if (showCustomInput) {
                        OutlinedTextField(
                            value = customEmail,
                            onValueChange = { customEmail = it },
                            label = { Text("Google Email Address") },
                            placeholder = { Text("e.g. user@gmail.com") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Button(
                            onClick = {
                                if (customEmail.contains("@") && customEmail.length > 5) {
                                    val customName = customEmail.substringBefore("@")
                                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                                    viewModel.signInUser(customEmail.trim(), customName, null)
                                    showAccountPicker = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Sign In using this email")
                        }
                    } else {
                        TextButton(
                            onClick = { showCustomInput = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Use another account")
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showAccountPicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
