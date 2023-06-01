package com.humbhi.blackbox.ui.data.network

import com.fasterxml.jackson.databind.ObjectMapper
import org.eclipse.paho.client.mqttv3.*
import java.util.concurrent.Executors

class MqttApiClient(
    private val mqttClient: MqttClient,
    private val objectMapper: ObjectMapper,
    private val loaderCallback: ((Boolean) -> Unit)? = null
) {
    private val executor = Executors.newSingleThreadExecutor()

    fun <T> get(url: String, responseClass: Class<T>, callback: (T) -> Unit) {
        loaderCallback?.invoke(true)

        executor.execute {
            mqttClient.subscribe(url, object : IMqttMessageListener {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    val response = objectMapper.readValue(message?.payload, responseClass)
                    callback(response)
                    mqttClient.unsubscribe(url)

                    loaderCallback?.invoke(false)
                }
            })
        }
    }

    fun <T> post(url: String, body: Any, responseClass: Class<T>, callback: (T) -> Unit) {
        loaderCallback?.invoke(true)

        executor.execute {
            val payload = objectMapper.writeValueAsString(body)
            mqttClient.subscribe(url, object : IMqttMessageListener {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    val response = objectMapper.readValue(message?.payload, responseClass)
                    callback(response)
                    mqttClient.unsubscribe(url)

                    loaderCallback?.invoke(false)
                }
            })
            mqttClient.publish(url, MqttMessage(payload.toByteArray()))
        }
    }
}
