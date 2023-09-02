package com.github.motoshige021.hiltdiprac.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.motoshige021.hiltdiprac.Global
import com.github.motoshige021.hiltdiprac.R
import com.github.motoshige021.hiltdiprac.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetailFragment: Fragment() {
    private lateinit var binding: FragmentDetailBinding

    private val args: DetailFragmentArgs by navArgs()
    private var isDeleteing = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.buttonSecond.setOnClickListener {
            backMainFragment()
        }
        binding.viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        binding.viewModel!!.deleteResult.observe(viewLifecycleOwner) {
            if (isDeleteing) { backMainFragment() }
        }

        setUpMainMenu()
        setUpProgramInfo()
    }

    private fun setUpMainMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.detail_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                var isRet = false;
                when (menuItem.itemId) {
                    R.id.menu_delete -> {
                        Log.d(Global.TAG, "Delete menu:" + args.programId)
//                        binding.viewModel!!.deleteResult.observe(viewLifecycleOwner) {
//                            backMainFragment()
//                        }
                        isDeleteing = true
                        binding.viewModel!!.deleteProgram(args.programId)
                        isRet = true
                    }
                }
                return isRet
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    fun setUpProgramInfo() {
        var viewModel = binding.viewModel ?: return
        viewModel.program.observe(viewLifecycleOwner) { tvProgram ->
            binding.titleTextView.text = tvProgram.title
            binding.detailTextView.text = tvProgram.description
            binding.complateCheckbox.isChecked = tvProgram.isCompleted
        }
        viewModel.getProgram(args.programId)
    }

    fun backMainFragment()  {
        var aciton = DetailFragmentDirections.actionDetailFragmentToMainFragment()
        findNavController().navigate(aciton)
    }
}