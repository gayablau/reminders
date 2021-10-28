package com.example.androidgaya.reminders.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidgaya.R
import com.example.androidgaya.factory.ViewModelFactory
import com.example.androidgaya.main.interfaces.MainActivityInterface
import com.example.androidgaya.main.ui.MainActivity
import com.example.androidgaya.reminders.recyclerview.ReminderAdapter
import com.example.androidgaya.reminders.recyclerview.SwipeToDeleteCallback
import com.example.androidgaya.reminders.viewmodel.RemindersViewModel
import com.example.androidgaya.application.ReminderApplication
import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.util.MainNavigator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import javax.inject.Inject

class RemindersFragment : Fragment() {
    var username: String? = ""
    lateinit var addFab: FloatingActionButton
    lateinit var recyclerViewReminders: RecyclerView
    lateinit var remindersList: LiveData<List<ReminderEntity>?>
    var nav: MainNavigator? = null
    lateinit var viewModel: RemindersViewModel
    lateinit var reminderAdapter: ReminderAdapter

    @Inject
    lateinit var factory: ViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as ReminderApplication).getAppComponent()!!.
        injectReminders(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reminders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        setAdapter()
        addFab.setOnClickListener { fabView: View? -> add() }
    }

    override fun onResume() {
        super.onResume()
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { v: View?, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPressed()
                return@setOnKeyListener true
            }
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_profile -> profile()
            R.id.action_logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logout() {
        (activity as? MainActivity)?.logout()
    }

    fun profile() {
        nav?.toProfileFragment()
    }

    private fun onBackPressed() {
        requireActivity().finishAffinity()
    }

    private fun init(view: View) {
        recyclerViewReminders = view.findViewById(R.id.recycler_view_reminders)
        addFab = view.findViewById(R.id.add_fab)
        recyclerViewReminders.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@RemindersFragment.context)
        recyclerViewReminders.setLayoutManager(layoutManager)
        nav = (activity as? MainActivityInterface)?.navigator
        (activity as? MainActivityInterface)?.changeToolbar(getString(R.string.toolbar_main,
                viewModel.username),
                false)
    }

    fun add() {
        nav?.toDetailsFragment()
    }

    private fun setAdapter() {
        reminderAdapter = ReminderAdapter(remindersList, { reminder: ReminderEntity ->
            nav?.toDetailsFragment(reminder.id)
        }) { reminder: ReminderEntity? ->
            viewModel.deleteReminder(reminder!!)
        }
        recyclerViewReminders.adapter = reminderAdapter
        recyclerViewReminders.layoutManager = LinearLayoutManager(this@RemindersFragment.context)
        val itemTouchHelper = ItemTouchHelper(
                SwipeToDeleteCallback(reminderAdapter)
        )
        itemTouchHelper.attachToRecyclerView(recyclerViewReminders)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, factory).get(RemindersViewModel::class.java)

        val reminderObserver = Observer<List<ReminderEntity>?> {
            reminderAdapter.notifyDataSetChanged()
        }

        remindersList = viewModel.getRemindersByUserId()
        remindersList.observe(this, reminderObserver)
    }
}