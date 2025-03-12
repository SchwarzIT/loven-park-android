package com.schwarzit.lovenpark.profile.signals.view.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schwarzit.lovenpark.R
import com.schwarzit.lovenpark.databinding.CardSignalsBinding
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalModel
import com.schwarzit.lovenpark.profile.signals.data.domainmodel.SignalCategory

class UserProfileSignalAdapter(var onClick: SignalClickListener) :
    RecyclerView.Adapter<UserProfileSignalAdapter.UserProfileSignalViewHolder>() {
    var signalList = listOf<SignalModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileSignalViewHolder {
        val binding = CardSignalsBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return UserProfileSignalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserProfileSignalViewHolder, position: Int) {
        val currentSignal = signalList[position]
        holder.bindItem(currentSignal)
    }

    override fun getItemCount() = signalList.size

    fun injectList(signalList: List<SignalModel>) {
        this.signalList = signalList
        notifyDataSetChanged()
    }


    inner class UserProfileSignalViewHolder(private val signalsBinding: CardSignalsBinding) :
        RecyclerView.ViewHolder(signalsBinding.root) {
        fun bindItem(signal: SignalModel) {
            signalsBinding.apply {
                val res = itemView.resources

                val categoryText = when (signal.category) {
                    SignalCategory.UNCOLLECTED_WASTE -> res.getString(R.string.signal_category_uncollected_waste)
                    SignalCategory.INJURED_TREE -> res.getString(R.string.signal_category_injured_tree)
                    SignalCategory.ANIMAL_CARCASSES_FOUND -> res.getString(R.string.signal_category_animal_carcasses_found)
                    SignalCategory.DAMAGED_PARK_FURNITURE -> res.getString(R.string.signal_category_damaged_park_furniture)
                    SignalCategory.OTHER -> res.getString(R.string.signal_category_other)
                }

                tvSignalNumber.text = res.getString(R.string.signal_unique_number, signal.number)
                tvSignalCategory.text = categoryText
                tvSignalDate.text = signal.createdAt
            }
            signalsBinding.ivSignalShare.setOnClickListener {
                //to be discussed
            }

            itemView.setOnClickListener {
                onClick.onSignalClick(signal.id)
            }
        }
    }

}