
import androidx.appcompat.widget.SearchView
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebouncingQueryTextListener(
    private val coroutineScope: CoroutineScope,
    private val onDebouncingQueryTextChange: (String?) -> Unit
) : SearchView.OnQueryTextListener {
    private val debouncePeriod = 500L
    private var searchJob: Job? = null

    override fun onQueryTextSubmit(query: String?) = true

    override fun onQueryTextChange(newText: String?): Boolean {
        searchJob?.cancel()
        searchJob = coroutineScope.launch {
            newText?.let {
                delay(debouncePeriod)
                onDebouncingQueryTextChange(newText)
            }
        }

        return true
    }
}