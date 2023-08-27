package com.github.motoshige021.hiltdiprac.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}