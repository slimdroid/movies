import androidx.compose.ui.window.ComposeUIViewController
import com.slimdroid.movies.MovieApp
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { MovieApp() }
