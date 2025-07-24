package com.slygames.aurashow.model

enum class TransitionType(val displayName: String) {
    Crossfade("Crossfade"),
    SlideLeft("Slide Left"),
    SlideRight("Slide Right"),
    SlideUp("Slide Up"),
    SlideDown("Slide Down"),
    ZoomIn("Zoom In"),
    ZoomOut("Zoom Out"),
    RotateIn("Rotate In"),
    RotateOut("Rotate Out"),
    FadeToBlack("Fade to Black"),
    WipeHorizontal("Wipe Horizontal"),
    WipeVertical("Wipe Vertical")
}