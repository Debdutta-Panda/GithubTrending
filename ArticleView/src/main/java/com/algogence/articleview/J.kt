package com.algogence.articleview

import org.json.JSONArray
import org.json.JSONObject



open class JBase{
    enum class Type{
        UNKNOWN,
        OBJECT,
        ARRAY,
        STRING,
        FLOAT,
        INTEGER,
        LONG,
        BOOLEAN,
        DOUBLE
    }
    var type: Type = Type.UNKNOWN
}
class JArray(private val jsonArray: JSONArray?): JBase() {
    init {
        type = Type.ARRAY
    }
    val children: List<J>
    get(){
        val count = jsonArray?.length()?:0
        val ret = mutableListOf<J>()
        for(i in 0 until count){
            ret.add(J(jsonArray?.get(i)))
        }
        return ret
    }

    val length: Int
    get(){
        return jsonArray?.length()?:0
    }

    operator fun get(index: Int): J?{
        if(index in 0 until length){
            return J(jsonArray?.get(index))
        }
        return null
    }
}
class J(private val src: Any? = null, private val forceObject: Boolean = true): JBase() {
    var value: Any? = null
        private set
    private var json: JSONObject? = null
        private set
    private var array: JSONArray? = null
    init {
        json = try {
            when(src){
                is JSONArray->{
                    array = src
                    type = Type.ARRAY
                    null
                }
                is String->{
                    if(forceObject){
                        type = Type.OBJECT
                        JSONObject(src)
                    }
                    else{
                        value = src
                        type = Type.STRING
                        null
                    }
                }
                is Float->{
                    value = src
                    type = Type.FLOAT
                    null
                }
                is Long->{
                    value = src
                    type = Type.LONG
                    null
                }
                is Double->{
                    value = src
                    type = Type.DOUBLE
                    null
                }
                is Int->{
                    value = src
                    type = Type.INTEGER
                    null
                }
                is Boolean->{
                    value = src
                    type = Type.BOOLEAN
                    null
                }
                is JSONObject->{
                    type = Type.OBJECT
                    src
                }
                else->null
            }
        } catch (e: Exception) {
            null
        }
    }
    operator fun get(key: String): J?{
        if(json?.has(key)==true){
            val v = json?.get(key)
            if(v!=null){
                val j = J(v,false)
                if(j.type!=Type.UNKNOWN){
                    return j
                }
            }
        }
        return null
    }

    fun getByPath(path: String): J? {
        var j: J? = this
        val chain = path.split(".").toMutableList()
        while(j!=null&& j.type !=Type.UNKNOWN){
            if(chain.isNotEmpty()){
                val key = chain.removeFirst()
                if(key.isNotEmpty()){
                    j = j[key]
                    if(j?.type==Type.UNKNOWN){
                        j = null
                    }
                }
                else{
                    j = null
                }
            }
            else{
                break
            }
        }
        return j
    }

    fun has(key: String): Boolean{
        return json?.has(key)==true
    }

    fun asString(): String?{
        return if(type==Type.STRING){
            value as? String
        } else{
            null
        }
    }

    fun asNumber(default: Number = 0): Number?{
        return when(type){
            Type.FLOAT -> value as? Float
            Type.INTEGER -> value as? Int
            Type.LONG -> value as? Long
            Type.DOUBLE -> value as? Double
            else->default
        }
    }

    fun asInt(): Int?{
        return if(type==Type.INTEGER){
            value as? Int
        } else{
            null
        }
    }

    fun asFloat(): Float?{
        return if(type==Type.FLOAT){
            value as? Float
        } else{
            null
        }
    }

    fun asDouble(): Double?{
        return if(type==Type.DOUBLE){
            value as? Double
        } else{
            null
        }
    }

    fun asBoolean(): Boolean?{
        return if(type==Type.BOOLEAN){
            value as? Boolean
        } else{
            null
        }
    }

    fun asLong(): Long?{
        return if(type==Type.LONG){
            value as? Long
        } else{
            null
        }
    }

    fun asArray(): JArray?{
        return if(type==Type.ARRAY){
            JArray(array)
        } else{
            null
        }
    }



    fun forEachKey(block: (String)->Unit){
        json?.keys()?.forEach(block)
    }

    val keys: List<String>
    get(){
        val k = mutableListOf<String>()
        forEachKey {
            k.add(it)
        }
        return k
    }
}