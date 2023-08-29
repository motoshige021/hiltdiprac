package com.github.motoshige021.hiltdiprac.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.github.motoshige021.hiltdiprac.*
import com.github.motoshige021.hiltdiprac.databinding.FragmentMainBinding

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding

    private lateinit var listAdapter: TvProgramAdapter

    companion object {
        fun newInstance() = MainFragment()
    }

    //private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // viewModel.loadData(taskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        setupListAdapter()
        setupMainMenu()
        setupSowProgram()

        /*
        binding.viewModel?.apply {
            this.loadData(taskId)
            binding.message!!.text = this.getData(taskId)
        }
        */
        //binding.message.text = binding.viewModel.getData(taskId)

        /*
        var messageText = view.findViewById<TextView>(R.id.message)
        messageText ?.apply {
            this.text = viewModel.getData(taskId)
        }
        */
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewModel
        viewModel?.let { viewModel ->
            listAdapter = TvProgramAdapter(viewModel)
            binding.tvprogramList.adapter =  listAdapter
        }
    }

    private fun setupMainMenu() {
        val menuHos: MenuHost = requireActivity()
        menuHos.addMenuProvider(object: MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_version -> {
                        Log.d(Global.TAG, "menu version")
                    }
                    R.id.menu_filtering -> {
                        showFilteringMenu()
                    }
                    /* --Start Debug Menu
                    R.id.menu_debug_toDetail -> {
                        var action = MainFragmentDirections.actionMainFragmentToDetailFragment("navDiretArgDebug")
                        findNavController().navigate(action)
                        //findNavController().navigate(R.id.action_MainFragment_to_DetailFragment,
                        //     Bundle().apply { putString(Global.ARG_PROGRAMID, "debugTestId") })
                    }
                    --End Debug Menu */
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showFilteringMenu() {
        Log.d(Global.TAG, "showFilteringMenu()")
        val rootMenu = activity?.findViewById<View>(R.id.menu_filtering)?: return
        PopupMenu(requireContext(), rootMenu).run {
            menuInflater.inflate(R.menu.main_fragment_filter_menu, menu)
            Log.d(Global.TAG, "showFilteringMenu: inflate(main_fragment_filter_menu)")
            setOnMenuItemClickListener { menuItem ->
                var filter = TaskRepository.PROGRAM_TYPE.ALL
                when (menuItem.itemId) {
                    R.id.menu_filter_all -> filter = TaskRepository.PROGRAM_TYPE.ALL
                    R.id.menu_filter_active -> filter = TaskRepository.PROGRAM_TYPE.ACTIVE
                    R.id.menu_filter_completed -> filter = TaskRepository.PROGRAM_TYPE.COMPLETED
                    else -> filter = TaskRepository.PROGRAM_TYPE.ALL
                }
                binding.viewModel!!.setFiltering(filter)
                true
            }
            show()
        }
    }

    private fun setupSowProgram() {
        //binding.viewModel!!.showProgramId.observe(viewLifecycleOwner) { id ->
        binding.viewModel!!.tvProgram.observe(viewLifecycleOwner) { tvProgram ->
            //val tvProgram = binding.viewModel!!.getProgram(id)
            tvProgram?.let {
                if (!Global.DEBUG_PROGRAM_DIALOG) {
                    if (binding.viewModel!!.getProgramId == it.id) {
                        Log.d(Global.TAG, "setupSowProgram(): id=" + it.id)
                        binding.viewModel!!.getProgramId = ""
                        var action = MainFragmentDirections.actionMainFragmentToDetailFragment(it.id)
                        findNavController().navigate(action)
                    }
                } else {
                    if (binding.viewModel!!.getProgramId == it.id) {
                        Log.d(Global.TAG, "setupSowProgram(): id=" + it.id)
                        AlertDialog.Builder(requireActivity())
                            .setTitle(it.title)
                            .setMessage(it.description)
                            .setPositiveButton(context!!.getString(R.string.dialog_button_ok),
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        Log.d(Global.TAG, "Click OK Button on Program Dialog")
                                    }
                                })
                            .show()
                    }
                }
            }
        }
    }
}