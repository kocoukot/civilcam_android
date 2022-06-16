package com.civilcam.ui.langSelect.content

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.civilcam.common.theme.CCTheme
import com.civilcam.domain.model.LanguageType
import com.civilcam.utils.LocaleHelper
import timber.log.Timber


private const val NO_SEGMENT_INDEX = -1
private val PRESSED_TRACK_PADDING = 1.dp
private val THUMB_OFFSET = 20

/** Alpha to use to indicate pressed state when unselected segments are pressed. */
private const val PRESSED_UNSELECTED_ALPHA = .6f
private val BACKGROUND_SHAPE = RoundedCornerShape(4.dp)


@Composable
fun SegmentedItem(currentLang: LanguageType, selectedLang: (LanguageType) -> Unit) {
    val context = LocalContext.current
    MaterialTheme {
        Surface {
            Column(Modifier.padding(16.dp), verticalArrangement = spacedBy(16.dp)) {
                val twoSegments = remember { listOf(LanguageType.ENGLISH, LanguageType.SPAIN) }
                var selectedTwoSegment by remember { mutableStateOf(currentLang) }
                SegmentedControl(
                    twoSegments,
                    selectedTwoSegment,
                    onSegmentSelected = {
                        Timber.d("selected lang $it")

//                        val configuration = LocalConfiguration.current
//                        configuration.setLocale(locale)
//                        val resources = LocalContext.current.resources
//                        resources.updateConfiguration(configuration, resources.displayMetrics)

                        LocaleHelper.setLocale(context, it.langValue)
                        selectedTwoSegment = it
                        selectedLang.invoke(it)
                    }
                ) {

                    SegmentText(it, it == selectedTwoSegment)
                }
            }
        }
    }
}


@Composable
fun <T : Any> SegmentedControl(
    segments: List<T>,
    selectedSegment: T,
    onSegmentSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    val state = remember { SegmentedControlState() }
    state.segmentCount = segments.size
    state.selectedSegment = segments.indexOf(selectedSegment)
    state.onSegmentSelected = { onSegmentSelected(segments[it]) }

    // Animate between whole-number indices so we don't need to do pixel calculations.
    val selectedIndexOffset by animateFloatAsState(state.selectedSegment.toFloat())

    // Use a custom layout so that we can measure the thumb using the height of the segments. The thumb
    // is whole composable that draws itself – this layout is just responsible for placing it under
    // the correct segment.
    Layout(
        content = {
            Thumb(state)
            Segments(state, segments, content)
        },
        modifier = modifier
            .fillMaxWidth()
            .then(state.inputModifier)
            .background(CCTheme.colors.lightGray, BACKGROUND_SHAPE)
            .padding(vertical = 0.dp)
    ) { measurables, constraints ->
        val (thumbMeasurable, segmentsMeasurable) = measurables

        val segmentsPlaceable = segmentsMeasurable.measure(constraints)
        state.updatePressedScale(segmentsPlaceable.height, this)

        val thumbPlaceable = thumbMeasurable.measure(
            Constraints.fixed(
                width = segmentsPlaceable.width / segments.size,
                height = segmentsPlaceable.height + THUMB_OFFSET
            )
        )

        layout(segmentsPlaceable.width, segmentsPlaceable.height) {
            val segmentWidth = segmentsPlaceable.width / segments.size

            // Place the thumb first since it should be drawn below the segments.
            thumbPlaceable.placeRelative(
                x = (selectedIndexOffset * segmentWidth).toInt(),
                y = (-THUMB_OFFSET / 2)
            )
            segmentsPlaceable.placeRelative(IntOffset.Zero)
        }
    }
}

@Composable
fun SegmentText(text: LanguageType, isSelected: Boolean) {
    Text(
        text.langTitle, maxLines = 1,
        color = if (isSelected) CCTheme.colors.black else CCTheme.colors.grayText,
        overflow = Ellipsis,
        modifier = Modifier.padding(vertical = 18.dp)
    )
}

@Composable
private fun Thumb(state: SegmentedControlState) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(
                state.segmentScaleModifier(
                    pressed = state.pressedSegment == state.selectedSegment,
                    segment = state.selectedSegment
                )
            )
            .shadow(4.dp, BACKGROUND_SHAPE)
            .background(CCTheme.colors.white, BACKGROUND_SHAPE)
    ) {}

}

@Composable
private fun <T> Segments(
    state: SegmentedControlState,
    segments: List<T>,
    content: @Composable (T) -> Unit
) {
    CompositionLocalProvider(
        LocalTextStyle provides TextStyle(fontWeight = FontWeight.Medium)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
        ) {
            segments.forEachIndexed { i, segment ->
                val isSelected = i == state.selectedSegment
                val isPressed = i == state.pressedSegment

                // Unselected presses are represented by fading.
                val alpha by animateFloatAsState(if (!isSelected && isPressed) PRESSED_UNSELECTED_ALPHA else 1f)

                // We can't use Modifier.selectable because it does way too much: it does its own input
                // handling and wires into Compose's indicaiton/interaction system, which we don't want because
                // we've got our own indication mechanism.
                val semanticsModifier = Modifier.semantics(mergeDescendants = true) {
                    selected = isSelected
                    role = Role.Button
                    onClick { state.onSegmentSelected(i); true }
                    stateDescription = if (isSelected) "Selected" else "Not selected"
                }

                Box(
                    Modifier
                        .weight(1f)
                        .then(semanticsModifier)
                        .alpha(alpha)
                        .then(state.segmentScaleModifier(isPressed && isSelected, i))
                        // Center the segment content.
                        .wrapContentWidth()
                ) {
                    content(segment)
                }
            }
        }
    }
}

