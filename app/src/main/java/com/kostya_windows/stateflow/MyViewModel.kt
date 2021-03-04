package com.kostya_windows.stateflow

import android.util.DisplayMetrics
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MyViewModel :ViewModel() {

    private val _stateScreen = MutableStateFlow<State>(State.waiting)
    val screenState:StateFlow<State> = _stateScreen

    private val _timerFlow = MutableSharedFlow<String>(replay = 0)
    val timerFlow:SharedFlow<String> = _timerFlow

    fun startTimer(time:String){

        CoroutineScope(Dispatchers.IO).launch {
            var timeInInt = time.toInt()

            if (timeInInt < 60){
                _stateScreen.value = State.errorStarted(message = "Time little!")
            } else {
                _stateScreen.value = State.successStarted(message = "Time was started! Secconds vsego $time")

                while (timeInInt != 0){
                    delay(100)
                    timeInInt--
                    _timerFlow.emit(timeInInt.toString())
                }
                _stateScreen.value = State.timerFinished
            }

        }


    }

    sealed class State{
        object waiting:State()
        data class successStarted(val message:String):State()
        data class errorStarted(val message:String):State()
        object timerFinished:State()
    }


}

