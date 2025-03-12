package com.schwarzit.lovenpark.signal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.schwarzit.lovenpark.databinding.FragmentSignalCategoryBinding

class SignalCategoryFragment : Fragment() {

    private var binding: FragmentSignalCategoryBinding? = null
    var categorySelectedListener: ((category: String) -> Unit)? = null
    var signalCatAdapter: SignalCategoryAdapter? = null
    private var selectedCategory: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignalCategoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerForSignalDataFromSignalRootFragment()
        initSignalCategoryRecyclerView()
        selectedCategory.let { signalCatAdapter?.setCategory(it) }
    }

    private fun registerForSignalDataFromSignalRootFragment() {
        val category = arguments?.getString(CATEGORY) ?: return
        selectedCategory = category
    }

    private fun initSignalCategoryRecyclerView() {
        val recyclerView = binding?.rvSignalCategorie
        recyclerView?.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        signalCatAdapter = SignalCategoryAdapter(requireContext())
        signalCatAdapter.let {
            it?.setCategoryList(SignalCategoriesHolder.getSignalCategories(requireContext()))
            it?.setCategory(selectedCategory)
            it?.categorySelectedListener = { category: String ->
                selectedCategory = category
                signalCatAdapter?.setCategory(selectedCategory)
                categorySelectedListener?.invoke(category)
            }
        }
        recyclerView?.adapter = signalCatAdapter
    }
}