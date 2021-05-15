package ru.spbstu.gusev.medicinesstorage.extensions

import android.content.res.Resources
import android.graphics.PorterDuff
import android.view.Menu
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import ru.spbstu.gusev.medicinesstorage.R

fun Menu.setIconsColor(@ColorInt color: Int) {
    this.forEach {
        it.icon.setColorFilter(
            color, PorterDuff.Mode.SRC_ATOP
        )
    }
}

fun Menu.setupSearch(fragment: Fragment) {
    val menuItem = this.findItem(R.id.menu_item_search)
    fragment.activity?.findViewById<MaterialToolbar>(R.id.toolbar)?.let {
        it.setOnClickListener { menuItem.expandActionView() }
    }
    val searchView = menuItem.actionView as SearchView
    searchView.apply {
        queryHint = this.resources.getString(R.string.search_hint)
        isSubmitButtonEnabled = true
        findViewById<EditText>(androidx.appcompat.R.id.search_src_text).apply {
            setTextColor(fragment.getColorFromTheme(R.attr.colorOnPrimary))
        }
    }
}