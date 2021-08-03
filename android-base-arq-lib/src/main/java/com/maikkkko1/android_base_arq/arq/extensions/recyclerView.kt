package com.maikkkko1.android_base_arq.arq.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.*

fun RecyclerView.createRecyclerStructure(
        // Orientation of the recycler view.
        layoutOrientation: Int = RecyclerView.VERTICAL
) {
    setHasFixedSize(true)
    layoutManager = LinearLayoutManager(context).apply {
        orientation = layoutOrientation
    }

    itemAnimator = DefaultItemAnimator()
}

fun <T> RecyclerView.createDefaultRecycler(
        // Layout resource used to create automatically the view.
        @LayoutRes resourceLayout: Int = 0,

        // List of items on the list.
        items: List<T> = listOf(),

        // Orientation of the recycler view.
        layoutOrientation: Int = RecyclerView.VERTICAL,

        // Delegate to create a new view.
        viewCreator: ((parent: ViewGroup) -> View)? = null,

        // Delegate to bind the item on  the view holder.
        prepareHolder: (holder: ViewHolder<T>) -> Unit
) {
    createRecyclerStructure(layoutOrientation = layoutOrientation)

    val data = mutableListOf<T>()
    data.addAll(items)

    adapter = object : BaseRecyclerAdapter<T, BaseRecyclerAdapterHolder<T>>(context, data) {
        override fun getItemLayoutResource() = resourceLayout
        override fun prepareViewHolder(view: View) = object : BaseRecyclerAdapterHolder<T>(view) {
            override fun attach(position: Int, item: T) {
                prepareHolder(
                        ViewHolder(view, item, position)
                )
            }
        }

        override fun createView(parent: ViewGroup) = viewCreator?.let { viewCreator(parent) }
                ?: super.createView(parent)
    }
}


fun <T> RecyclerView.refreshItems(newItems: List<T>, callback: DiffUtil.ItemCallback<T>) {
    val oldItems = (adapter as BaseRecyclerAdapter<T, *>).items
    val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
        override fun getOldListSize() = oldItems.size

        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(old: Int, new: Int) = callback.areItemsTheSame(
                oldItems[old],
                newItems[new]
        )

        override fun areContentsTheSame(old: Int, new: Int) = callback.areContentsTheSame(
                oldItems[old],
                newItems[new]
        )
    })

    oldItems.clear()
    oldItems.addAll(newItems)

    result.dispatchUpdatesTo(adapter!!)
}

data class ViewHolder<T>(val view: View, val item: T, val position: Int)

private abstract class BaseRecyclerAdapter<T, VH : BaseRecyclerAdapterHolder<T>>(
        val context: Context,
        val items: MutableList<T>
) : RecyclerView.Adapter<VH>() {

    @LayoutRes
    abstract fun getItemLayoutResource(): Int

    abstract fun prepareViewHolder(view: View): VH

    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: VH, position: Int) = holder.attach(position, items[position])

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = prepareViewHolder(createView(parent))

    open fun createView(parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(
                getItemLayoutResource(),
                parent,
                false
        )
    }
}

private abstract class BaseRecyclerAdapterHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun attach(position: Int, item: T)
}

private fun <T, VDB : ViewDataBinding> createRecyclerView(
        recyclerView: RecyclerView,
        layoutManager: RecyclerView.LayoutManager,
        cachedAdapter: BaseRecyclerAdapterBinding<T, *, *>? = null,
        items: List<T> = listOf(),
        // Prepare view binding
        viewBindingCreator: (parent: ViewGroup, inflater: LayoutInflater) -> VDB,

        // Delegate to bind the item on  the view holder.
        prepareHolder: (item: T, viewBinding: VDB, viewHolder: BaseRecyclerAdapterBindingHolder<T, VDB>) -> Unit,

        // Called when list is done
        onRecyclerDone: (adapter: BaseRecyclerAdapterBinding<T, VDB, BaseRecyclerAdapterBindingHolder<T, VDB>>) -> Unit = {}
) {
    val context = recyclerView.context

    recyclerView.setHasFixedSize(true)
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.layoutManager = layoutManager

    if (cachedAdapter != null) {
        recyclerView.adapter = cachedAdapter
        onRecyclerDone.invoke(cachedAdapter as BaseRecyclerAdapterBinding<T, VDB, BaseRecyclerAdapterBindingHolder<T, VDB>>)
    } else {
        val adapter = object :
                BaseRecyclerAdapterBinding<T, VDB, BaseRecyclerAdapterBindingHolder<T, VDB>>(
                        context,
                        items.toMutableList()
                ) {
            override fun prepareViewHolder(parent: ViewGroup) =
                    object : BaseRecyclerAdapterBindingHolder<T, VDB>(
                            viewBindingCreator.invoke(parent, LayoutInflater.from(context))
                    ) {
                        override fun attach(
                                position: Int,
                                item: T,
                                binding: VDB,
                                viewHolder: BaseRecyclerAdapterBindingHolder<T, VDB>
                        ) {
                            prepareHolder.invoke(item, binding, viewHolder)
                        }
                    }
        }

        recyclerView.adapter = adapter
        onRecyclerDone.invoke(adapter)
    }
}

