package com.dailykorean.app.main.my

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.dailykorean.app.R
import kotlinx.android.synthetic.main.empty_list.view.*
import kotlinx.android.synthetic.main.fragment_favorite_expression.view.*
import kotlinx.android.synthetic.main.my_toolbar.*

/**
 * Created by musooff on 04/01/2019.
 */

abstract class EditActionModeFragment : Fragment() {


    protected abstract fun getActionAdapter(): ActionAdapter

    protected abstract fun getEmptyString(): String

    fun invalidateEmptyList(view: View) {
        if (getActionAdapter().isEditable()){
            view.empty.visibility = View.GONE
        }
        else {
            view.empty.visibility = View.VISIBLE
            view.empty_text.text = getEmptyString()
        }
    }

    var selectedItems = ArrayList<String>()

    private lateinit var actionBar: ActionBar
    private lateinit var editMenu: MenuItem
    private lateinit var cancelMenu: MenuItem

    var isActionMode: Boolean = false


    interface ActionAdapter {
        fun isEditable(): Boolean

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        actionBar = (activity as AppCompatActivity).supportActionBar!!
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.menu_select -> updateOptionMenu(true)
            R.id.menu_cancel -> updateOptionMenu(false)
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateOptionMenu(enabled: Boolean){
        selectedItems = ArrayList()
        isActionMode = enabled
        if (enabled){
            actionBar.title = ""
            activity!!.toolbar_delete.visibility = View.VISIBLE
        }
        else{
            activity!!.toolbar_delete.visibility = View.GONE
            actionBar.title = getString(R.string.title_my_tb)
        }
        editMenu.isVisible = !isActionMode
        cancelMenu.isVisible = isActionMode
        (getActionAdapter() as RecyclerView.Adapter<*>).notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.my, menu)
        editMenu = menu.findItem(R.id.menu_select)
        cancelMenu = menu.findItem(R.id.menu_cancel)

        editMenu.isEnabled = getActionAdapter().isEditable()
        cancelMenu.isVisible = isActionMode

        super.onCreateOptionsMenu(menu, inflater)
    }

    fun invalidateOptionMenu(){
        activity!!.invalidateOptionsMenu()
    }

    fun updateCheckedItem(expressionId: String, view: View){
        if (selectedItems.contains(expressionId)){
            selectedItems.remove(expressionId)
        }
        else{
            selectedItems.add(expressionId)
        }
        view.visibility = View.VISIBLE
        view.isEnabled = !view.isEnabled

    }
}