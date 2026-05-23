package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatMessage
import com.example.data.GeminiDietService
import com.example.viewmodel.WorkoutViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DietScreen(
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val weightLogs by viewModel.bodyWeightLogs.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    
    // Auto pull the latest body weight
    val latestWeightKg = remember(weightLogs) {
        weightLogs.firstOrNull()?.weight ?: 75.0
    }

    var ageInput by remember { mutableStateOf("25") }
    var scaleIsKg by remember { mutableStateOf(true) }
    var selectedGoal by remember { mutableStateOf("Weight Gain") } // "Weight Gain" or "Weight Loss"
    var isGeneratingDiet by remember { mutableStateOf(false) }
    var userMessageText by remember { mutableStateOf("") }
    
    // Conversation History
    val chatMessages = remember { mutableStateListOf<ChatMessage>() }
    val lazyListState = rememberLazyListState()

    // Setup automatic scroll to bottom when new messages arrive
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            lazyListState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    val displayWeight = if (scaleIsKg) {
        String.format(Locale.getDefault(), "%.1f kg", latestWeightKg)
    } else {
        String.format(Locale.getDefault(), "%.1f lbs", latestWeightKg * 2.20462262)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Diet Assistant Header Card with quick physical diagnostics
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = "Diet icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Old-School Diet Coach",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "High-protein nutrition plans fueled by Gemini",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    // Diagnostic Inputs Row (Age, Goals, Weight)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Diagnostic 1: Latest Recorded Weight
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Body Weight",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface,
                                onClick = { scaleIsKg = !scaleIsKg },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Scale,
                                        contentDescription = "Scale",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = displayWeight,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        // Diagnostic 2: Age entering
                        Column(modifier = Modifier.weight(0.9f)) {
                            Text(
                                text = "Age Years",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            OutlinedTextField(
                                value = ageInput,
                                onValueChange = {
                                    if (it.all { char -> char.isDigit() } && it.length <= 2) {
                                        ageInput = it
                                    }
                                },
                                textStyle = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(8.dp),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = "Age Icon",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(14.dp)
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("diet_age_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                                ),
                                singleLine = true
                            )
                        }

                        // Diagnostic 3: Weight Gain vs Loss Segmented
                        Column(modifier = Modifier.weight(1.3f)) {
                            Text(
                                text = "Diet Goal",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                Button(
                                    onClick = { selectedGoal = "Weight Gain" },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedGoal == "Weight Gain") MaterialTheme.colorScheme.primary else Color.Transparent,
                                        contentColor = if (selectedGoal == "Weight Gain") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    ),
                                    shape = RoundedCornerShape(0.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(38.dp)
                                        .testTag("diet_goal_toggle_gain")
                                ) {
                                    Text("Gain 💪", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                }
                                Button(
                                    onClick = { selectedGoal = "Weight Loss" },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedGoal == "Weight Loss") MaterialTheme.colorScheme.primary else Color.Transparent,
                                        contentColor = if (selectedGoal == "Weight Loss") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                    ),
                                    shape = RoundedCornerShape(0.dp),
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(38.dp)
                                        .testTag("diet_goal_toggle_loss")
                                ) {
                                    Text("Loss 🔥", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                }
                            }
                        }
                    }
                }
            }

            // Message Scroll Window
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                if (chatMessages.isEmpty()) {
                    // Empty Conversation Prompt with solid initial recommendation triggers
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "Empty chat marker",
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Meal Logs are empty",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Ask the Old School Coach for a dynamic, personalized bodybuilder style diet target according to your weights & age!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Button(
                            onClick = {
                                val parsedAge = ageInput.toIntOrNull() ?: 25
                                isGeneratingDiet = true
                                chatMessages.add(ChatMessage("Generate old-school diet recommendation for my weight and age.", true))
                                coroutineScope.launch {
                                    val aiResult = GeminiDietService.getDietRecommendation(
                                        weightKg = latestWeightKg,
                                        age = parsedAge,
                                        goal = selectedGoal,
                                        chatHistory = chatMessages.toList()
                                    )
                                    chatMessages.add(ChatMessage(aiResult, false))
                                    isGeneratingDiet = false
                                }
                            },
                            enabled = !isGeneratingDiet,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("generate_diet_plan_button")
                        ) {
                            Icon(imageVector = Icons.Default.Restaurant, contentDescription = "Plate")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Generate Diet Blueprint ⚙️",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Black)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(chatMessages) { message ->
                            ChatBubbleItem(message = message)
                        }
                        if (isGeneratingDiet) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Old School Coach is typing meal targets...",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Input Send Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = userMessageText,
                    onValueChange = { userMessageText = it },
                    placeholder = { Text("Ask follow-up questions... eg. high-carb staples?") },
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("diet_chat_input_field"),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                IconButton(
                    onClick = {
                        if (userMessageText.isNotBlank() && !isGeneratingDiet) {
                            val msgToSend = userMessageText
                            userMessageText = ""
                            chatMessages.add(ChatMessage(msgToSend, true))
                            val parsedAge = ageInput.toIntOrNull() ?: 25
                            isGeneratingDiet = true
                            coroutineScope.launch {
                                val aiResult = GeminiDietService.getDietRecommendation(
                                    weightKg = latestWeightKg,
                                    age = parsedAge,
                                    goal = selectedGoal,
                                    chatHistory = chatMessages.toList()
                                )
                                chatMessages.add(ChatMessage(aiResult, false))
                                isGeneratingDiet = false
                            }
                        }
                    },
                    enabled = userMessageText.isNotBlank() && !isGeneratingDiet,
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            color = if (userMessageText.isNotBlank() && !isGeneratingDiet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(23.dp)
                        )
                        .testTag("diet_chat_send_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Message Icon",
                        tint = if (userMessageText.isNotBlank() && !isGeneratingDiet) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(message: ChatMessage) {
    val containerColor = if (message.isUser) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
    }

    val contentColor = if (message.isUser) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val shape = if (message.isUser) {
        RoundedCornerShape(topStart = 16.dp, topEnd = 4.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    } else {
        RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .clip(shape)
                .background(containerColor)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.text,
                color = contentColor,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp)
            )
        }
    }
}
