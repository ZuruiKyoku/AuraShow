import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun GridWithScrollbar(
    state: LazyGridState,
    containerHeightDp: Float = 200f,
    content: @Composable () -> Unit
) {
    Box {
        content()

        val showScrollbar by remember {
            derivedStateOf {
                state.layoutInfo.totalItemsCount > state.layoutInfo.visibleItemsInfo.size
            }
        }

        if (showScrollbar) {
            val scrollProgress by remember {
                derivedStateOf {
                    val firstVisible = state.firstVisibleItemIndex
                    val totalItems = state.layoutInfo.totalItemsCount
                    firstVisible / (totalItems.toFloat() - 1).coerceAtLeast(1f)
                }
            }

            val density = LocalDensity.current // ✅ Valid usage here

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
                    .fillMaxHeight(fraction = 0.2f)
                    .width(4.dp)
                    .align(Alignment.TopEnd)
                    .offset {
                        val offsetPx = (scrollProgress * containerHeightDp * density.density).toInt()
                        IntOffset(0, offsetPx)
                    }
                    .background(Color.White, shape = RoundedCornerShape(2.dp))
            )
        }
    }
}