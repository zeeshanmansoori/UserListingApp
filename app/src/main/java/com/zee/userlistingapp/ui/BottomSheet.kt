package com.zee.userlistingapp.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.zee.userlistingapp.databinding.CustomBottomSheetBinding
import com.zee.userlistingapp.ui.home.HomeActivity

class BottomSheet(val listener:HomeActivity.FilterClickedListener,var conditionA:String="",val count:String=""): BottomSheetDialogFragment() {

    private lateinit var binding: CustomBottomSheetBinding
    private var repose = "repos:"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            if (count.isNotEmpty()){
                repoCount.setText(count)
                when(conditionA){
                    "" ->binding.equal.isChecked = true
                    ">" ->binding.greater.isChecked = true
                    "<" ->binding.lesser.isChecked = true
                }
            }

        }

        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            conditionA = when(checkedId){
                binding.equal.id -> ""
                binding.greater.id -> ">"
                binding.equal.id -> "<"
                else -> ""
            }
        }
        binding.saveBtn.setOnClickListener {
            val count = binding.repoCount.text.toString()
            if (count.isNotEmpty()) {
                listener.onFilterClosed(repose,conditionA,count)
                dismiss()
            }
            else {
                Toast.makeText(this.context, "have not applied filter", Toast.LENGTH_LONG).show()
            }


        }
    }



}