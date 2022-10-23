package red.code101.app.adapters.recycler

import android.view.LayoutInflater
import red.code101.app.adapters.core.AbstractAdapter
import red.code101.app.databinding.ItemAuthLinkProviderBinding
import red.code101.app.ui.auth.LinkProvider

class AuthLinkProviderAdapter(
    providers: List<LinkProvider>,
    private val onProviderClick: (LinkProvider) -> Unit
) : AbstractAdapter<LinkProvider, ItemAuthLinkProviderBinding>(providers) {
    override fun onCreteHolder(i: LayoutInflater) = ItemAuthLinkProviderBinding.inflate(i)

    override fun ItemAuthLinkProviderBinding.bind(item: LinkProvider) {
        button.setText(item.resIdLabel)
        button.setIconResource(item.resIdIcon)
        button.setOnClickListener { onProviderClick.invoke(item) }
    }
}