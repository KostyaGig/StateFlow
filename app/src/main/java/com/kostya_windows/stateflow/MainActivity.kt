package com.kostya_windows.stateflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private val TAG = "MyMainActivity"

    private lateinit var viewModel: MyViewModel

    private lateinit var toolbar: Toolbar

    private lateinit var setTimeBtn:Button
    private lateinit var fieldTimer:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        setUpToobar()

        lifecycleScope.launchWhenStarted {
            viewModel.screenState.collect{
                state ->
                when(state){

                    is MyViewModel.State.waiting -> {
                        Toast.makeText(this@MainActivity,"Timer еще не запущен",Toast.LENGTH_SHORT).show()
                    }
                    is MyViewModel.State.successStarted -> {
                        Log.d(TAG, state.message)
                        Toast.makeText(this@MainActivity,"Timer запущен",Toast.LENGTH_SHORT).show()
                    }
                    is MyViewModel.State.errorStarted -> {
                        Toast.makeText(this@MainActivity,"Время не может быть меньше 60 секунд!",Toast.LENGTH_SHORT).show()
                        Log.d(TAG,state.message)
                    }
                    is MyViewModel.State.timerFinished -> {
                        Toast.makeText(this@MainActivity,"Время не может быть меньше 60 секунд!",Toast.LENGTH_SHORT).show()
                        Log.d(TAG,"Timer is finished!")
                        toolbar.title = "Timer was finished!"
                    }

                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.timerFlow.collect {
                currentSecond ->

                Log.d(TAG, "Second left -> $currentSecond")
                toolbar.title = "Second left -> "  + currentSecond
            }
        }

        setTimeBtn.setOnClickListener {
            if (fieldTimer.text.toString().isNotEmpty()) {
                val time = fieldTimer.text.toString()

                viewModel.startTimer(time)
            } else {
                Toast.makeText(this,"Field empty!",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun init() {
        viewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        setTimeBtn = findViewById(R.id.set_time_btn)
        fieldTimer = findViewById(R.id.field_timer)
    }

    private fun setUpToobar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

}