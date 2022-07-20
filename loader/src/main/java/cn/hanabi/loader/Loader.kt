package cn.hanabi.loader

import cn.hanabi.loader.antidump.AntiDump
import com.google.gson.Gson
import com.google.gson.JsonObject
import net.minecraft.launchwrapper.Launch
import net.minecraft.launchwrapper.LaunchClassLoader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.Socket
import java.net.URLClassLoader
import java.nio.charset.StandardCharsets
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object Loader {
//URLClassLoader(arrayOf(), Launch.classLoader)

    var mixinByte = ByteArray(0)
    var refMapByte = ByteArray(0)
    var mixinCache = mutableListOf<String>()
    val refMapFile = File(System.getProperty("java.io.tmpdir"), "+~JF${getRandomString(18)}" + ".tmp")
    var shouldInit = false

    inline fun load() {
        AntiDump.checkLaunchFlags()
        AntiDump.disableJavaAgents()
        AntiDump.setPackageNameFilter()
        AntiDump.dissasembleStructs()
//

        val resourceCache = LaunchClassLoader::class.java.getDeclaredField("resourceCache").let {
            it.isAccessible = true
            it[Launch.classLoader] as MutableMap<String, ByteArray>
        }


        val host = "173.82.163.16"
        val port = 37254 //你的驗證伺服器端口

        val fileSocket = Socket(host, port)
        val inputF = DataInputStream(fileSocket.getInputStream())
        val outputF = DataOutputStream(fileSocket.getOutputStream())

        var passed = false
        //驗證的東西
        outputF.writeUTF("HIHI")

        if (inputF.readUTF().equals("Passed")) {
            passed = true
        }

        if (passed) {
            ZipInputStream(inputF).use { zipStream ->
                var zipEntry: ZipEntry?
                while (zipStream.nextEntry.also { zipEntry = it } != null) {
                    var name = zipEntry!!.name
                    if (name.endsWith(".class")) {
                        name = name.removeSuffix(".class")
                        name = name.replace('/', '.')
                        resourceCache[name] = zipStream.readBytes()
//                        zipStream.readBytes().let {
//                            defineClass(name, it, 0, it.size)
//                        }
                    } else {
                        if (name == "mixins.hanabi.json") {
                            mixinByte = zipStream.readBytes()
                        } else if (name == "mixins.hanabi.refmap.json") {
                            refMapByte = zipStream.readBytes()
                        }
                    }
                }
            }
            val mixinConfig: JsonObject = Gson().fromJson(String(mixinByte, StandardCharsets.UTF_8), JsonObject::class.java)
            mixinConfig.getAsJsonArray("client").forEach {
                mixinCache.add(it.asString)
            }
            mixinConfig.getAsJsonArray("mixins").forEach {
                mixinCache.add(it.asString)
            }
            shouldInit = true
            fileSocket.close()
        } else {
            val shutDownMethod = Class.forName("java.lang.Shutdown").getDeclaredMethod("exit", Integer.TYPE)
            shutDownMethod.isAccessible = true
            shutDownMethod.invoke(null, 0)
        }

        // replace classloader
//        val newClassLoader = object : LaunchClassLoader(Launch.classLoader.urLs) {
//            init {
//                this.javaClass.getDeclaredField("parent").set(this, this@LoaderClassLoader)
//            }
//        }
//
//        Launch.classLoader = newClassLoader
    }
}

inline fun File.writeToFile(byte: ByteArray) {
    FileOutputStream(this).use {
        it.write(byte)
        it.flush()
        it.close()
    }
}

inline fun getRandomString(length: Int): String {
    val allowedChars = ('0'..'9') + ('a'..'z') + ('A'..'Z')
    return (1..length).map { allowedChars.random() }.joinToString("")
}