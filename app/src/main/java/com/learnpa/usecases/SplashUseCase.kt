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
        emit(Command(Action.LOADING))
        delay(3000)
        emit(Command(Action.GO_TO_PAGE, Routes.Home))
    }
}