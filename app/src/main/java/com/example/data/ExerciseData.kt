package com.example.data

enum class ExerciseType {
    BENCH_PRESS,
    DUMBBELL_PRESS,
    DECLINE_PRESS,
    CHEST_DIPS,
    DUMBBELL_FLY,
    CABLE_FLY,
    PUSH_UP,
    DEADLIFT,
    PULL_UP,
    BARBELL_SQUAT,
    CALF_RAISE,
    OVERHEAD_PRESS,
    LATERAL_RAISE,
    BICEP_CURL,
    TRICEP_EXTENSION,
    LEG_RAISE,
    PLANK
}

data class Exercise(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val trainerTips: String,
    val steps: List<String>,
    val type: ExerciseType
)

object ExerciseData {
    val categories = listOf(
        "Chest",
        "Back",
        "Legs",
        "Shoulders",
        "Biceps",
        "Triceps",
        "Abs"
    )

    val exercises = listOf(
        // CHEST
        Exercise(
            id = "bench_press",
            name = "Flat Bench Press",
            category = "Chest",
            description = "The absolute king of chest exercises. Targets the sternal head of the pectoralis major, triceps, and anterior deltoids.",
            trainerTips = "Keep your feet flat on the floor and maintain a slight arch in your lower back. Retract your scapula (squeeze shoulder blades) before unracking the bar to protect your shoulders.",
            steps = listOf(
                "Lie flat on the bench under the barbell, eyes directly below the bar.",
                "Grip the bar with hands slightly wider than shoulder-width, palms facing away.",
                "Unrack the barbell and hold it straight over your chest with locked elbows.",
                "Lower the bar slowly to your mid-chest line, keeping elbows at roughly a 45-degree angle.",
                "Push the bar explosively back to the starting position while pressing your feet into the floor."
            ),
            type = ExerciseType.BENCH_PRESS
        ),
        Exercise(
            id = "dumbbell_press",
            name = "Incline Dumbbell Press",
            category = "Chest",
            description = "Superb movement for prioritizing the clavicular (upper) head of the chest, allowing for a deeper stretch and more natural movement path.",
            trainerTips = "Set the incline bench to a moderate 30-degree angle. Higher angles shift the focus too much onto your front shoulders rather than your upper chest.",
            steps = listOf(
                "Sit on an incline bench with a dumbbell in each hand, resting them on your thighs.",
                "Lie back and press the dumbbells up above your chest, palms facing forward.",
                "Slowly lower the dumbbells down to the sides of your upper chest, elbows slightly tucked.",
                "Once you feel a comfortable stretch in your pecs, press the weights back up to the top."
            ),
            type = ExerciseType.DUMBBELL_PRESS
        ),
        Exercise(
            id = "decline_bench_press",
            name = "Decline Barbell Press",
            category = "Chest",
            description = "Mainly targets the sternal and lower costal head of the pectoralis major. The decline angle reduces strain on the shoulders while allowing you to lift heavier loads.",
            trainerTips = "Secure your shins/feet securely under the pads. Always have a spotter for decline barbell work, as unracking and racking can be mechanically awkward.",
            steps = listOf(
                "Secure your legs at the end of the decline bench and slowly lie down.",
                "Grip the barbell with a medium-wide overhand grip, eyes directly under the bar.",
                "Unrack the barbell and steady it over your lower chest.",
                "Lower the bar slowly to the base of your sternum, keeping elbows tucked in at a 45-degree angle.",
                "Press the bar back up to the start, emphasizing chest contraction."
            ),
            type = ExerciseType.DECLINE_PRESS
        ),
        Exercise(
            id = "chest_dips",
            name = "Weighted Chest Dips",
            category = "Chest",
            description = "An excellent lower chest builder that also heavily recruits the anterior deltoids and triceps. Done on parallel bars.",
            trainerTips = "To target the chest rather than triceps, lean your torso forward (about 30 degrees) and let your elbows flare slightly outward during the descent.",
            steps = listOf(
                "Grab the parallel bars and hoist yourself up, locking your elbows.",
                "Cross your legs and lean your torso forward at a slight angle.",
                "Lower yourself slowly by bending elbows, letting them flare out slightly, until your shoulders are below elbow level.",
                "Press through your chest and triceps to push your body back up to starting position."
            ),
            type = ExerciseType.CHEST_DIPS
        ),
        Exercise(
            id = "dumbbell_flys",
            name = "Flat Dumbbell Flys",
            category = "Chest",
            description = "An isolation movement designed to stretch the muscle fibers of the outer pectorals and squeeze the inner chest at the peak.",
            trainerTips = "Keep a slight bend in your elbows throughout the entire range of motion to avoid excessive biceps and joint strain. Never slam the dumbbells at the top.",
            steps = listOf(
                "Lie back on a flat bench holding dumbbells over your chest, palms facing each other.",
                "With elbows slightly bent, lower your arms outward in a wide arc until you feel a deep stretch in your chest.",
                "Reverse the movement in the same wide arc path, squeezing your chest at the top as if hugging a barrel."
            ),
            type = ExerciseType.DUMBBELL_FLY
        ),
        Exercise(
            id = "cable_crossovers",
            name = "Standing Cable Flys",
            category = "Chest",
            description = "Provides constant tension on the chest muscles, especially in the fully contracted position. Outstanding for inner chest definition.",
            trainerTips = "Stand in a staggered stance for stability. Focus on squeezing your hands together and reaching forward as if clapping, contracting the inner pecs.",
            steps = listOf(
                "Set the pulleys to shoulder-height or higher, grab both handles and step forward in a staggered stance.",
                "Start with arms extended wide but slightly bent, torso leaning forward slightly.",
                "Bring your hands together in a wide arc sweep path in front of your mid-torso.",
                "Squeeze your chest muscles firmly at the center crossover point, then slowly return to start."
            ),
            type = ExerciseType.CABLE_FLY
        ),
        Exercise(
            id = "push_ups",
            name = "Classic Floor Push-Up",
            category = "Chest",
            description = "The foundational bodyweight chest builder. Builds muscular endurance, core stability, and shoulder integrity.",
            trainerTips = "Squeeze your glutes and brace your abs to prevent your lower back from sagging. Your body should remain in a perfect straight line from head to toe.",
            steps = listOf(
                "Get into a high plank position with hands slightly wider than shoulder-width, palms flat on floor.",
                "Lower your chest to the floor by bending your elbows, keeping them tucked at 45 degrees.",
                "Push hard through your hands to return back to the top position, locking out your arms."
            ),
            type = ExerciseType.PUSH_UP
        ),

        // BACK
        Exercise(
            id = "deadlift",
            name = "Barbell Deadlift",
            category = "Back",
            description = "A foundational compound lift that strengthens the entire posterior chain, including the spinal erectors, lats, glutes, and hamstrings.",
            trainerTips = "Never let your lower back round! Pull the slack out of the bar before you lift. Keep the barbell in constant contact with your shins and thighs during the movement.",
            steps = listOf(
                "Stand with feet shoulder-width apart, shins about an inch from the bar, barbell over mid-foot.",
                "Bend at the hips and knees to grip the bar with a shoulder-width double-overhand grip.",
                "Flatten your back, chest up, pull shoulder blades back and down, and engage your core.",
                "Drive through your heels to stand fully upright, keeping the bar close to your body.",
                "Hinge at the hips, keeping the bar sliding down your thighs, then bend knees to return bar to floor."
            ),
            type = ExerciseType.DEADLIFT
        ),
        Exercise(
            id = "pull_up",
            name = "Bodyweight Pull-Up",
            category = "Back",
            description = "An elite bodyweight measurement of relative strength. Targets the latissimus dorsi (lats), teres major, lower traps, and biceps.",
            trainerTips = "Concentrate on pulling from your elbows rather than pulling with your hands. Fully extend at the bottom, but do not relax your shoulder girdle.",
            steps = listOf(
                "Grasp the pull-up bar with an overhand grip slightly wider than shoulder-width, arms fully extended.",
                "Squeeze your shoulder blades down and back, and engage your core to prevent swinging.",
                "Pull yourself up by driving your elbows down toward your ribs until your chin clears the bar.",
                "Control your descent slowly until your arms are fully extended at the bottom."
            ),
            type = ExerciseType.PULL_UP
        ),
        Exercise(
            id = "barbell_row",
            name = "Bent-Over Barbell Row",
            category = "Back",
            description = "One of the absolute best compound exercises for back thickness, targeting the lats, rhomboids, rear delts, and middle/lower traps.",
            trainerTips = "Keep your spine neutral (no rounding) and pull the bar to your lower rib cage or abdominal area.",
            steps = listOf(
                "Hold a barbell with an overhand grip, feet shoulder-width apart.",
                "Hinge at your hips, bending slightly at the knees, keeping your back flat and almost parallel to the floor.",
                "Pull the barbell up to your lower rib cage, squeezing your shoulder blades together at the top.",
                "Lower the bar back down to the starting position under full control."
            ),
            type = ExerciseType.DEADLIFT
        ),
        Exercise(
            id = "lat_pulldown",
            name = "Wide-Grip Lat Pulldown",
            category = "Back",
            description = "A classic machine alternative to the pull-up, specializing in building sweeping lat width and dynamic upper body pull power.",
            trainerTips = "Pull primarily with your elbows, and lean your upper torso back slightly to let the bar clear your chin to touch your upper chest.",
            steps = listOf(
                "Sit at a pulldown station and adjust the pad. Grab the bar with a wide overhand grip.",
                "Brace your core, draw your shoulder blades flat, and pull the bar down to your upper chest.",
                "Squeeze your lats tightly at the bottom for a fraction of a second.",
                "Engage in a slow, controlled release back up to full vertical arm extension."
            ),
            type = ExerciseType.PULL_UP
        ),
        Exercise(
            id = "dumbbell_row",
            name = "Single-Arm Dumbbell Row",
            category = "Back",
            description = "Unilateral lat work that eliminates muscular imbalances and allows a greater deep range of motion for back hypertrophy.",
            trainerTips = "Keep your hips and shoulders square. Pull the dumbbell back toward your waistline rather than straight up to maximize lat loading.",
            steps = listOf(
                "Place your knee and hand flat on a flat bench. Keep your outer leg on the floor and hold a dumbbell.",
                "Start with your arm fully extended toward the floor, keeping your back flat.",
                "Drive your elbow back and up, pulling the weight of the dumbbell toward your waistline.",
                "Lower the dumbbell controlled to the starting position."
            ),
            type = ExerciseType.DEADLIFT
        ),

        // LEGS
        Exercise(
            id = "barbell_squat",
            name = "Barbell Back Squat",
            category = "Legs",
            description = "The ultimate leg builder, recruiting quads, glutes, hamstrings, and the entire core to stabilize the heavy load.",
            trainerTips = "Push your knees outward on the way down and the way up to keep them aligned with your toes. Descend until your thighs are at least parallel to the floor.",
            steps = listOf(
                "Position the barbell on your upper back traps (not your neck). Lift and step back.",
                "Set your feet shoulder-width apart, toes pointed slightly outward (approx. 15-30 degrees).",
                "Brace your core, inhale deep, hinge hips back and bend knees to initiate descent.",
                "Squat down controlled until parallel or deeper, keeping chest upright and weight centered in feet.",
                "Drive explosively through your mid-foot, pushing knees out, back to the standing position."
            ),
            type = ExerciseType.BARBELL_SQUAT
        ),
        Exercise(
            id = "calf_raise",
            name = "Standing Calf Raises",
            category = "Legs",
            description = "Targets the gastrocnemius muscle of the calves. Essential for building ankle stability and explosive lower leg power.",
            trainerTips = "Achieve a full, deep stretch at the bottom of every rep, hold for a second, and raise yourself to the very tips of your toes. Do not bounce!",
            steps = listOf(
                "Stand with your toes on a raised platform, heels hanging off, holding a dumbbell or barbell.",
                "Lower your heels as far as comfortably possible to feel a deep stretch in your calves.",
                "Press through the balls of your feet, lifting your heels as high as possible.",
                "Squeeze your calves tightly at the peak of the contraction for a full second before lowering."
            ),
            type = ExerciseType.CALF_RAISE
        ),
        Exercise(
            id = "romanian_deadlift",
            name = "Barbell Romanian Deadlift",
            category = "Legs",
            description = "Elite compound movement focusing heavily on high hamstring stretch, glute activation, and spine loading.",
            trainerTips = "Push your hips far back as if trying to press them against a wall. Do NOT squat the weight down; prioritize the stretch on your hamstrings.",
            steps = listOf(
                "Stand with feet hip-width apart, holding a barbell at your thighs with an overhand grip.",
                "Hinge at the hips, keeping your knees slightly bent as you push your pelvis backwards.",
                "Lower the barbell down the front of your shins until you feel a deep stretch in your hamstrings.",
                "Drive your hips forward and squeeze your glutes hard to return back to standing."
            ),
            type = ExerciseType.DEADLIFT
        ),
        Exercise(
            id = "leg_press",
            name = "45-Degree Leg Press",
            category = "Legs",
            description = "Machine compound vertical press that isolates the quadriceps and glutes without loading the spinal column.",
            trainerTips = "Never bend your knees past a 90-degree angle, and never fully lock out your knees at the top to avoid hyperextension risks.",
            steps = listOf(
                "Sit in the leg-press machine with your back flat. Place feet shoulder-width on the sled.",
                "Disengage the safety locks and slowly lower the platform toward your chest until knees are bent at 90 degrees.",
                "Press through your heels, pushing the sled back up to extension.",
                "Keep knees slightly bent at the top vertical point."
            ),
            type = ExerciseType.BARBELL_SQUAT
        ),
        Exercise(
            id = "walking_lunges",
            name = "Dumbbell Walking Lunges",
            category = "Legs",
            description = "An excellent unilateral leg movement targeting the quadriceps, glutes, hamstrings, and building dynamic balance.",
            trainerTips = "Take long steps to target the glutes and hamstrings, or shorter steps to focus on the quads. Keep your front knee aligned with your toes.",
            steps = listOf(
                "Stand tall, holding a dumbbell in each hand at your sides.",
                "Step forward with your right leg, lowering your hips until your rear knee is just above the floor.",
                "Keep your torso upright and front knee directly over your ankle.",
                "Push off your right foot to step forward with your left leg, moving into the next lunge."
            ),
            type = ExerciseType.BARBELL_SQUAT
        ),

        // SHOULDERS
        Exercise(
            id = "overhead_press",
            name = "Barbell Overhead Press",
            category = "Shoulders",
            description = "The classic military press. Rebuilds core stabilization and absolute raw power in the deltoids and triceps.",
            trainerTips = "Keep your glutes and quads tightly squeezed throughout. This solidifies your foundation and prevents excess arching in your lower back.",
            steps = listOf(
                "Rack the barbell near upper chest height. Grip slightly wider than shoulder-width.",
                "Step back, feet shoulder-width, rack the bar across your front collarbones, elbows slightly forward.",
                "Squeeze your entire legs and core, pull your chin back, and push the bar straight up to the ceiling.",
                "As the bar clears your forehead, push your head slightly forward, locking out elbows above.",
                "Slowly return the bar to the collarbone shelf, keeping a vertical path."
            ),
            type = ExerciseType.OVERHEAD_PRESS
        ),
        Exercise(
            id = "lateral_raise",
            name = "Dumbbell Lateral Raise",
            category = "Shoulders",
            description = "Isolated movement that targets the lateral (side) deltoids, creating that broad, classic 'V-taper' shoulder width.",
            trainerTips = "Lean slightly forward and lead the movement with your elbows, keeping a very slight bend in them. Imagine pouring out a pitcher of water at the top.",
            steps = listOf(
                "Stand tall with dumbbells at your sides, chest out, shoulders rolled back and down.",
                "Raise your arms out to the sides in a wide arc (not straight up), palms facing down.",
                "Continue until your arms are parallel to the floor, level with your shoulders.",
                "Pause for a fraction of a second, then lower the dumbbells back down under control."
            ),
            type = ExerciseType.LATERAL_RAISE
        ),
        Exercise(
            id = "arnold_press",
            name = "Dumbbell Arnold Press",
            category = "Shoulders",
            description = "Converts standard pressing to rotate the hands, hitting all three heads of the deltoids (front, lateral, rear) dynamically.",
            trainerTips = "Go lighter on this variation than standard press. Keep the rotation fluid and continuous from chest-level up to overhead lockout.",
            steps = listOf(
                "Sit on a bench with back support, holding dumbbells in front of your upper chest, palms facing your chest.",
                "As you press the weights up, slowly rotate your palms outward to face forward.",
                "At the top, lock out your elbows in a standard overhead position.",
                "Lower the weights slowly, rotating your palms back inward to the start."
            ),
            type = ExerciseType.OVERHEAD_PRESS
        ),
        Exercise(
            id = "rear_delt_fly",
            name = "Bent-Over Rear Delt Fly",
            category = "Shoulders",
            description = "Isolates the posterior deltoid (rear shoulder), which is crucial for structural shoulder balance and that 3D aesthetic.",
            trainerTips = "Lead with your pinkies at the top to maximally load the rear delts rather than the upper back traps.",
            steps = listOf(
                "Hold dumbbells, bend at hips until your torso is almost flat and parallel to the floor.",
                "With a slight bend in your elbows, raise your arms out to the sides until parallel to the floor.",
                "Contract your rear shoulders at the peak, then slowly lower the weights down."
            ),
            type = ExerciseType.LATERAL_RAISE
        ),

        // BICEPS
        Exercise(
            id = "bicep_curl",
            name = "Standing Barbell Curl",
            category = "Biceps",
            description = "The gold standard for bicep thickness and mass, targeting both the short and long heads of the biceps brachii.",
            trainerTips = "Keep your elbows pinned to your sides! Do not swing your hips or use momentum to raise the barbell; keep the movement strictly isolated in the biceps.",
            steps = listOf(
                "Stand straight, holding a barbell with an underhand grip (palms up) at shoulder-width.",
                "Keep your feet planted, elbows tight to your ribs, and core firmly braced.",
                "Curl the barbell upward, contracting your biceps until the bar is near shoulder height.",
                "Squeeze your biceps hard at the peak, then slowly lower the bar back to the starting point."
            ),
            type = ExerciseType.BICEP_CURL
        ),
        Exercise(
            id = "incline_db_curl",
            name = "Incline Dumbbell Curl",
            category = "Biceps",
            description = "Puts the long head of the biceps on an extreme stretch, yielding maximum peak activation.",
            trainerTips = "Keep your shoulders retracted back and do not let your elbows drift forward as you lift the weight.",
            steps = listOf(
                "Sit on an incline bench set to 45 degrees, dumbbells resting hanging fully down.",
                "Lock your elbows in place, pinch your shoulders back, and curl the dumbbells up toward your shoulders.",
                "Squeeze your biceps tightly at the top before slowly lowering down."
            ),
            type = ExerciseType.BICEP_CURL
        ),
        Exercise(
            id = "hammer_curl",
            name = "Dumbbell Hammer Curl",
            category = "Biceps",
            description = "Neutral-grip curl that hits the biceps, brachialis, and brachioradialis (forearm). Formulates massive forearm thickness.",
            trainerTips = "Think of squeezing your thumbs upward. Try to completely avoid any swinging; keep your hips completely stationary.",
            steps = listOf(
                "Stand straight with dumbbells in hand, palms facing each other (neutral grip).",
                "Keep your elbows tucked into your sides.",
                "Curl the weights up while keeping your palms facing each other throughout the lift.",
                "Squeeze your arms at the top, then slowly lower the dumbbells back down."
            ),
            type = ExerciseType.BICEP_CURL
        ),
        Exercise(
            id = "concentration_curl",
            name = "Seated Combination Concentration Curl",
            category = "Biceps",
            description = "Ultimate isolation curl that completely removes secondary muscle help, maximizing biceps fiber recruitment.",
            trainerTips = "Brace your elbow firmly against the inside of your thigh, and concentrate on folding your arm purely via the biceps.",
            steps = listOf(
                "Sit on the edge of a bench with legs spread wide, holding a dumbbell in one hand.",
                "Bend forward, bracing your working elbow against the inside of your corresponding thigh.",
                "Slowly curl the dumbbell upward toward your face, focusing fully on the biceps muscle.",
                "Contract firmly at the top, then lower the dumbbell back under strict control."
            ),
            type = ExerciseType.BICEP_CURL
        ),

        // TRICEPS
        Exercise(
            id = "triceps_extension",
            name = "Overhead Triceps Extension",
            category = "Triceps",
            description = "Targets the long head of the triceps which makes up the bulk of upper arm size. Done sitting or standing with a dumbbell.",
            trainerTips = "Keep your upper arms vertical and close to your ears. Don't let your elbows flare out wide; keep them pointing forward as much as possible.",
            steps = listOf(
                "Sit or stand tall, holding a heavy dumbbell with both hands underneath the inner plate.",
                "Raise the dumbbell straight overhead, locking out your arms to the ceiling.",
                "Bend at your elbows, lowering the dumbbell behind your head in a slow, controlled semicircle.",
                "Extend your elbows back to raise the dumbbell, squeezing your triceps hard at the top."
            ),
            type = ExerciseType.TRICEP_EXTENSION
        ),
        Exercise(
            id = "triceps_pushdown",
            name = "Triceps Rope Pushdown",
            category = "Triceps",
            description = "Excellent isolation exercise targeting the lateral and medial heads of the triceps with constant cable tension.",
            trainerTips = "Flare the rope apart at the very bottom of the rep to force a peak contraction in the lateral triceps head.",
            steps = listOf(
                "Attach a double-rope handle to a high cable pulley. Grab the rope with a neutral grip.",
                "Keep your elbows glued to your rib cage and lean your torso slightly forward.",
                "Push the rope straight down to the floor, locking out your elbows completely.",
                "Pull your hands apart at the bottom to maximize the squeeze, then return up slowly."
            ),
            type = ExerciseType.TRICEP_EXTENSION
        ),
        Exercise(
            id = "skull_crushers",
            name = "EZ-Bar Skull Crushers",
            category = "Triceps",
            description = "One of the most potent mass-builders of the triceps, working the high attachment of the long head on a flat bench.",
            trainerTips = "Angle your upper arms slightly backward (towards your head) to keep constant tension on the triceps throughout the range.",
            steps = listOf(
                "Lie flat on a flat bench holding an EZ-curl bar above your chest with shoulder-width grip.",
                "Angle your upper arms slightly back toward your head.",
                "Keep your elbows tucked and stationary, bending only at the elbows to lower the bar to your forehead or just behind.",
                "Extend your elbows back up to full lockout, squeezing triceps hard."
            ),
            type = ExerciseType.TRICEP_EXTENSION
        ),
        Exercise(
            id = "close_grip_press",
            name = "Close-Grip Bench Press",
            category = "Triceps",
            description = "Compound movement that targets the triceps heavily while still recruiting the chest and front delts.",
            trainerTips = "Do not hold your hands too close together (standard shoulder-width is perfect) to avoid excessive wrist strain. Keep your elbows tucked.",
            steps = listOf(
                "Lie flat on a bench and grip the barbell with hands roughly shoulder-width apart.",
                "Unrack the bar and hold it stable over your upper chest.",
                "Lower the bar slowly to your lower chest, keeping your elbows tucked tightly against your ribs.",
                "Press the bar up explosively, driving through your triceps at lockout."
            ),
            type = ExerciseType.BENCH_PRESS
        ),

        // ABS
        Exercise(
            id = "leg_raise",
            name = "Hanging Leg Raise",
            category = "Abs",
            description = "Powerful core exercise prioritizing the lower rectus abdominis and hip flexors. Requires shoulder and grip engagement.",
            trainerTips = "Avoid using momentum or swinging. If hanging is too difficult, bend your knees to do hanging knee raises, then slowly build up to straight legs.",
            steps = listOf(
                "Hang from a pull-up bar with an overhand grip, arms and body fully straight.",
                "Engage your lats and core, keeping your legs completely together.",
                "Exhale and raise your legs up in front of you until they are at least parallel to the floor.",
                "Lower your legs slowly back to the vertical position, resisting gravity to prevent swinging."
            ),
            type = ExerciseType.LEG_RAISE
        ),
        Exercise(
            id = "ab_rollout",
            name = "Ab Wheel Rollout",
            category = "Abs",
            description = "Advanced eccentric core training that forces your abs, hip flexors, and lower back to brace with supreme high-tension output.",
            trainerTips = "Keep your lower back rounded at all times (like a cat). If your lower back arches, you are transferring strain to your spine instead of your abs.",
            steps = listOf(
                "Kneel on the floor, holding the handles of an ab wheel directly beneath your shoulders.",
                "Keep your abs fully braced, tuck your tailbone as you roll the wheel forward.",
                "Extend out as far as possible without letting your hips sag or back arch.",
                "Pull yourself back to the starting kneeling position using your abdominal wall."
            ),
            type = ExerciseType.PLANK
        ),
        Exercise(
            id = "cable_crunch",
            name = "Kneeling Cable Crunch",
            category = "Abs",
            description = "Allows weighted progressive overload for the rectus abdominis, generating deeper abdominal ridges.",
            trainerTips = "Do not pull the rope with your arms or sit down on your heels. Lock your hips in place and fold your upper torso solely via your abdominal wall.",
            steps = listOf(
                "Kneel in front of a high pulley set with a rope attachment. Pull rope to the sides of your head.",
                "Hips should stay high and locked. Forcefully flex your core to pull your elbows down toward your knees.",
                "Squeeze your abs tightly at the bottom peak contraction.",
                "Control the weighted return to the top position, maintaining abdominal tension."
            ),
            type = ExerciseType.LEG_RAISE
        ),
        Exercise(
            id = "russian_twist",
            name = "Weighted Russian Twist",
            category = "Abs",
            description = "Dynamic rotational core training that targets the internal and external oblique muscles for midsection stability.",
            trainerTips = "Follow your hands/plate rotation with your head and shoulders to ensure actual spinal rotation rather than just arm movement.",
            steps = listOf(
                "Sit on the floor with knees bent and feet hovered a few inches off the floor to balance.",
                "Hold a weight plate, dumbbell, or medicine ball with both hands in front of your chest.",
                "Twist your torso to the right, tapping the weight on the floor beside your hip.",
                "Reverse direction and twist your torso to the left, repeating the tap."
            ),
            type = ExerciseType.PLANK
        )
    )

    fun getExercisesByCategory(category: String): List<Exercise> {
        return exercises.filter { it.category.equals(category, ignoreCase = true) }
    }

    fun getExerciseById(id: String): Exercise? {
        return exercises.find { it.id == id }
    }
}
