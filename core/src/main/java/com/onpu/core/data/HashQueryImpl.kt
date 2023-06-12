package com.onpu.core.data

import android.util.Base64
import com.onpu.domain.component.HashQuery
import java.net.URLEncoder
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class HashQueryImpl: HashQuery {

    override fun paramsToHash(vararg param: Pair<String, String>, isErrorServer:Boolean) = sortedMapToHash(sortedMapOf(*param),isErrorServer)

    override fun paramsToHash(param: Map<String, String>, isErrorServer:Boolean) = sortedMapToHash(param.toSortedMap(),isErrorServer)

    private fun sortedMapToHash(map: SortedMap<String, String>, isErrorServer: Boolean): String {
        val text = map.joinToString()
        println("qwert 0 " + map)
        println("qwert 1 " + text)
        val mac = Mac.getInstance(algorithmHMAC)
        println("qwert 2 " + mac)
        mac.init(SecretKeySpec(
            if(isErrorServer) errorKey.toByteArray() else signKey.toByteArray(),
            algorithmHMAC))
        println("qwert 3 " + mac)
        val encode = mac.doFinal(text.toByteArray()).base64Encode()
        println("qwert 4 " + encode)
        var hash = ""
        for (index in encode.indices) {
            val c = encode[index]
            hash += when {
                c == '+' -> '-'
                c == '/' -> '_'
                (encode.length - 1) == index && c == '=' -> continue
                else -> c
            }
        }

        return hash
    }

    private fun SortedMap<String, String>.joinToString(): String {
//        return entries.joinToString("&") { "${it.key.urlEncode()}=${it.value.urlEncode()}" }
        return entries.joinToString("&") { "${it.key}=${it.value}" }
    }

    private fun String.urlEncode() = URLEncoder.encode(this, "UTF-8")

    private fun ByteArray.base64Encode(): String = Base64
        .encodeToString(this, Base64.NO_WRAP)

    companion object {
        const val algorithmHMAC = "HmacSHA256"
        const val signKey = "437gGUVF73dg&&hfd38!*34jvnUuhif4rhkjbnjyHkKL(Hk)927#%3sb"
        const val errorKey = "438fndjkJKNddfuij(*NdekmvlekmcJNHuybecfjNK;evecij*mdKKlslf"
    }

}

/*
package com.onpu.core.data

import android.util.Base64
import com.onpu.domain.component.HashQuery
import java.net.URLEncoder
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class HashQueryImpl: HashQuery {

    override fun paramsToHash(vararg param: Pair<String, String>) = sortedMapToHash(sortedMapOf(*param))

    override fun paramsToHash(param: Map<String, String>) = sortedMapToHash(param.toSortedMap())

    private fun sortedMapToHash(map: SortedMap<String, String>): String {
        val text = map.joinToString()
        println(text)
        val mac = Mac.getInstance(algorithmHMAC)
        mac.init(SecretKeySpec(signKey.toByteArray(), algorithmHMAC))
        val encode = mac.doFinal(text.toByteArray()).base64Encode()
        var hash = ""
        for (index in encode.indices) {
            val c = encode[index]
            hash += when {
                c == '+' -> '-'
                c == '/' -> '_'
                (encode.length - 1) == index && c == '=' -> continue
                else -> c
            }
        }
        return hash
    }

    private fun SortedMap<String, String>.joinToString(): String {
        return entries.joinToString("&") { "${it.key.urlEncode()}=${it.value.urlEncode()}" }
    }

    private fun String.urlEncode() = URLEncoder.encode(this, "UTF-8")

    private fun ByteArray.base64Encode(): String = Base64
        .encodeToString(this, Base64.NO_WRAP)

    companion object {
        const val algorithmHMAC = "HmacSHA256"
        const val signKey = "437gGUVF73dg&&hfd38!*34jvnUuhif4rhkjbnjyHkKL(Hk)927#%3sb"
    }

}
* */