package com.github.motoshige021.hiltdiprac.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.motoshige021.hiltdiprac.Global
import com.github.motoshige021.hiltdiprac.R
import com.github.motoshige021.hiltdiprac.databinding.FragmentDetailBinding

class DetailFragment: Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding

    private val args: DetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.buttonSecond.setOnClickListener {
            var aciton = DetailFragmentDirections.actionDetailFragmentToMainFragment()
            findNavController().navigate(aciton)
        }
        binding!!.deugTextView.text = args.programId
        setUpMainMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                        isRet = true
                    }
                }
                return isRet
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}