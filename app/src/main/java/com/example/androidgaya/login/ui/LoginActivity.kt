package com.example.androidgaya.login.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.androidgaya.R
import com.example.androidgaya.application.ReminderApplication
import com.example.androidgaya.login.viewmodel.LoginViewModel
import com.example.androidgaya.repositories.models.UserPayload
import com.example.androidgaya.socket.SocketService
import com.example.androidgaya.util.LoginNavigator
import com.squareup.moshi.JsonAdapter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var userPayloadAdapter: JsonAdapter<UserPayload>

    var username = ""
    var password = ""
    private lateinit var viewModel: LoginViewModel
    private lateinit var nav: LoginNavigator
    lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as ReminderApplication).getAppComponent()?.injectLogin(this)
        init()
        if (savedInstanceState != null) {
            username_et.setText(savedInstanceState.getString(getString(R.string.username_uppercase), ""))
            password_et.setText(savedInstanceState.getString(getString(R.string.password_uppercase), ""))
        }
        login_btn.setOnClickListener { v: View? -> login() }
        username_et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                login_btn.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    companion object {
        fun getIntent(context: Context?): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(getString(R.string.username_uppercase), username_et.text.toString())
        outState.putString(getString(R.string.password_uppercase), password_et.text.toString())
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    private fun goToMainActivity() {
        nav.toMainActivity()
    }

    fun init() {
        setContentView(R.layout.activity_login)
        serviceIntent = Intent(this, SocketService::class.java)
        startService(serviceIntent)
        initViewModel()
        nav = LoginNavigator(this)
        if (viewModel.userId.isNotEmpty()) {
            goToMainActivity()
        }
        supportActionBar?.hide()
        image_clock.setBackgroundResource(R.drawable.alarm_clock_img)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    fun login() {
        username = username_et.text.toString()
        password = password_et.text.toString()
        viewModel.connectUser(UserPayload(username, password), ::connectUser)
    }

    private fun connectUser(dataFromSocket: Array<Any>, dataFromClient: String) {
        lifecycleScope.launch {
            if (dataFromSocket[0].toString().isNotBlank()) {
                runCatching {
                    userPayloadAdapter.fromJson(dataFromClient)?.let {
                        viewModel.setLoggedIn(application, dataFromSocket[0].toString(), it.username)
                        goToMainActivity()
                    }
                }
            }
        }
    }
}