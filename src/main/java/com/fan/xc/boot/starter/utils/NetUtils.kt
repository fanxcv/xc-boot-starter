package com.fan.xc.boot.starter.utils

import com.fan.xc.boot.starter.exception.XcToolsException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

/**
 * 基于HttpConnection的网络请求实现
 */
object NetUtils {
    private val log: Logger = LoggerFactory.getLogger(NetUtils::class.java)

    /**
     * 发送get请求
     */
    @JvmStatic
    fun <T> get(uri: String, function: (uri: String, connection: HttpURLConnection) -> T): T {
        var connection: HttpURLConnection? = null
        try {
            connection = initConnection(uri)
            connection.requestMethod = "GET"
            return function(uri, connection)
        } catch (e: IOException) {
            log.error("请求地址：{}", uri, e)
            throw XcToolsException(e)
        } finally {
            connection?.disconnect()
        }
    }

    @JvmStatic
    fun get(uri: String): String {
        return get(uri, this::getResult)
    }

    /**
     * 发送post请求,json参数
     */
    @JvmStatic
    fun <T> post(uri: String, body: String, function: (uri: String, connection: HttpURLConnection) -> T): T {
        var connection: HttpURLConnection? = null
        try {
            val bytes = body.toByteArray(StandardCharsets.UTF_8)

            connection = initPostConnection(uri)
            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
            connection.setRequestProperty("Content-Length", bytes.size.toString())
            connection.outputStream.use { os ->
                os.write(bytes)
                os.flush()
            }
            return function(uri, connection)
        } catch (e: IOException) {
            log.error("请求地址：{} \n\t {}", uri, body, e)
            throw XcToolsException(e)
        } finally {
            connection?.disconnect()
        }
    }

    @JvmStatic
    fun post(uri: String, body: String): String {
        return post(uri, body, this::getResult)
    }

    /**
     * 发送post请求, 支持自定义header
     */
    @JvmStatic
    fun <T> post(uri: String, params: Map<String, Any?>, headers: Map<String, String>? = null, function: (uri: String, connection: HttpURLConnection) -> T): T {
        var connection: HttpURLConnection? = null
        try {
            // 如有有入参,处理参数
            var bytes: ByteArray? = null
            if (params.isNotEmpty()) {
                val list = params.map { "${it.key}=${it.value}" }
                bytes = list.joinToString("&").toByteArray(StandardCharsets.UTF_8)
            }

            connection = initPostConnection(uri)
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")

            // 设置header
            headers?.forEach { (k, v) -> connection.setRequestProperty(k, v) }

            if (bytes != null) {
                connection.setRequestProperty("Content-Length", bytes.size.toString())
                connection.outputStream.use { os ->
                    os.write(bytes)
                    os.flush()
                }
            }

            return function(uri, connection)
        } catch (e: IOException) {
            log.error("请求地址：{} \n\t {}", uri, params, e)
            throw XcToolsException(e)
        } finally {
            connection?.disconnect()
        }
    }

    /**
     * 发送post请求,x-www-form-urlencoded传参方式
     */
    @JvmStatic
    fun <T> post(uri: String, params: Map<String, Any?>, function: (uri: String, connection: HttpURLConnection) -> T): T = post(uri, params, null, function)


    @JvmStatic
    fun post(uri: String, params: Map<String, Any?>): String {
        return post(uri, params, this::getResult)
    }

