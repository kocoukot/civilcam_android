package com.digi_crony.service.socket

import com.digi_crony.BuildConfig
import com.digi_crony.domain.usecase.GetUserSessionTokenUseCase
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import java.net.URI
import java.net.URISyntaxException


object SocketHandler : KoinComponent {
    private const val DIGI_NAMESPACE = "/digicrony"
    lateinit var mSocket: Socket
    private val getUserSessionTokenUseCase: GetUserSessionTokenUseCase by inject()

    @Synchronized
    fun setSocket() {
        try {
            val address = BuildConfig.SOCKET_URL

            val options = IO.Options().apply {
                timeout = 30000
                reconnection = true
            }
            options.query = "X-Session-ID=${getUserSessionTokenUseCase.invoke()}"

            val manager = Manager(URI.create(address), options)
            mSocket = manager.socket(DIGI_NAMESPACE)

        } catch (e: URISyntaxException) {
            Timber.d("socket error ${e.localizedMessage}")

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {

        mSocket
            .on(Socket.EVENT_CONNECT) {
                Timber.d("socket connect ${mSocket.id()} ${mSocket.connected()}")
            }
            .on(Socket.EVENT_CONNECT_ERROR) { array ->
                Timber.d("socket error ${array.map { it }}")
            }
            .on(Socket.EVENT_DISCONNECT) { array ->
                Timber.d("socket disconnected ${array.map { it }}")
            }
            .on("accessDenied") { array ->
                Timber.d("socket accessDenied ${array.map { it }}")

            }
        mSocket.io().reconnection(true)
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }

}