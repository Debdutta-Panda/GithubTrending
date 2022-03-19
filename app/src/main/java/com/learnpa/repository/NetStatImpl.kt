package com.learnpa.repository

import com.learnpa.app.MyApplication
import com.learnpea.models.Net
import com.learnpea.repository.NetStat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NetStatImpl: NetStat {
    override val current: Net
        get() {
            val n = MyApplication.net
            return Net(n.on, n.metered)
        }

    override suspend fun waitForOnline(): Boolean =
        suspendCoroutine { cont ->
            var listener: ((Net)->Unit)? = null
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