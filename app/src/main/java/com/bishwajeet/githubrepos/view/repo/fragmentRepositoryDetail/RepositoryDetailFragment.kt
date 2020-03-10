package com.bishwajeet.githubrepos.view.repo.fragmentRepositoryDetail


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bishwajeet.githubrepos.GitHubRepoApp
import com.bishwajeet.githubrepos.R
import com.bishwajeet.githubrepos.model.GitHubRepository
import com.bishwajeet.githubrepos.view.repo.fragmentRepositoryList.RepositoryDetailViewModel
import kotlinx.android.synthetic.main.fragment_repository_detail.view.*
import javax.inject.Inject

class RepositoryDetailFragment : Fragment() {

    private val _tag = "RepoDetailFragment"

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<RepositoryDetailViewModel> {
        viewModelFactory
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (requireActivity().application as GitHubRepoApp).applicationComponent.repositoryDetailComponent()
            .create()
            .inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = inflater.inflate(R.layout.fragment_repository_detail, container, false)
        setHasOptionsMenu(true)

        arguments?.getSerializable("repository")
            ?.let {
                Log.i(_tag, it.toString())
                viewModel.setRepository(it as GitHubRepository)
            }

        binding.repo_name.text = viewModel.getRepository().name
        binding.repo_description.text = viewModel.getRepository().description
        binding.repo_language.text = viewModel.getRepository().language

        return binding
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle the up button here
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}
