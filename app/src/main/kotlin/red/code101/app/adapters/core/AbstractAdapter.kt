package red.code101.app.adapters.core

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class AbstractAdapter<T, B : ViewBinding>(val item: List<T>) :
    RecyclerView.Adapter<AbstractAdapter<T, B>.Holder>() {
    override fun getItemCount() = item.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(onCreteHolder(inflater))
    }

    abstract fun onCreteHolder(i: LayoutInflater): B

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(item[position])
    }

    abstract fun B.bind(item: T)

    inner class Holder(private val binding: B) :
        AbstractHolderRecycler<T>(binding) {
        override fun bind(item: T) {
            binding.bind(item)
        }
    }

    abstract inner class AbstractHolderRecycler<T>(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: T)
    }
}

abstract class AbstractAdapterPosition<T, B : ViewBinding>(val item: List<T>) :
    RecyclerView.Adapter<AbstractAdapterPosition<T, B>.Holder>() {
    override fun getItemCount() = item.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(onCreteHolder(inflater))
    }

    abstract fun onCreteHolder(i: LayoutInflater): B

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(item[position], position)
    }

    abstract fun B.bind(item: T, position: Int)

    inner class Holder(private val binding: B) :
        AbstractHolderRecycler<T>(binding) {
        override fun bind(item: T, position: Int) {
            binding.bind(item, position)
        }
    }

    abstract inner class AbstractHolderRecycler<T>(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(item: T, position: Int)
    }
}