    /**
     * 文件上传
     */
    @JvmStatic
    fun <T> upload(uri: String, fileName: String, `is`: InputStream, function: (uri: String, connection: HttpURLConnection) -> T): T {
        var connection: HttpURLConnection? = null
        try {
            connection = initPostConnection(uri)

            val boundary = "----------" + System.currentTimeMillis()
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")

            // 请求正文信息
            // 第一部分：
            // 2.将文件头输出到微信服务器
            val sb = StringBuilder()
            // 必须多两道线
            sb.append("--")
            sb.append(boundary)
            sb.append("\r\n")
            sb.append("Content-Disposition: form-data;name=\"media\";filelength=\"${`is`.available()}\";filename=\"$fileName\"\r\n")
            sb.append("Content-Type:application/octet-stream\r\n\r\n")
            val head: ByteArray = sb.toString().toByteArray(StandardCharsets.UTF_8)
            // 获得输出流
            DataOutputStream(connection.outputStream).use { dos ->
                // 将表头写入输出流中：输出表头
                dos.write(head)
                // 3.将文件正文部分输出到微信服务器
                // 把文件以流文件的方式 写入到微信服务器中
                DataInputStream(`is`).use { dis ->
                    dis.copyTo(dos)
                }
                // 4.将结尾部分输出到微信服务器
                // 定义最后数据分隔线
                val foot: ByteArray = "\r\n--$boundary--\r\n".toByteArray(StandardCharsets.UTF_8)
                dos.write(foot)
                dos.flush()
            }
            return function(uri, connection)
        } catch (e: IOException) {
            log.error("上传地址：{}", uri, e)
            throw XcToolsException(e)
        } finally {
            connection?.disconnect()
        }
    }

    @JvmStatic
    fun upload(uri: String, fileName: String, `is`: InputStream): String {
        return upload(uri, fileName, `is`, this::getResult)
    }

    private fun getResult(uri: String, connection: HttpURLConnection): String {
        return try {
            BufferedReader(InputStreamReader(connection.inputStream)).use(BufferedReader::readText)
        } catch (e: IOException) {
            throw XcToolsException("failed to parse the returned data, uri: $uri")
        }
    }

    private fun initConnection(uri: String): HttpURLConnection {
        val connection = URL(uri).openConnection() as HttpURLConnection
        connection.connectTimeout = 60 * 1000
        connection.readTimeout = 60 * 1000
        return connection
    }

    private fun initPostConnection(uri: String): HttpURLConnection {
        val connection = initConnection(uri)
        connection.setRequestProperty("connection", "Keep-Alive")
        connection.setRequestProperty("Charset", "UTF-8")
        connection.requestMethod = "POST"
        connection.useCaches = false
        connection.doOutput = true
        connection.doInput = true
        return connection
    }

    // class Builder {
    //     private var url: URL? = null
    //     private var contentType: String? = null
    //     private val param: MutableMap<String, Any?> = Maps.newHashMap()
    //     private val header: MutableMap<String, String> = Maps.newHashMap()
    //
    //     constructor()
    //     constructor(url: String) {
    //         this.url = URL(url)
    //     }
    //
    //     /**
    //      * 设置Url
    //      */
    //     fun url(url: String): Builder {
    //         this.url = URL(url)
    //         return this
    //     }
    //
    //     /**
    //      * 添加单个参数
    //      */
    //     fun addParam(k: String, v: Any?): Builder {
    //         this.param[k] = v
    //         return this
    //     }
    //
    //     /**
    //      * 添加多个参数
    //      */
    //     fun addParams(vs: Map<String, Any?>): Builder {
    //         this.param.putAll(vs)
    //         return this
    //     }
    //
    //     /**
    //      * 添加上传文件
    //      * @param name 上传对象名
    //      * @param is 上传对象流
    //      */
    //     fun addUploadData(name: String, `is`: InputStream): Builder {
    //         this.contentType
    //     }
    //
    //     /**
    //      * 添加单个请求头
    //      */
    //     fun addHeader(k: String, v: String): Builder {
    //         this.header[k] = v
    //         return this
    //     }
    //
    //     /**
    //      * 添加多个参数
    //      */
    //     fun addHeaders(vs: Map<String, String>): Builder {
    //         this.header.putAll(vs)
    //         return this
    //     }
    //
    //
    // }

    // fun get(url: String): Builder {
    //     return Builder(url)
    // }
}