/**
 * Encapsulate the creation of recycler views in order to reduce all the necessary boilerplate
 * when creating a list.
 */
fun <T, VDB : ViewDataBinding> RecyclerView.createBindingRecyclerView(
        cachedAdapter: BaseRecyclerAdapterBinding<T, *, *>? = null,
        items: List<T> = listOf(),
        layoutOrientation: Int = RecyclerView.VERTICAL,
        layoutManager: RecyclerView.LayoutManager? = null,
        viewBindingCreator: (parent: ViewGroup, inflater: LayoutInflater) -> VDB,
        prepareHolder: (item: T, viewBinding: VDB, viewHolder: BaseRecyclerAdapterBindingHolder<T, VDB>) -> Unit,
        onRecyclerDone: (adapter: BaseRecyclerAdapterBinding<T, VDB, BaseRecyclerAdapterBindingHolder<T, VDB>>) -> Unit = {}
) = createRecyclerView(
        recyclerView = this,
        cachedAdapter = cachedAdapter,
        items = items,
        layoutManager = layoutManager ?: LinearLayoutManager(context).apply {
            orientation = layoutOrientation
        },
        viewBindingCreator = viewBindingCreator,
        prepareHolder = prepareHolder,
        onRecyclerDone = onRecyclerDone
)

fun <T, VDB : ViewDataBinding> RecyclerView.createBindingRecyclerViewGridManager(
        cachedAdapter: BaseRecyclerAdapterBinding<T, *, *>? = null,
        items: List<T> = listOf(),
        countItemsPerLine: Int = 3,
        viewBindingCreator: (parent: ViewGroup, inflater: LayoutInflater) -> VDB,
        prepareHolder: (item: T, viewBinding: VDB, viewHolder: BaseRecyclerAdapterBindingHolder<T, VDB>) -> Unit,
        onRecyclerDone: (adapter: BaseRecyclerAdapterBinding<T, VDB, BaseRecyclerAdapterBindingHolder<T, VDB>>) -> Unit = {}
) = createRecyclerView(
        recyclerView = this,
        cachedAdapter = cachedAdapter,
        items = items,
        layoutManager = GridLayoutManager(context, countItemsPerLine),
        viewBindingCreator = viewBindingCreator,
        prepareHolder = prepareHolder,
        onRecyclerDone = onRecyclerDone
)

/**
 * Encapsulate the list update computation
 */
fun <T> RecyclerView.refreshBindingItems(
        newItems: List<T>,
        callback: DiffUtil.ItemCallback<T>
) {
    this.adapter?.let {
        val oldItems = (it as BaseRecyclerAdapterBinding<T, *, *>).allItems
        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun getOldListSize() = oldItems.size
            override fun getNewListSize() = newItems.size

            override fun areItemsTheSame(old: Int, new: Int) = callback.areItemsTheSame(
                    oldItems[old],
                    newItems[new]
            )

            override fun areContentsTheSame(old: Int, new: Int) = callback.areContentsTheSame(
                    oldItems[old],
                    newItems[new]
            )
        })
        result.dispatchUpdatesTo(it)
        it.updateItems(newItems)
    }
}


