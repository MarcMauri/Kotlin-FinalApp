package es.marcmauri.mylibrary.interfaces

import android.support.v7.widget.Toolbar

interface IToolbar {
    fun toolbarToLoad(toolbar: Toolbar?)
    fun enablehomeDisplay(value: Boolean)
}