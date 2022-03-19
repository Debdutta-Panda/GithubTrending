package com.learnpa

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.learnpa.app.MyApplication
import java.util.prefs.Preferences

//val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "counter")

@Composable
fun Int.SpaceX(){
    Spacer(modifier = Modifier.width(this.dp))
}
@Composable
fun Int.SpaceY(){
    Spacer(modifier = Modifier.height(this.dp))
}
val Int.string: String
get(){
    return MyApplication.instance?.stringResource(this)?:""
}

val Int.resourceDrawable: Drawable?
get(){
    return MyApplication.instance?.drawableResource(this)?:null
}

val Int.resourceDimension: Float
get(){
    return try {
        MyApplication.instance?.dimensionResource(this)?:0f
    } catch (e: Exception) {
        0f
    }
}

val Int.resourceColor: Int
get(){
    return MyApplication.instance?.colorResource(this)?:0
}