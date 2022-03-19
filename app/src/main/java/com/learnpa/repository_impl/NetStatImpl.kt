package com.learnpa.repository_impl

import com.learnpa.ConnectivityListener
import com.learnpa.app.MyApplication
import com.learnpa.repository.NetStat
import io.ktor.http.*
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NetStatImpl: NetStat {
    override val current: ConnectivityListener.Net
        get() {
            val n = MyApplication.net
            return ConnectivityListener.Net(n.on, n.metered)
        }

    override suspend fun waitForOnline(): Boolean =
        suspendCoroutine { cont ->
            var listener: ((ConnectivityListener.Net)->Unit)? = null
            listener = { net->
                if(net.on){
                    listener?.let {
                        try {
                            MyApplication.unRegisterConnectivityListener(listener!!)
                        } catch (e: Exception) {
                        }
                    }

                    cont.resume(true)
                }
            }
            MyApplication.registerConnectivityListener(listener)
        }
}