package com.learnpa.di

import android.content.Context
import android.util.Log
import com.learnpa.repository.HomeRepository
import com.learnpa.repository_impl.SplashRepositoryImpl
import com.learnpa.repository.DataStore
import com.learnpa.repository.NetStat
import com.learnpa.repository.SplashRepository
import com.learnpa.repository_impl.DataStoreImpl
import com.learnpa.repository_impl.HomeRepositoryImpl
import com.learnpa.repository_impl.NetStatImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import nl.frank.vmnc.ui.nav.MyRouteNavigator
import nl.frank.vmnc.ui.nav.RouteNavigator
import java.net.UnknownHostException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideCoinRepository(client: HttpClient,dataStore: DataStore): HomeRepository {
        return HomeRepositoryImpl(client,dataStore)
    }
    @Provides
    @Singleton
    fun provideSplashRepository(apiClient: HttpClient): SplashRepository {
        return SplashRepositoryImpl(apiClient)
    }

    @Provides
    @Singleton
    fun provideApiClient(): HttpClient {
        return HttpClient(Android) {
            HttpResponseValidator {
                validateResponse { response ->
                    /*response.content.read {
                        Log.d("flfjjfsjflf",String(it.array()))
                    }*/
                }
                handleResponseException {exception->
                    Log.d("ktor_exception",exception.message?:"no ex message")
                }
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
    }

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext appContext: Context): DataStore {
        return DataStoreImpl(appContext)
    }

    @Singleton
    @Provides
    fun provideNetStat(): NetStat {
        return NetStatImpl()
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    @ViewModelScoped
    fun bindRouteNavigator(): RouteNavigator = MyRouteNavigator()
}