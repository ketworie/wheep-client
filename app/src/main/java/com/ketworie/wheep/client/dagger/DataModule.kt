package com.ketworie.wheep.client.dagger

import android.app.Application
import androidx.room.Room
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ketworie.wheep.client.MainApplication.Companion.SERVER_BASE
import com.ketworie.wheep.client.chat.ChatService
import com.ketworie.wheep.client.hub.HubDao
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
    fun provideChatService(client: OkHttpClient): ChatService {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_BASE)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
            .build()
        return retrofit.create(ChatService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(): Database {
        return Room.databaseBuilder(application, Database::class.java, "wheep").build()
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

}