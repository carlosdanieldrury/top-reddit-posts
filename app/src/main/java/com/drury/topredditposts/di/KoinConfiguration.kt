package com.drury.topredditposts.di

import android.content.Context
import com.drury.topredditposts.data.network.RedditApi
import com.drury.topredditposts.data.network.TopRedditRepositoryImpl
import com.drury.topredditposts.domain.TopRedditRepository
import com.drury.topredditposts.domain.TopRedditUseCase
import com.drury.topredditposts.domain.TopRedditUseCaseImpl
import com.drury.topredditposts.presentation.viewmodel.MainListViewModel
import com.drury.topredditposts.presentation.viewmodel.TopRedditPostDetailsViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://www.reddit.com/"

fun createKoinConfiguration(context: Context) {
    startKoin {
        androidContext(context)
        androidLogger(Level.ERROR)
        modules(
            listOf(
                createNetworkModule(),
                createRepositoryModule(),
                createUseCaseModule(),
                createViewModelModule()
            )
        )
    }
}

fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder().build()
}

fun provideApi(retrofit: Retrofit): RedditApi {
    return retrofit.create(RedditApi::class.java)
}

fun createNetworkModule() = module {
    factory { provideOkHttpClient()}
    factory { provideApi(get()) }
    single { provideRetrofit(get()) }
}

fun createRepositoryModule() = module {
    single<TopRedditRepository> { TopRedditRepositoryImpl(get()) }
}

fun createUseCaseModule() = module {
    single<TopRedditUseCase> { TopRedditUseCaseImpl(get()) }
}

fun createViewModelModule() = module {
    viewModel { MainListViewModel(get()) }
    viewModel { TopRedditPostDetailsViewModel(get()) }
}
