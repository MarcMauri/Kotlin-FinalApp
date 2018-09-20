package es.marcmauri.mylibrary

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import es.marcmauri.mylibrary.interfaces.IToolbar

open class ToolbarActivity : AppCompatActivity(), IToolbar {

    protected var _toolbar: Toolbar? = null

    override fun toolbarToLoad(toolbar: Toolbar?) {
        _toolbar = toolbar
        _toolbar?.let { setSupportActionBar(_toolbar) }
    }

    override fun enablehomeDisplay(value: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }
}