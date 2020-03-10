package com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import com.bishwajeet.githubrepos.GitHubRepoApp
import com.bishwajeet.githubrepos.R
import com.bishwajeet.githubrepos.model.GitHubRepository
import com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList.helper.RepoClickListener
import com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList.helper.RepositoryListAdapter
import kotlinx.android.synthetic.main.fragment_repository_list.view.*
import javax.inject.Inject

class RepositoryListFragment : Fragment(),
    RepoClickListener {

    private val _tag = "RepoListFragment"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<RepositoryListViewModel> { viewModelFactory }
    private val adapter =
        RepositoryListAdapter(
            this as RepoClickListener
        )
    private lateinit var binding: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflater.inflate(R.layout.fragment_repository_list, container, false)
        initAdapter()
        return binding
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as GitHubRepoApp).applicationComponent.repositoryListComponent()
            .create()
            .inject(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finishAfterTransition()
        }
    }


    private fun initAdapter() {
        binding.rvRepository.adapter = adapter
        viewModel._data.observe(viewLifecycleOwner, Observer<PagedList<GitHubRepository>> {
            Log.i(_tag, "Updating list with ${it.size} items")
            adapter.submitList(it)
        })

        viewModel._eventNetworkError.observe(viewLifecycleOwner, Observer<String> {
            Log.i(_tag, it)
        })

        viewModel._count.observe(viewLifecycleOwner, Observer<Int> {
            Log.e(_tag, "Total: $it items")
        })
    }


    override fun onRepoClick(repo: GitHubRepository) {
        val nav =
            RepositoryListFragmentDirections.actionRepositoryListFragmentToRepositoryDetailFragment(
                repo
            )
        findNavController().navigate(nav)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_logout -> {
            activity?.finishAfterTransition()
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_actionbar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
