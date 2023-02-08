package com.ntbx.android.test.flight.fragment.envList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.ntbx.android.test.flight.R
import com.ntbx.android.test.flight.databinding.FragmentEnvListBinding
import com.ntbx.android.test.flight.fragment.appList.AppListFragment
import com.ntbx.android.test.flight.fragment.appList.AppListFragmentArgs
import com.ntbx.android.test.flight.models.Key


class EnvListFragment : Fragment() {
    private lateinit var binding: FragmentEnvListBinding
    lateinit var envListAdapter: EnvListAdapter
    private val args: EnvListFragmentArgs by navArgs()

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
        binding.txtTitle.text = args.appName
        binding.recycleview.adapter = envListAdapter
        binding.recycleview.layoutManager = GridLayoutManager(requireContext(), 1)
        setDataEnv()

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    fun setDataEnv() {
        val data: MutableList<Key> = mutableListOf()
        val envName = listOf("gamma", "dev", "uat")
        envName.forEach { env ->
            data.add(Key(appName = args.appName, env = env))
        }
        envListAdapter.submitList(data)
    }

    override fun onResume() {
        super.onResume()
        try {
            AppListFragment.appListFragment.cancelAll()
        }catch (e : Exception){}

    }
}