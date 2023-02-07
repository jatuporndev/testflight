package com.ntbx.android.test.flight.fragment.envList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.ntbx.android.test.flight.R
import com.ntbx.android.test.flight.databinding.FragmentEnvListBinding
import com.ntbx.android.test.flight.fragment.appList.AppListFragment


class EnvListFragment : Fragment() {
    private lateinit var binding: FragmentEnvListBinding
    lateinit var envListAdapter: EnvListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnvListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        envListAdapter = EnvListAdapter()
        val envName = listOf("gamma", "dev", "uat")
        binding.recycleview.adapter = envListAdapter
        binding.recycleview.layoutManager = GridLayoutManager(requireContext(), 1)
        envListAdapter.submitList(envName)

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            AppListFragment.appListFragment.cancelAll()
        }catch (e : Exception){}

    }
}