fun <T, VDB : ViewDataBinding> RecyclerView.attachSimpleDataBindingData(
        items: List<T> = listOf(),
        cachedAdapter: BaseRecyclerAdapterBinding<T, *, *>? = null,
        layoutOrientation: Int = RecyclerView.VERTICAL,
        viewBindingCreator: (parent: ViewGroup, inflater: LayoutInflater) -> VDB,
        prepareHolder: (item: T, viewBinding: VDB, viewHolder: BaseRecyclerAdapterBindingHolder<T, VDB>) -> Unit,
        onRecyclerDone: (adapter: BaseRecyclerAdapterBinding<T, VDB, BaseRecyclerAdapterBindingHolder<T, VDB>>) -> Unit = {},
        updateCallback: DiffUtil.ItemCallback<T>?
) {
    if (adapter == null) {
        createBindingRecyclerView(
                items = items,
                cachedAdapter = cachedAdapter,
                layoutOrientation = layoutOrientation,
                viewBindingCreator = viewBindingCreator,
                prepareHolder = prepareHolder,
                onRecyclerDone = onRecyclerDone
        )
    } else {
        updateCallback?.let {
            refreshBindingItems(items, it)
        }
    }
}

fun <T, VDB : ViewDataBinding> RecyclerView.attachSimpleDataBindingGridData(
        items: List<T> = listOf(),
        countItemsPerLine: Int = 3,
        viewBindingCreator: (parent: ViewGroup, inflater: LayoutInflater) -> VDB,
        prepareHolder: (item: T, viewBinding: VDB, viewHolder: BaseRecyclerAdapterBindingHolder<T, VDB>) -> Unit,
        onRecyclerDone: (adapter: BaseRecyclerAdapterBinding<T, VDB, BaseRecyclerAdapterBindingHolder<T, VDB>>) -> Unit = {},
        updateCallback: DiffUtil.ItemCallback<T>?
) {
    if (adapter == null) {
        createBindingRecyclerViewGridManager(
                items = items,
                countItemsPerLine = countItemsPerLine,
                viewBindingCreator = viewBindingCreator,
                prepareHolder = prepareHolder,
                onRecyclerDone = onRecyclerDone
        )
    } else {
        updateCallback?.let {
            refreshBindingItems(items, it)
        }
    }
}

/**
 * Default adapter to create lists without boilerplate.
 */
abstract class BaseRecyclerAdapterBinding<T, VDB : ViewDataBinding, VH : BaseRecyclerAdapterBindingHolder<T, VDB>>(
        val context: Context,
        var items: MutableList<T>
) : RecyclerView.Adapter<VH>() {

    val allItems: MutableList<T> = ArrayList(items)

    abstract fun prepareViewHolder(parent: ViewGroup): VH

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(position, items[position])
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return prepareViewHolder(parent)
    }

    fun filter(query: String, comparator: (query: String, item: T) -> Boolean) {
        items = if (query.trim().isBlank()) {
            allItems
        } else {
            allItems.filter {
                comparator(query, it)
            }.toMutableList()
        }

        notifyDataSetChanged()
    }

    fun updateItems(items: List<T>) {
        this.allItems.clear()
        this.allItems.addAll(items)

        this.items.clear()
        this.items.addAll(items)
    }
}

/**
 * Default view holder to create lists without boilerplate
 */
abstract class BaseRecyclerAdapterBindingHolder<T, VDB : ViewDataBinding>(private val binding: VDB) : RecyclerView.ViewHolder(binding.root) {
    abstract fun attach(
            position: Int,
            item: T,
            binding: VDB,
            viewHolder: BaseRecyclerAdapterBindingHolder<T, VDB>
    )

    fun bind(position: Int, item: T) {
        attach(position, item, binding, this)
        binding.executePendingBindings()
    }
}

/**
 * Handle the pagination when the list is almost in the and.
 */
class LoadNextPageScrollMonitor(private val loadNextPageHandler: () -> Unit) : RecyclerView.OnScrollListener() {
    private var lastItemVisiblePositionOnList = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val lastItemVisiblePosition = layoutManager.findLastVisibleItemPosition()

        // It is at last but one.
        if (isScrollingDown(lastItemVisiblePosition) && recyclerView.shouldLoadMoreItems()) {
            loadNextPageHandler.invoke()
        }

        lastItemVisiblePositionOnList = lastItemVisiblePosition
    }

    private fun isScrollingDown(lastItemVisiblePosition: Int) = lastItemVisiblePosition > lastItemVisiblePositionOnList

    private fun RecyclerView.shouldLoadMoreItems(): Boolean {
        val layoutManager = layoutManager as LinearLayoutManager

        val totalItemCount = layoutManager.itemCount
        val lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()

        return lastCompletelyVisibleItemPosition > totalItemCount - 3
    }
}