package com.learnpa.usecases


import android.net.wifi.p2p.WifiP2pDevice.FAILED
import android.provider.Contacts.PresenceColumns.OFFLINE
import android.util.Log
import com.learnpa.models.Action
import com.learnpa.models.Command
import com.learnpa.repository.NetStat
import com.learnpa.repository.SplashRepository
import com.learnpea.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SplashUseCase @Inject constructor(
    private val repository: SplashRepository,
    private val dataStore: com.learnpa.repository.DataStore,
    private val netStat: NetStat
) {

    operator fun invoke(): Flow<Command> = flow {
        val l = repository.languages()
        Log.d("fdlkfjsfjlf",l.toString())
        emit(Command(Action.LOADING))
        var fetched = false
        while(!fetched){
            if(!netStat.current.on){
                emit(Command(Action.OFFLINE))
                netStat.waitForOnline()
            }
            emit(Command(Action.ONLINE))
            delay(2000)
            emit(Command(Action.LOADING))
            val r = repository.getEndPoint()
            emit(Command(Action.VALIDATING))
            if(r!=null&&r.success&&(r.endPoint?.baseUrl?.isNotEmpty()==true)){
                emit(Command(Action.SUCCESS))
                fetched = true
                dataStore.setBaseUrl(r.endPoint.baseUrl)
                emit(Command(Action.DONE))
                delay(1000)
                emit(Command(Action.GO_TO_PAGE, Routes.Home))
            }
            else{
                emit(Command(Action.FAILED))
                delay(2000)
                emit(Command(Action.RETRYING))
                delay(2000)
            }
        }
    }
}