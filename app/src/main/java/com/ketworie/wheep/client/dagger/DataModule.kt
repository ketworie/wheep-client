package com.ketworie.wheep.client.dagger

import android.app.Application
import androidx.room.Room
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ketworie.wheep.client.MainApplication.Companion.WEB_SERVER
import com.ketworie.wheep.client.chat.ChatService
import com.ketworie.wheep.client.chat.MessageDao
import com.ketworie.wheep.client.hub.HubDao
import com.ketworie.wheep.client.network.NetworkResponseAdapterFactory
import com.ketworie.wheep.client.room.Database
import com.ketworie.wheep.client.security.AuthInterceptor
import com.ketworie.wheep.client.user.UserDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton


@Module
class DataModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideObjectMapper(): ObjectMapper {
        return jacksonObjectMapper().registerModule(JavaTimeModule())
    }

    @Provides
    @Singleton
    fun provideChatService(client: OkHttpClient, objectMapper: ObjectMapper): ChatService {
        val retrofit = Retrofit.Builder()
            .baseUrl(WEB_SERVER)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .build()
        return retrofit.create(ChatService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(): Database {
        return Room.databaseBuilder(application, Database::class.java, "wheep")
            .fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideUserDao(db: Database): UserDao {
        return db.userDao()
    }

    @Provides
    @Singleton
    fun providesHubDao(db: Database): HubDao {
        return db.hubDao()
    }

    @Provides
    @Singleton
    fun providesMessageDao(db: Database): MessageDao {
        return db.messageDao()
    }

}