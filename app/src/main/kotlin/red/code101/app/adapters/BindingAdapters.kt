package red.code101.app.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code101.domain.AuthProvider
import com.bumptech.glide.Glide
import red.code101.app.adapters.recycler.AuthProviderAdapter
import red.code101.app.adapters.recycler.AuthLinkProviderAdapter
import red.code101.app.ui.auth.LinkProvider

object BindingAdapters {

    @BindingAdapter("photoUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {
        if (url.isNullOrBlank()) return
        Glide.with(view.context).load(url).circleCrop().into(view)
    }

    @BindingAdapter("providers")
    @JvmStatic
    fun loadProviders(view: RecyclerView, authProviders: List<AuthProvider>) {
        view.adapter = AuthProviderAdapter(
            authProviders = authProviders.toMutableList().apply {
                // move first Email and password provider
                firstOrNull { it.id == LinkProvider.EmailAndPassword.id }?.let { provider ->
                    remove(provider)
                    add(0, provider)
                }
            }
        )
        view.layoutManager = object : LinearLayoutManager(view.context) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    @BindingAdapter("app:providersMerge", "app:onProviderClick")
    @JvmStatic
    fun loadProvidersMerge(
        view: RecyclerView,
        authProviders: List<AuthProvider>,
        onProviderClick: (LinkProvider) -> Unit
    ) {
        view.adapter = AuthLinkProviderAdapter(
            // show full list of providers
            providers = LinkProvider.values().toMutableList().apply {
                // remove the provider that is already connected
                removeIf { enumProvider ->
                    authProviders.firstOrNull { it.id == enumProvider.id } != null
                }
            },
            onProviderClick = onProviderClick
        )
        view.layoutManager = object : GridLayoutManager(view.context, 2) {
            override fun canScrollVertically(): Boolean = false
        }
    }
}


