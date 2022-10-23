package red.code101.app.ui.auth

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import red.code101.app.R

enum class LinkProvider(
    val id: String,
    @StringRes
    val resIdLabel: Int,
    @DrawableRes
    val resIdIcon: Int,
    @ColorRes
    val resIdBgColor: Int
) {
    EmailAndPassword(
        id = "password",
        resIdLabel = R.string.label_email_and_password,
        resIdIcon = R.drawable.ic_provider_email,
        resIdBgColor = R.color.bg_email_and_password
    ),
    Google(
        id = "google.com",
        resIdLabel = R.string.label_google,
        resIdIcon = R.drawable.ic_provider_google,
        resIdBgColor = R.color.bg_google
    ),
    Facebook(
        id = "facebook.com",
        resIdLabel = R.string.label_facebook,
        resIdIcon = R.drawable.ic_provider_facebook,
        resIdBgColor = R.color.bg_facebook
    );

    companion object {
        @JvmStatic
        private val byId: MutableMap<String, LinkProvider?> = HashMap()

        init {
            values().forEach {
                byId[it.id] = it
            }
        }

        @JvmStatic
        fun getOrNull(id: String) = try {
            byId[id]
        } catch (_: Exception) {
            null
        }

        @JvmStatic
        fun getOrDefault(id: String) = try {
            byId[id] ?: EmailAndPassword
        } catch (e: Exception) {
            e.printStackTrace()
            EmailAndPassword
        }
    }
}