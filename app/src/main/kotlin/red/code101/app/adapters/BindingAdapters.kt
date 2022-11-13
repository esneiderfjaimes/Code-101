package red.code101.app.adapters

import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code101.domain.AuthProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.button.MaterialButton
import red.code101.app.R
import red.code101.app.adapters.recycler.AuthLinkProviderAdapter
import red.code101.app.adapters.recycler.AuthProviderAdapter
import red.code101.app.ui.auth.LinkProvider
import red.code101.app.utils.dpToPx
import red.code101.app.utils.getMenuItem

object BindingAdapters {

    @BindingAdapter("photoUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {
        if (url.isNullOrBlank()) return
        Glide.with(view.context).load(url).circleCrop().into(view)
    }

    @BindingAdapter("photoUrl")
    @JvmStatic
    fun loadImage(view: TextView, url: String?) {
        if (url.isNullOrBlank()) return
        Glide.with(view.context).asDrawable().load(url)
            .apply(RequestOptions().override(64.dpToPx(), 64.dpToPx()))
            .circleCrop().into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, t: Transition<in Drawable>?) {
                    view.setCompoundDrawablesRelativeWithIntrinsicBounds(null, resource, null, null)
                }

                override fun onLoadCleared(placeholder: Drawable?) = Unit
            })
    }

    @BindingAdapter("app:providers", "app:onUnlink")
    @JvmStatic
    fun loadProviders(
        view: RecyclerView,
        authProviders: List<AuthProvider>,
        onUnlink: (String) -> Unit
    ) {
        view.adapter = AuthProviderAdapter(
            authProviders = authProviders.toMutableList().apply {
                // move first Email and password provider
                firstOrNull { it.id == LinkProvider.EmailAndPassword.id }?.let { provider ->
                    remove(provider)
                    add(0, provider)
                }
            },
            onUnlink = onUnlink
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

    @BindingAdapter("mcbOnClick")
    @JvmStatic
    fun setMenuCloseButtonOnClickListener(
        view: Toolbar,
        onClick: () -> Unit
    ) {
        view.setOnMenuItemClickListener {
            if (it.itemId == R.id.close) {
                onClick.invoke()
                true
            } else false
        }
    }

    @BindingAdapter("navigationOnClick")
    @JvmStatic
    fun setNavigationOnClickListener(
        view: Toolbar,
        onClick: () -> Unit
    ) {
        view.setNavigationOnClickListener {
            onClick.invoke()
        }
    }

    @BindingAdapter("app:mtbLabel", "app:mtbOnClick")
    @JvmStatic
    fun loadToolbarMenuTextButton(
        view: Toolbar,
        label: String,
        onClick: () -> Unit
    ) {
        (view.getMenuItem(0)?.actionView as MaterialButton?)?.apply {
            text = label
            setOnClickListener { onClick.invoke() }
        }
    }
}


