package red.code101.app.adapters.recycler

import android.view.LayoutInflater
import android.view.View
import code101.domain.AuthProvider
import red.code101.app.adapters.BindingAdapters.loadImage
import red.code101.app.adapters.core.AbstractAdapterPosition
import red.code101.app.databinding.ItemAuthProviderBinding
import red.code101.app.ui.auth.LinkProvider

class AuthProviderAdapter(
    authProviders: List<AuthProvider>,
    private val onUnlink: (providerId: String) -> Unit
) : AbstractAdapterPosition<AuthProvider, ItemAuthProviderBinding>(authProviders) {
    override fun onCreteHolder(i: LayoutInflater) = ItemAuthProviderBinding.inflate(i)

    override fun ItemAuthProviderBinding.bind(item: AuthProvider, position: Int) {
        val linkProvider = LinkProvider.getOrDefault(item.id)
        iconProvider.setImageResource(linkProvider.resIdIcon)
        iconProvider.setBackgroundResource(linkProvider.resIdBgColor)

        labelNameProvider.setText(linkProvider.resIdLabel)

        loadImage(photoProvider, item.photoUrl)

        val displayName =
            if (!item.displayName.isNullOrBlank()) item.displayName else item.email
        labelDisplayName.text = displayName

        if (position != 0) {
            fabUnlink.visibility = View.VISIBLE
            fabUnlink.setOnClickListener { onUnlink.invoke(item.id) }
        } else {
            fabUnlink.visibility = View.GONE
        }
    }
}
