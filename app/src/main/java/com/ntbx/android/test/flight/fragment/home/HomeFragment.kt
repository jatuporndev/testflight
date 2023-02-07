package com.ntbx.android.test.flight.fragment.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ntbx.android.test.flight.databinding.FragmentHomeBinding
import com.ntbx.android.test.flight.fragment.appList.AppListFragment
import java.io.File

class HomeFragment : Fragment(), IHomeFragment {

    companion object {
        @JvmStatic
        lateinit var homeFragment: IHomeFragment
    }

    init {
        homeFragment = this
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeAdapter = HomeAdapter()
        val env = listOf("gamma", "dev", "uat")
        binding.recycleview.adapter = homeAdapter
        binding.recycleview.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.btnInfo.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToInfoFragment())
        }
        homeAdapter.submitList(env)
    }

    override fun goToAppList(env: String) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAppListFragment(env))
    }

    override fun onResume() {
        super.onResume()
        try {
            AppListFragment.appListFragment.cancelAll()
        }catch (e : Exception){}

    }
}