package com.andruid.magic.dailytasks.ui.activity

import DebouncingQueryTextListener
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.andruid.magic.dailytasks.R
import com.andruid.magic.dailytasks.databinding.ActivityCompletedTasksBinding
import com.andruid.magic.dailytasks.ui.adapter.CompletedTaskAdapter
import com.andruid.magic.dailytasks.ui.viewbinding.viewBinding
import com.andruid.magic.dailytasks.ui.viewmodel.SearchViewModel
import com.andruid.magic.dailytasks.util.color

class CompletedTasksActivity : ContainerTransformActivity("completed_tasks_transition") {
    private val binding by viewBinding(ActivityCompletedTasksBinding::inflate)
    private val taskAdapter = CompletedTaskAdapter()
    private val searchViewModel by viewModels<SearchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initCollapsingActionBar(binding.toolBar, color(R.color.purple_heart))
        initRecyclerView()
        initSearchView()

        searchViewModel.taskSearchResults.observe(this) {
            taskAdapter.submitData(lifecycle, it)
        }
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(DebouncingQueryTextListener(lifecycleScope) { query ->
            searchViewModel.updateQuery(query ?: "")
        })
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = taskAdapter
    }
}