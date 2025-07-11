package com.example.stayfit.data

import com.example.stayfit.R

object WorkoutData {
    val workoutsByCategory: Map<String, List<Exercise>> = mapOf(
        "Classic" to listOf(
            Exercise("Jumping Jacks", 50, R.drawable.jumping_jack, "Start with your feet together and your arms by your sides, then jump up with your feet apart and your hands overhead. Return to the start position then do the next rep. This exercise provides a full-body workout and works all your large muscle groups."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Wall Sit", 50, R.drawable.wall_sit, "Start with your back against the wall, then slide down until your knees are at a 90 degree angle. Keep your back against the wall with your hands and arms away from your legs. Hold the position."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Push-ups", 50, R.drawable.push_up, "Place your hands on the floor, shoulder-width apart. Lower your body until your chest nearly touches the floor, then push yourself back up. Keep your body straight throughout."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Abdominal Crunches", 50, R.drawable.abdominal_crunches, "Lie on your back with your knees bent and feet flat on the floor. Place your hands behind your head, then lift your shoulders off the floor using your abdominal muscles. Lower back down and repeat."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Step-up onto Chair", 50, R.drawable.stepup_onto_chair, "Stand in front of a chair. Step up with one leg, then bring the other leg up. Step down and repeat, alternating legs."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Squats", 50, R.drawable.squats, "Stand with your feet shoulder-width apart. Lower your hips back and down as if sitting in a chair, then return to standing. Keep your chest up and knees behind your toes."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Triceps Dip on Chair", 50, R.drawable.triceps_dip, "Sit on the edge of a chair, place your hands next to your hips. Slide your butt off the chair and bend your elbows to lower your body, then push back up."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Plank", 50, R.drawable.plank, "Place your forearms on the floor, elbows under shoulders. Keep your body straight from head to heels and hold the position."),
            Exercise("Rest", 10, 0, ""),
            Exercise("High Knees Running in Place", 50, R.drawable.high_knees_running, "Run in place, bringing your knees as high as possible with each step."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Lunges", 50, R.drawable.lunges, "Step forward with one leg and lower your hips until both knees are bent at about a 90-degree angle. Push back to the starting position and switch legs.")
        ),
        "Abs Workout" to listOf(
            Exercise("Jumping Squats", 50, R.drawable.jumping_squats, "Start in a squat position, then jump up explosively. Land softly and immediately lower back into a squat to complete one rep. This is a powerful plyometric exercise that strengthens your entire lower body and core."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Reverse Crunches", 50, R.drawable.reverse_crunches, "Lie on your back and lift your legs and hips towards the ceiling, bringing your knees towards your chest. Lower back down slowly. This targets your lower abs."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Straight-Arm Plank", 50, R.drawable.straight_arm_plank, "Hold a push-up position with your arms fully extended and your body in a straight line from head to heels. Engage your core and glutes."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Russian Twist", 50, R.drawable.russian_twist, "Sit on the floor with your knees bent and feet elevated. Twist your torso from side to side, touching your hands to the floor on each side. This exercise works your obliques."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Long Arm Crunches", 50, R.drawable.long_arm_crunches, "Lie on your back with your legs bent. Extend your arms straight out behind your head. Crunch up, lifting your shoulders off the floor while keeping your arms straight."),
            Exercise("Rest", 10, 0, ""),
            Exercise("One Leg Bridge", 50, R.drawable.one_leg_bridge, "Lie on your back with your knees bent. Extend one leg straight out. Lift your hips off the floor, then lower them back down. This isolates the glute and hamstring of the supporting leg."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Plank", 50, R.drawable.plank, "Place your forearms on the floor with your elbows under your shoulders. Keep your body in a straight line from head to heels and hold the position. It's a great exercise for core stability."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Cross Arm Crunches", 50, R.drawable.cross_arm_crunches, "Lie on your back with your knees bent. Cross your arms over your chest. Lift your shoulders towards the ceiling, using your abdominal muscles."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Mountain Climber", 50, R.drawable.mountain_climber, "Start in a plank position. Quickly drive your knees towards your chest one at a time, as if you are running in place. This is a full-body exercise that boosts your cardio and core strength."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Bicycle Crunches", 50, R.drawable.bicycle_crunches, "Lie on your back and bring your knees in towards your chest. Straighten one leg while twisting your upper body to bring the opposite elbow towards the bent knee. Alternate sides in a pedaling motion.")
        ),
        "Butt Workout" to listOf(
            Exercise("Glute Bridge", 50, R.drawable.glute_bridge, "Lie on your back with your knees bent and feet flat on the floor. Lift your hips toward the ceiling, squeezing your glutes at the top. Lower back down and repeat."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Donkey Kicks", 50, R.drawable.donkey_kicks, "Start on all fours. Keeping your knee bent, lift one leg up behind you until your thigh is parallel to the floor. Lower it back down and repeat. This isolates and strengthens the glutes."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Fire Hydrants", 50, R.drawable.fire_hydrants, "Start on all fours. Keeping your knee bent, lift one leg out to the side, away from your body. Lower it back down and repeat. This targets the outer glutes and hip abductors."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Side Lunges", 50, R.drawable.side_lunges, "Step out to one side and lower your hips, keeping your other leg straight. Push back to the starting position and repeat on the other side. This works your inner and outer thighs and glutes."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Sumo Squats", 50, R.drawable.sumo_squats, "Stand with your feet wider than shoulder-width apart and your toes pointed out. Lower your hips down into a squat. Return to the starting position. This emphasizes the inner thighs and glutes."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Walking Lunges", 50, R.drawable.walking_lunges, "Step forward into a lunge, then instead of pushing back, bring your back foot forward to step into the next lunge. This adds a cardiovascular element to the traditional lunge."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Squats", 50, R.drawable.squats, "Stand with your feet shoulder-width apart. Lower your hips back and down as if sitting in a chair, keeping your chest up and knees behind your toes. This is a fundamental exercise for lower body strength."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Clamshells", 50, R.drawable.clamshells, "Lie on your side with your knees bent and stacked. Keeping your feet together, lift your top knee up towards the ceiling. Lower it back down. This strengthens the hip abductors."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Hip Thrusts", 50, R.drawable.hip_thrusts, "Sit on the floor with your upper back against a bench and your knees bent. Drive your hips up towards the ceiling, squeezing your glutes at the top. Lower back down. This is one of the best exercises for glute development."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Step-Ups", 50, R.drawable.step_ups, "Stand in front of a sturdy box or bench. Step up with one foot, then the other. Step back down and repeat, alternating the leading leg. This builds single-leg strength and stability.")
        ),
        "Leg Workout" to listOf(
            Exercise("Squats", 50, R.drawable.squats, "Stand with your feet shoulder-width apart. Lower your hips back and down as if sitting in a chair. Keep your chest up and your knees from going past your toes. This is a foundational exercise for building lower body strength."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Lunges", 50, R.drawable.lunges, "Step forward with one leg and lower your hips until both knees are at a 90-degree angle. Push off your front foot to return to the start. This targets your quads, hamstrings, and glutes."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Calf Raises", 50, R.drawable.calf_raises, "Stand with your feet flat on the floor. Push through the balls of your feet to raise your heels up as high as you can. Lower back down slowly. This isolates and strengthens your calf muscles."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Pistol Squats", 50, R.drawable.pistol_squats, "Balance on one leg and extend the other straight out in front of you. Lower yourself down into a full squat on your supporting leg. This is an advanced move that builds incredible single-leg strength and balance."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Box Jumps", 50, R.drawable.box_jumps, "Stand in front of a sturdy box. Jump up onto the box, landing softly with both feet. Step back down. This plyometric exercise develops explosive power in your legs."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Wall Sit", 50, R.drawable.wall_sit, "Slide your back down a wall until your thighs are parallel to the floor. Hold this position. This builds isometric strength and endurance in your quads."),
            Exercise("Rest", 10, 0, ""),
            Exercise("High Knees", 50, R.drawable.high_knees, "Run in place, driving your knees up towards your chest as high as possible. This is a great warm-up and cardiovascular exercise."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Side Leg Raises", 50, R.drawable.side_leg_raises, "Lie on your side with your legs stacked. Lift your top leg up towards the ceiling without moving your torso. Lower it back down. This targets your hip abductors."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Curtsy Lunges", 50, R.drawable.curtsy_lunges, "Step your right leg behind your left, as if you were curtsying. Lower your hips until your left thigh is nearly parallel to the floor. Return to the start and switch sides. This works your glutes and inner thighs."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Jump Squats", 50, R.drawable.jump_squats, "Perform a regular squat, but as you come up, explode into a jump. Land softly and immediately go into your next squat. This builds explosive power and cardiovascular endurance.")
        ),
        "Arm Workout" to listOf(
            Exercise("Push-ups", 50, R.drawable.push_up, "Start in a high plank position. Lower your body until your chest is just above the floor, then push back up. This classic exercise works your chest, shoulders, and triceps."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Triceps Dips", 50, R.drawable.triceps_dip, "Use a chair or bench. Place your hands on the edge and slide your hips off. Lower your body by bending your elbows, then push back up. This isolates your triceps."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Plank to Up-Down", 50, R.drawable.plank_to_up_down, "Start in a forearm plank. One arm at a time, push up to a high plank position, then lower back down to your forearms. This builds core and shoulder stability."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Diamond Push-ups", 50, R.drawable.diamond_push_ups, "Perform a push-up with your hands close together, forming a diamond shape with your thumbs and index fingers. This variation places more emphasis on the triceps."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Pike Push-ups", 50, R.drawable.pike_push_ups, "Get into a downward dog position. Bend your elbows to lower your head towards the floor, then push back up. This targets your shoulders."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Inchworms", 50, R.drawable.inchworms, "From a standing position, walk your hands out to a plank position. Then, walk your feet in towards your hands. This is a great dynamic stretch and core strengthener."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Arm Circles", 50, R.drawable.arm_circles, "Stand with your arms extended out to your sides. Make small, controlled circles with your hands, then reverse the direction. This is a good warm-up for your shoulder joints."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Shoulder Taps", 50, R.drawable.shoulder_taps, "In a high plank position, tap one shoulder with the opposite hand, trying to keep your hips as still as possible. This builds core and shoulder stability."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Wall Push-ups", 50, R.drawable.wall_push_ups, "Stand facing a wall and place your hands on it. Perform a push-up motion against the wall. This is a great modification for building up to standard push-ups."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Plank Jacks", 50, R.drawable.plank_jacks, "Start in a high plank position. Jump your feet out to the sides and then back in, like a horizontal jumping jack. This adds a cardio element to the plank.")
        ),
        "Sleepy Time Stretch" to listOf(
            Exercise("Child's Pose", 50, R.drawable.childs_pose, "Kneel on the floor and sit back on your heels, then fold forward, resting your forehead on the floor. This is a gentle resting pose that stretches the back and hips."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Cat-Cow Stretch", 50, R.drawable.cat_cow_stretch, "On all fours, alternate between arching your back up towards the ceiling (cat) and dropping your belly down towards the floor (cow). This improves spinal flexibility."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Knees to Chest", 50, R.drawable.knees_to_chest, "Lie on your back and gently pull both knees into your chest. This stretches your lower back."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Supine Spinal Twist", 50, R.drawable.supine_spinal_twist, "Lie on your back and let your knees fall to one side while keeping your shoulders on the floor. This provides a deep twist for your spine and back."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Seated Forward Bend", 50, R.drawable.seated_forward_bend, "Sit with your legs extended in front of you and hinge at your hips to fold forward. This stretches your hamstrings and back."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Butterfly Stretch", 50, R.drawable.butterfly_stretch, "Sit with the soles of your feet together and let your knees fall out to the sides. This stretches your inner thighs, groin, and hips."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Neck Stretches", 50, R.drawable.neck_stretches, "Gently tilt your head from side to side and forward and back to release tension in your neck and shoulders."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Cobra Pose", 50, R.drawable.cobra_pose, "Lie on your stomach and gently push your chest up off the floor, keeping your hips down. This is a gentle backbend that opens up the chest."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Standing Hamstring Stretch", 50, R.drawable.standing_hamstring_stretch, "Stand with one foot forward and hinge at your hips to feel a stretch in the back of your front leg."),
            Exercise("Rest", 10, 0, ""),
            Exercise("Triceps Stretch", 50, R.drawable.triceps_stretch, "Reach one arm overhead and bend the elbow. Use your other hand to gently pull the elbow to deepen the stretch in your tricep.")
        )
    )
}