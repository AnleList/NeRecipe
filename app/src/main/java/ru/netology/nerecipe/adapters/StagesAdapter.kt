package ru.netology.nerecipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nerecipe.data.Recipe
import ru.netology.nerecipe.data.Stage
import ru.netology.nerecipe.databinding.CardViewingLayoutBinding
import ru.netology.nerecipe.databinding.RecipeCardLayoutBinding

internal class StagesAdapter(
    private val interactionListener: RecipeInteractionListener
    ) : ListAdapter<Stage, StagesAdapter.ViewHolder>(DiffCallback)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CardViewingLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder (
        private val binding: CardViewingLayoutBinding,
        listener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {


        private lateinit var stage: Stage

        init {
            binding.stageImage.setOnLongClickListener {
                TODO("Not yet implemented")
                return@setOnLongClickListener false
            }
            binding.stageText.setOnLongClickListener {
                TODO("Not yet implemented")
                return@setOnLongClickListener false
            }
        }
        fun bind(stage: Stage) {
            this.stage = stage
            binding.stageText.text = stage.text
            Glide.with(binding.stageImage)
                .load(stage.imageURL)
                .into(binding.stageImage)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Stage>() {

        override fun areItemsTheSame(oldItem: Stage, newItem: Stage) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Stage, newItem: Stage) =
            oldItem == newItem
    }
}