class SegmentedControlState {
    var segmentCount by mutableStateOf(0)
    var selectedSegment by mutableStateOf(0)
    var onSegmentSelected: (Int) -> Unit by mutableStateOf({})
    var pressedSegment by mutableStateOf(NO_SEGMENT_INDEX)

    /**
     * Scale factor that should be used to scale pressed segments (both the segment itself and the
     * thumb). When this scale is applied, exactly [PRESSED_TRACK_PADDING] will be added around the
     * element's usual size.
     */
    var pressedSelectedScale by mutableStateOf(1f)
        private set

    /**
     * Calculates the scale factor we need to use for pressed segments to get the desired padding.
     */
    fun updatePressedScale(controlHeight: Int, density: Density) {
        with(density) {
            val pressedPadding = PRESSED_TRACK_PADDING * 2
            val pressedHeight = controlHeight - pressedPadding.toPx()
            pressedSelectedScale = pressedHeight / controlHeight
        }
    }

    /**
     * Returns a [Modifier] that will scale an element so that it gets [PRESSED_TRACK_PADDING] extra
     * padding around it. The scale will be animated.
     *
     * The scale is also performed around either the left or right edge of the element if the [segment]
     * is the first or last segment, respectively. In those cases, the scale will also be translated so
     * that [PRESSED_TRACK_PADDING] will be added on the left or right edge.
     */
    @SuppressLint("ModifierFactoryExtensionFunction")
    fun segmentScaleModifier(
        pressed: Boolean,
        segment: Int,
    ): Modifier = Modifier.composed {
        val scale by animateFloatAsState(if (pressed) pressedSelectedScale else 1f)
        val xOffset by animateDpAsState(if (pressed) PRESSED_TRACK_PADDING else 0.dp)

        graphicsLayer {
            this.scaleX = scale
            this.scaleY = scale

            // Scales on the ends should gravitate to that edge.
            this.transformOrigin = TransformOrigin(
                pivotFractionX = when (segment) {
                    0 -> 0f
                    segmentCount - 1 -> 1f
                    else -> .5f
                },
                pivotFractionY = .5f
            )

            // But should still move inwards to keep the pressed padding consistent with top and bottom.
            this.translationX = when (segment) {
                0 -> xOffset.toPx()
                segmentCount - 1 -> -xOffset.toPx()
                else -> 0f
            }
        }
    }

    /**
     * A [Modifier] that will listen for touch gestures and update the selected and pressed properties
     * of this state appropriately.
     *
     * Input will be reset if the [segmentCount] changes.
     */
    val inputModifier = Modifier.pointerInput(segmentCount) {
        val segmentWidth = size.width / segmentCount

        // Helper to calculate which segment an event occured in.
        fun segmentIndex(change: PointerInputChange): Int =
            ((change.position.x / size.width.toFloat()) * segmentCount)
                .toInt()
                .coerceIn(0, segmentCount - 1)

        forEachGesture {
            awaitPointerEventScope {
                val down = awaitFirstDown()

                pressedSegment = segmentIndex(down)
                val downOnSelected = pressedSegment == selectedSegment
                val segmentBounds = Rect(
                    left = pressedSegment * segmentWidth.toFloat(),
                    right = (pressedSegment + 1) * segmentWidth.toFloat(),
                    top = 0f,
                    bottom = size.height.toFloat()
                )

                // Now that the pointer is down, the rest of the gesture depends on whether the segment that
                // was "pressed" was selected.
                if (downOnSelected) {
                    // When the selected segment is pressed, it can be dragged to other segments to animate the
                    // thumb moving and the segments scaling.
                    horizontalDrag(down.id) { change ->
                        pressedSegment = segmentIndex(change)

                        // Notify the SegmentedControl caller when the pointer changes segments.
                        if (pressedSegment != selectedSegment) {
                            onSegmentSelected(pressedSegment)
                        }
                    }
                } else {
                    // When an unselected segment is pressed, we just animate the alpha of the segment while
                    // the pointer is down. No dragging is supported.
                    waitForUpOrCancellation(inBounds = segmentBounds)
                        // Null means the gesture was cancelled (e.g. dragged out of bounds).
                        ?.let { onSegmentSelected(pressedSegment) }
                }

                // In either case, once the gesture is cancelled, stop showing the pressed indication.
                pressedSegment = NO_SEGMENT_INDEX
            }
        }
    }
}

/**
 * Copy of nullary waitForUpOrCancellation that works with bounds that may not be at 0,0.
 */
private suspend fun AwaitPointerEventScope.waitForUpOrCancellation(inBounds: Rect): PointerInputChange? {
    while (true) {
        val event = awaitPointerEvent(PointerEventPass.Main)
        if (event.changes.all { it.changedToUp() }) {
            // All pointers are up
            return event.changes[0]
        }

        if (event.changes.any { it.consumed.downChange || !inBounds.contains(it.position) }) {
            return null // Canceled
        }

        // Check for cancel by position consumption. We can look on the Final pass of the
        // existing pointer event because it comes after the Main pass we checked above.
        val consumeCheck = awaitPointerEvent(PointerEventPass.Final)
        if (consumeCheck.changes.any { it.positionChangeConsumed() }) {
            return null
        }
    }
}