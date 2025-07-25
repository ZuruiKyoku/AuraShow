import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun GridWithScrollbar(
    state: LazyGridState,
    content: @Composable () -> Unit
) {
    var containerHeightPx by remember { mutableStateOf(1) } // prevent divide-by-zero
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .onSizeChanged { containerHeightPx = it.height }
    ) {
        content()

        val showScrollbar by remember {
            derivedStateOf {
                state.layoutInfo.totalItemsCount > state.layoutInfo.visibleItemsInfo.size
            }
        }

        if (showScrollbar && containerHeightPx > 0) {
            val scrollProgress by remember {
                derivedStateOf {
                    val firstVisible = state.firstVisibleItemIndex
                    val totalItems = state.layoutInfo.totalItemsCount
                    val visibleItems = state.layoutInfo.visibleItemsInfo.size

                    val scrollRange = totalItems - visibleItems
                    if (scrollRange <= 0) 0f else firstVisible / scrollRange.toFloat()
                }
            }

            val thumbHeightFraction = remember {
                derivedStateOf {
                    val totalItems = state.layoutInfo.totalItemsCount.toFloat()
                    val visibleItems = state.layoutInfo.visibleItemsInfo.size.toFloat().coerceAtLeast(1f)
                    (visibleItems / totalItems).coerceIn(0.05f, 1f)
                }
            }.value

            val thumbHeightPx = (containerHeightPx * thumbHeightFraction).toInt()
            val maxOffsetPx = containerHeightPx - thumbHeightPx
            val offsetPx = (scrollProgress * maxOffsetPx).toInt()

            // Track
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .align(Alignment.CenterEnd)
                    .background(Color.Gray.copy(alpha = 0.3f))
            )

            // Thumb
            Box(
                modifier = Modifier
                    .height((thumbHeightPx / density.density).dp)
                    .width(4.dp)
                    .align(Alignment.TopEnd)
                    .offset { IntOffset(0, offsetPx) }
                    .background(Color.White, shape = RoundedCornerShape(2.dp))
            )
        }
    }
}
