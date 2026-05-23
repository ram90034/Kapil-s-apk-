package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.data.ExerciseType

@Composable
fun ExerciseAnimation(
    type: ExerciseType,
    modifier: Modifier = Modifier
) {
    // Shared infinite looping progress indicator (0.0 to 1.0)
    val infiniteTransition = rememberInfiniteTransition(label = "exercise_animation")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lift_progress"
    )

    // Gold/Yellow for active target/weights, Slate grey for body structure
    val primaryGold = Color(0xFFFFD54F) // Gold
    val secondarySteel = Color(0xFF78909C) // Steel Slate
    val ironBlack = Color(0xFF37474F) // Dark support
    val pulseAccent = Color(0xFFFFB74D) // Amber core/glow

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E2125))
            .border(1.dp, Color(0xFF2E3440).copy(alpha = 0.5f), RoundedCornerShape(16.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            val w = size.width
            val h = size.height
            val centerX = w / 2f
            val centerY = h / 2f

            when (type) {
                ExerciseType.BENCH_PRESS, ExerciseType.DECLINE_PRESS -> {
                    // Draw Bench
                    val benchY = centerY + 30f
                    drawLine(color = ironBlack, start = Offset(centerX - 120f, benchY), end = Offset(centerX + 120f, benchY), strokeWidth = 12f, cap = StrokeCap.Round)
                    drawLine(color = ironBlack, start = Offset(centerX - 90f, benchY), end = Offset(centerX - 90f, benchY + 60f), strokeWidth = 10f)
                    drawLine(color = ironBlack, start = Offset(centerX + 90f, benchY), end = Offset(centerX + 90f, benchY + 60f), strokeWidth = 10f)

                    // Draw Rack uprights
                    drawLine(color = secondarySteel, start = Offset(centerX - 60f, benchY - 80f), end = Offset(centerX - 60f, benchY + 60f), strokeWidth = 8f)
                    drawLine(color = secondarySteel, start = Offset(centerX + 60f, benchY - 80f), end = Offset(centerX + 60f, benchY + 60f), strokeWidth = 8f)

                    // Press motion: bar travels from benchY - 10f to benchY - 110f
                    val barY = (benchY - 10f) - (progress * 100f)

                    // Draw Body Torso
                    drawOval(color = secondarySteel.copy(alpha = 0.8f), topLeft = Offset(centerX - 50f, benchY - 20f), size = Size(100f, 40f))
                    drawCircle(color = secondarySteel, radius = 18f, center = Offset(centerX - 80f, benchY - 10f)) // Head

                    // Draw Arms holding barbell
                    val shoulderOffset = Offset(centerX - 20f, benchY - 10f)
                    val barHoldOffset = Offset(centerX - 20f, barY)
                    drawLine(color = secondarySteel, start = shoulderOffset, end = barHoldOffset, strokeWidth = 8f, cap = StrokeCap.Round)

                    val shoulderOffsetR = Offset(centerX + 20f, benchY - 10f)
                    val barHoldOffsetR = Offset(centerX + 20f, barY)
                    drawLine(color = secondarySteel, start = shoulderOffsetR, end = barHoldOffsetR, strokeWidth = 8f, cap = StrokeCap.Round)

                    // Draw Barbell
                    drawLine(color = primaryGold, start = Offset(centerX - 130f, barY), end = Offset(centerX + 130f, barY), strokeWidth = 8f)
                    // Draw Plates
                    drawRect(color = primaryGold, topLeft = Offset(centerX - 130f, barY - 25f), size = Size(14f, 50f))
                    drawRect(color = primaryGold, topLeft = Offset(centerX - 114f, barY - 20f), size = Size(10f, 40f))
                    drawRect(color = primaryGold, topLeft = Offset(centerX + 116f, barY - 25f), size = Size(14f, 50f))
                    drawRect(color = primaryGold, topLeft = Offset(centerX + 104f, barY - 20f), size = Size(10f, 40f))
                }

                ExerciseType.DUMBBELL_PRESS, ExerciseType.DUMBBELL_FLY -> {
                    // Draw Incline Bench
                    val p1 = Offset(centerX - 90f, centerY + 80f)
                    val p2 = Offset(centerX + 70f, centerY - 80f)
                    drawLine(color = ironBlack, start = p1, end = p2, strokeWidth = 12f, cap = StrokeCap.Round)
                    drawLine(color = ironBlack, start = Offset(centerX - 50f, centerY + 40f), end = Offset(centerX - 50f, centerY + 90f), strokeWidth = 10f)

                    // Draw Body Torso parallel to bench incline
                    val seatX = centerX - 30f
                    val seatY = centerY + 30f
                    drawOval(color = secondarySteel, topLeft = Offset(seatX - 40f, seatY - 50f), size = Size(90f, 50f))
                    drawCircle(color = secondarySteel, radius = 18f, center = Offset(seatX + 60f, seatY - 70f)) // Head

                    // Dumbbells pressed up diagonally/vertically
                    val dY = (centerY + 10f) - (progress * 110f)
                    val dXL = centerX - 50f - (progress * 15f)
                    val dXR = centerX + 40f + (progress * 15f)

                    // Left hand arm
                    drawLine(color = secondarySteel, start = Offset(seatX - 10f, seatY - 20f), end = Offset(dXL, dY), strokeWidth = 8f, cap = StrokeCap.Round)
                    // Right hand arm
                    drawLine(color = secondarySteel, start = Offset(seatX + 10f, seatY - 20f), end = Offset(dXR, dY), strokeWidth = 8f, cap = StrokeCap.Round)

                    // Draw dumbbells
                    drawDumbbell(this, Offset(dXL, dY), primaryGold)
                    drawDumbbell(this, Offset(dXR, dY), primaryGold)
                }

                ExerciseType.DEADLIFT -> {
                    // Ground line
                    drawLine(color = ironBlack, start = Offset(30f, centerY + 80f), end = Offset(w - 30f, centerY + 80f), strokeWidth = 8f)

                    // Pull-up progression: Hip pivot angle
                    // Progress from 0 (bent over) to 1 (standing tall)
                    val isStanding = progress
                    val hipX = centerX - 10f
                    val hipY = centerY + 20f

                    // Standing height
                    val shoulderX = centerX - 10f - (1f - isStanding) * 50f
                    val shoulderY = centerY - 50f + (1f - isStanding) * 40f

                    val headX = shoulderX - (1f - isStanding) * 15f
                    val headY = shoulderY - 25f

                    // Legs from hip to floor
                    drawLine(color = secondarySteel, start = Offset(centerX - 20f, centerY + 80f), end = Offset(hipX, hipY), strokeWidth = 10f)

                    // Torso block (Spine)
                    drawLine(color = secondarySteel, start = Offset(hipX, hipY), end = Offset(shoulderX, shoulderY), strokeWidth = 10f, cap = StrokeCap.Round)
                    // Head
                    drawCircle(color = secondarySteel, radius = 16f, center = Offset(headX, headY))

                    // Barbell height (Floor up to thighs)
                    val barY = centerY + 65f - (isStanding * 45f)
                    val barX = shoulderX

                    // Arms from shoulders to Barbell
                    drawLine(color = secondarySteel, start = Offset(shoulderX, shoulderY), end = Offset(barX, barY), strokeWidth = 7f, cap = StrokeCap.Round)

                    // Barbell
                    drawLine(color = primaryGold, start = Offset(barX - 110f, barY), end = Offset(barX + 110f, barY), strokeWidth = 8f)
                    // Big Plates
                    drawCircle(color = primaryGold, radius = 26f, center = Offset(barX - 110f, barY))
                    drawCircle(color = primaryGold, radius = 26f, center = Offset(barX + 110f, barY))
                    drawCircle(color = primaryGold.copy(alpha = 0.6f), radius = 20f, center = Offset(barX - 98f, barY))
                    drawCircle(color = primaryGold.copy(alpha = 0.6f), radius = 20f, center = Offset(barX + 98f, barY))
                }

                ExerciseType.CHEST_DIPS, ExerciseType.PULL_UP -> {
                    // Pull up frame / Dip Station
                    drawLine(color = ironBlack, start = Offset(centerX - 100f, centerY - 100f), end = Offset(centerX + 100f, centerY - 100f), strokeWidth = 10f, cap = StrokeCap.Round)
                    drawLine(color = ironBlack, start = Offset(centerX - 100f, centerY - 100f), end = Offset(centerX - 100f, centerY + 120f), strokeWidth = 8f)
                    drawLine(color = ironBlack, start = Offset(centerX + 100f, centerY - 100f), end = Offset(centerX + 100f, centerY + 120f), strokeWidth = 8f)

                    // Hanging displacement
                    val hangY = centerY + 60f - (progress * 80f)

                    // Arms bent/extended holding top bar at centerX - 50f & +50f
                    drawLine(color = secondarySteel, start = Offset(centerX - 50f, centerY - 100f), end = Offset(centerX - 35f, hangY - 40f), strokeWidth = 8f, cap = StrokeCap.Round)
                    drawLine(color = secondarySteel, start = Offset(centerX + 50f, centerY - 100f), end = Offset(centerX + 35f, hangY - 40f), strokeWidth = 8f, cap = StrokeCap.Round)

                    // Torso
                    drawLine(color = secondarySteel, start = Offset(centerX, hangY - 40f), end = Offset(centerX, hangY + 40f), strokeWidth = 12f, cap = StrokeCap.Round)
                    // Head
                    drawCircle(color = primaryGold, radius = 16f, center = Offset(centerX, hangY - 60f)) // Highlight head clearing bar!

                    // Legs
                    drawLine(color = secondarySteel, start = Offset(centerX, hangY + 40f), end = Offset(centerX - 10f, hangY + 100f), strokeWidth = 8f, cap = StrokeCap.Round)
                }

                ExerciseType.BARBELL_SQUAT -> {
                    // Ground line
                    drawLine(color = ironBlack, start = Offset(30f, centerY + 80f), end = Offset(w - 30f, centerY + 80f), strokeWidth = 8f)

                    // Height values: deep squat when progress = 0
                    val squatFactor = progress // 0 to 1
                    val hipY = (centerY + 60f) - (squatFactor * 50f)
                    val kneeY = centerY + 40f
                    val shoulderY = hipY - 70f

                    // Foot is locked on ground
                    val footX = centerX - 25f
                    val footY = centerY + 80f

                    // Lower leg
                    drawLine(color = secondarySteel, start = Offset(footX, footY), end = Offset(footX - 30f * (1f - squatFactor), kneeY), strokeWidth = 9f, cap = StrokeCap.Round)
                    // Upper leg (thigh)
                    drawLine(color = secondarySteel, start = Offset(footX - 30f * (1f - squatFactor), kneeY), end = Offset(centerX, hipY), strokeWidth = 9f, cap = StrokeCap.Round)
                    // Torso
                    drawLine(color = secondarySteel, start = Offset(centerX, hipY), end = Offset(centerX - 10f * (1f - squatFactor), shoulderY), strokeWidth = 10f, cap = StrokeCap.Round)
                    // Head
                    drawCircle(color = secondarySteel, radius = 16f, center = Offset(centerX - 10f * (1f - squatFactor), shoulderY - 20f))

                    // Barbell sits on shoulders
                    val barX = centerX - 10f * (1f - squatFactor)
                    val barY = shoulderY

                    drawLine(color = primaryGold, start = Offset(barX - 100f, barY), end = Offset(barX + 100f, barY), strokeWidth = 8f)
                    drawRect(color = primaryGold, topLeft = Offset(barX - 100f, barY - 20f), size = Size(15f, 40f))
                    drawRect(color = primaryGold, topLeft = Offset(barX + 85f, barY - 20f), size = Size(15f, 40f))
                }

                ExerciseType.PUSH_UP, ExerciseType.PLANK -> {
                    // Ground line
                    drawLine(color = ironBlack, start = Offset(30f, centerY + 65f), end = Offset(w - 30f, centerY + 65f), strokeWidth = 8f)

                    // If PUSH_UP, height pivots down/up dynamically. If PLANK, core pulses.
                    val isPushUp = (type == ExerciseType.PUSH_UP)
                    val travel = if (isPushUp) (1f - progress) * 25f else 0f

                    // Body suspended straight (toes at right, elbow/shoulder at left)
                    val toeX = centerX + 110f
                    val toeY = centerY + 60f
                    val elbowX = centerX - 90f
                    val elbowY = centerY + 30f + travel

                    // Forearm / Arm on ground pushing
                    if (isPushUp) {
                        // Draw moving arms pushing up/down
                        drawLine(color = secondarySteel, start = Offset(elbowX, elbowY), end = Offset(elbowX, centerY + 60f), strokeWidth = 10f, cap = StrokeCap.Round)
                        drawLine(color = secondarySteel, start = Offset(elbowX, centerY + 60f), end = Offset(elbowX + 40f, centerY + 60f), strokeWidth = 10f, cap = StrokeCap.Round)
                    } else {
                        // Forearm resting flat for static plank
                        drawLine(color = secondarySteel, start = Offset(elbowX, elbowY), end = Offset(elbowX + 40f, centerY + 60f), strokeWidth = 10f, cap = StrokeCap.Round)
                        drawLine(color = secondarySteel, start = Offset(elbowX, centerY + 60f), end = Offset(elbowX + 40f, centerY + 60f), strokeWidth = 10f, cap = StrokeCap.Round)
                    }

                    // Torso line
                    drawLine(color = secondarySteel, start = Offset(elbowX, elbowY), end = Offset(toeX, toeY), strokeWidth = 12f, cap = StrokeCap.Round)
                    drawCircle(color = secondarySteel, radius = 15f, center = Offset(elbowX - 20f, elbowY - 15f)) // Head

                    // Squeezing core indicator (Aura pulsing on abdomen!)
                    val pulseRadius = 15f + (progress * 25f)
                    val abX = centerX - 20f
                    val abY = centerY + 43f + (travel / 2f)
                    drawCircle(
                        color = pulseAccent.copy(alpha = 0.5f - (progress * 0.4f)),
                        radius = pulseRadius,
                        center = Offset(abX, abY)
                    )
                    drawCircle(
                        color = primaryGold,
                        radius = 8f,
                        center = Offset(abX, abY)
                    )
                }

                ExerciseType.CALF_RAISE -> {
                    // Step Block
                    drawRect(color = ironBlack, topLeft = Offset(centerX - 60f, centerY + 50f), size = Size(120f, 30f))

                    // Calf stretch movement: ankle pivots up and down
                    val liftHeight = progress * 35f

                    // Ankle and heel (hanging off)
                    val toeX = centerX + 10f
                    val toeY = centerY + 50f
                    val heelX = centerX - 40f
                    val heelY = centerY + 55f - liftHeight

                    // Calf/shin lines
                    drawLine(color = primaryGold, start = Offset(heelX, heelY), end = Offset(toeX, toeY), strokeWidth = 9f, cap = StrokeCap.Round) // Active target foot
                    drawLine(color = secondarySteel, start = Offset(toeX, toeY), end = Offset(centerX - 10f, centerY - 60f - liftHeight), strokeWidth = 9f, cap = StrokeCap.Round) // Lower leg

                    // Torso standing tall
                    val bodyY = centerY - 140f - liftHeight
                    drawLine(color = secondarySteel, start = Offset(centerX - 10f, centerY - 60f - liftHeight), end = Offset(centerX - 10f, bodyY), strokeWidth = 11f, cap = StrokeCap.Round)
                    drawCircle(color = secondarySteel, radius = 16f, center = Offset(centerX - 10f, bodyY - 20f))
                }

                ExerciseType.OVERHEAD_PRESS -> {
                    // Floor
                    drawLine(color = ironBlack, start = Offset(50f, centerY + 80f), end = Offset(w - 50f, centerY + 80f), strokeWidth = 8f)

                    // Standing Frame
                    drawLine(color = secondarySteel, start = Offset(centerX, centerY + 80f), end = Offset(centerX, centerY - 40f), strokeWidth = 10f, cap = StrokeCap.Round) // Spine
                    drawCircle(color = secondarySteel, radius = 16f, center = Offset(centerX, centerY - 60f)) // Head

                    // Overhead bar progression
                    val pressY = (centerY - 35f) - (progress * 105f)

                    // Arms reaching to press
                    drawLine(color = secondarySteel, start = Offset(centerX - 15f, centerY - 45f), end = Offset(centerX - 40f, pressY), strokeWidth = 7f, cap = StrokeCap.Round)
                    drawLine(color = secondarySteel, start = Offset(centerX + 15f, centerY - 45f), end = Offset(centerX + 40f, pressY), strokeWidth = 7f, cap = StrokeCap.Round)

                    // Barbell
                    drawLine(color = primaryGold, start = Offset(centerX - 110f, pressY), end = Offset(centerX + 110f, pressY), strokeWidth = 8f)
                    drawCircle(color = primaryGold, radius = 22f, center = Offset(centerX - 100f, pressY))
                    drawCircle(color = primaryGold, radius = 22f, center = Offset(centerX + 100f, pressY))
                }

                ExerciseType.CABLE_FLY, ExerciseType.LATERAL_RAISE -> {
                    // Draw Standing straight torso
                    drawLine(color = secondarySteel, start = Offset(centerX, centerY + 70f), end = Offset(centerX, centerY - 40f), strokeWidth = 12f)
                    drawCircle(color = secondarySteel, radius = 18f, center = Offset(centerX, centerY - 62f))

                    // Angle of raises from down (10 degrees) to horizontal (90 degrees / parallel)
                    val angleProgress = progress
                    val armLength = 80f
                    val leftArmEndX = centerX - 15f - (angleProgress * armLength)
                    val leftArmEndY = centerY - 30f + ((1f - angleProgress) * 70f)
                    
                    val rightArmEndX = centerX + 15f + (angleProgress * armLength)
                    val rightArmEndY = centerY - 30f + ((1f - angleProgress) * 70f)

                    // Hands/arms drawn out to shoulder girdle width
                    drawLine(color = primaryGold, start = Offset(centerX - 15f, centerY - 30f), end = Offset(leftArmEndX, leftArmEndY), strokeWidth = 8f, cap = StrokeCap.Round)
                    drawLine(color = primaryGold, start = Offset(centerX + 15f, centerY - 30f), end = Offset(rightArmEndX, rightArmEndY), strokeWidth = 8f, cap = StrokeCap.Round)

                    // Small Hand Dumbbells
                    drawDumbbell(this, Offset(leftArmEndX, leftArmEndY), primaryGold)
                    drawDumbbell(this, Offset(rightArmEndX, rightArmEndY), primaryGold)
                }

                ExerciseType.BICEP_CURL -> {
                    // Profile body
                    drawCircle(color = secondarySteel, radius = 18f, center = Offset(centerX - 30f, centerY - 70f)) // Head
                    drawLine(color = secondarySteel, start = Offset(centerX - 35f, centerY - 50f), end = Offset(centerX - 35f, centerY + 60f), strokeWidth = 12f) // Back torso

                    val elbowX = centerX - 25f
                    val elbowY = centerY - 10f
                    // Upper arm pinned vertically down
                    drawLine(color = secondarySteel, start = Offset(centerX - 30f, centerY - 45f), end = Offset(elbowX, elbowY), strokeWidth = 10f)

                    // Lower pivoting forearm (moves up in arc from 0 to 1)
                    val curlAngle = progress * 135f // up to 135 deg flex
                    val length = 60f
                    val angleRads = Math.toRadians((90f - curlAngle).toDouble())
                    val wristX = elbowX + (length * Math.cos(angleRads)).toFloat()
                    val wristY = elbowY - (length * Math.sin(angleRads)).toFloat()

                    drawLine(color = primaryGold, start = Offset(elbowX, elbowY), end = Offset(wristX, wristY), strokeWidth = 8f, cap = StrokeCap.Round) // Highlight targeted biceps!
                    drawDumbbell(this, Offset(wristX, wristY), primaryGold)
                }

                ExerciseType.TRICEP_EXTENSION -> {
                    // Profile body
                    drawCircle(color = secondarySteel, radius = 18f, center = Offset(centerX - 30f, centerY - 40f)) // Head
                    drawLine(color = secondarySteel, start = Offset(centerX - 35f, centerY - 20f), end = Offset(centerX - 35f, centerY + 90f), strokeWidth = 12f) // Torso

                    // Upper arm pointing almost vertically skyward
                    val shoulderOffset = Offset(centerX - 30f, centerY - 15f)
                    val elbowOffset = Offset(centerX - 20f, centerY - 80f)
                    drawLine(color = secondarySteel, start = shoulderOffset, end = elbowOffset, strokeWidth = 10f)

                    // Pivot forearm from behind back (0 = flexed back) to overhead locked (1 = fully extended)
                    val extensionFactor = progress
                    val forearmAngle = -120f + (extensionFactor * 130f) // from -120 flex to +10 extension
                    val armLen = 55f
                    val extRads = Math.toRadians(forearmAngle.toDouble())
                    val dbX = elbowOffset.x + (armLen * Math.cos(extRads)).toFloat()
                    val dbY = elbowOffset.y - (armLen * Math.sin(extRads)).toFloat()

                    drawLine(color = primaryGold, start = elbowOffset, end = Offset(dbX, dbY), strokeWidth = 8f, cap = StrokeCap.Round) // Highlight triceps extension!
                    drawDumbbell(this, Offset(dbX, dbY), primaryGold)
                }

                ExerciseType.LEG_RAISE -> {
                    // Bar pullup hook
                    drawLine(color = ironBlack, start = Offset(centerX - 60f, centerY - 100f), end = Offset(centerX + 60f, centerY - 100f), strokeWidth = 8f)

                    // Hanging upper torso static
                    drawLine(color = secondarySteel, start = Offset(centerX, centerY - 100f), end = Offset(centerX, centerY + 10f), strokeWidth = 10f, cap = StrokeCap.Round)
                    drawCircle(color = secondarySteel, radius = 16f, center = Offset(centerX, centerY - 80f))

                    // Legs swinging from straight down (angle = 90 deg) to straight out (angle = 180 deg)
                    val swFactor = progress
                    val swingAngle = 90f + (swFactor * 90f) // swing 90 degrees
                    val swingRad = Math.toRadians(swingAngle.toDouble())
                    val legLen = 80f
                    val legEndX = centerX + (legLen * Math.cos(swingRad)).toFloat()
                    val legEndY = (centerY + 10f) + (legLen * Math.sin(swingRad)).toFloat()

                    drawLine(color = primaryGold, start = Offset(centerX, centerY + 10f), end = Offset(legEndX, legEndY), strokeWidth = 8f, cap = StrokeCap.Round) // Highlight abs workload
                }

                ExerciseType.PLANK -> {
                    // Ground line
                    drawLine(color = ironBlack, start = Offset(30f, centerY + 65f), end = Offset(w - 30f, centerY + 65f), strokeWidth = 8f)

                    // Body suspended perfectly straight (toes at right, elbow at left)
                    val toeX = centerX + 110f
                    val toeY = centerY + 60f
                    val elbowX = centerX - 90f
                    val elbowY = centerY + 30f

                    // Forearm resting flat
                    drawLine(color = secondarySteel, start = Offset(elbowX, elbowY), end = Offset(elbowX + 40f, centerY + 60f), strokeWidth = 10f, cap = StrokeCap.Round)
                    drawLine(color = secondarySteel, start = Offset(elbowX, centerY + 60f), end = Offset(elbowX + 40f, centerY + 60f), strokeWidth = 10f, cap = StrokeCap.Round) // arm on ground

                    // Perfectly rigid core line from shoulder/elbow to heel
                    drawLine(color = secondarySteel, start = Offset(elbowX, elbowY), end = Offset(toeX, toeY), strokeWidth = 12f, cap = StrokeCap.Round)
                    drawCircle(color = secondarySteel, radius = 15f, center = Offset(elbowX - 20f, elbowY - 15f)) // Head

                    // Squeezing core indicator (Aura pulsing on abdomen!)
                    val pulseRadius = 15f + (progress * 25f)
                    val abX = centerX - 20f
                    val abY = centerY + 43f
                    drawCircle(
                        color = pulseAccent.copy(alpha = 0.5f - (progress * 0.4f)),
                        radius = pulseRadius,
                        center = Offset(abX, abY)
                    )
                    drawCircle(
                        color = primaryGold,
                        radius = 8f,
                        center = Offset(abX, abY)
                    )
                }
            }
        }
    }
}

// Helper to draw a dumbbell on Canvas easily
private fun drawDumbbell(drawScope: DrawScope, center: Offset, color: Color) {
    val sizePlate = 14f
    val lenHandle = 16f
    // Handle
    drawScope.drawLine(
        color = color,
        start = Offset(center.x - lenHandle, center.y),
        end = Offset(center.x + lenHandle, center.y),
        strokeWidth = 5f
    )
    // Left Bell plates
    drawScope.drawRect(
        color = color,
        topLeft = Offset(center.x - lenHandle - 4f, center.y - sizePlate),
        size = Size(8f, sizePlate * 2)
    )
    // Right Bell plates
    drawScope.drawRect(
        color = color,
        topLeft = Offset(center.x + lenHandle - 4f, center.y - sizePlate),
        size = Size(8f, sizePlate * 2)
    )
}
