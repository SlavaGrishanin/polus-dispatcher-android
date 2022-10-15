package io.github.grishaninvyacheslav.polus_dispatcher.domain.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.grishaninvyacheslav.polus_dispatcher.BuildConfig
import io.github.grishaninvyacheslav.polus_dispatcher.domain.di.providers.LocalCiceroneHolder
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.data_sources.IExecutorDataSource
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.data_sources.IJobsDataSource
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs.IJobsRepository
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.jobs.JobsRepository
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.profile.IProfileRepository
import io.github.grishaninvyacheslav.polus_dispatcher.domain.models.repositories.profile.ProfileRepository
import io.github.grishaninvyacheslav.polus_dispatcher.model.data_sources.IAuthDataSource
import io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.auth.AuthRepository
import io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.auth.IAuthRepository
import io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.preferences.IPreferencesRepository
import io.github.grishaninvyacheslav.polus_dispatcher.model.repositories.preferences.PreferencesRepository
import io.github.grishaninvyacheslav.polus_dispatcher.ui.screens.IScreens
import io.github.grishaninvyacheslav.polus_dispatcher.ui.screens.Screens
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.auth.AuthViewModel
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.job.JobViewModel
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.main.MainViewModel
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.sheet.SheetViewModel
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.test.TestMainViewModel
import io.github.grishaninvyacheslav.polus_dispatcher.ui.view_models.test.SubViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { LocalCiceroneHolder() }
    single { provideScreens() }
    single { provideAuthRepository(get(), get()) }
    single { providePreferencesRepository(get()) }
    single { provideJobsRepository(get(), get()) }
    single { provideProfileRepository(get(), get()) }

    viewModel { MainViewModel(get()) }
    viewModel { TestMainViewModel() }
    viewModel { SubViewModel() }
    viewModel { AuthViewModel(get()) }
    viewModel { SheetViewModel(get()) }
    viewModel { JobViewModel(get(), get()) }

    // API Module
    single(named("baseUrl")) { provideBaseUrl() }
    single { provideAuthApi(get(named("baseUrl")), get()) }
    single { provideGson() }
    single { provideJobApi() }
    single { provideExecutorApi() }
}

fun provideScreens(): IScreens = Screens()

fun provideAuthRepository(
    authApi: IAuthDataSource,
    preferencesRepository: IPreferencesRepository
): IAuthRepository = AuthRepository(authApi, preferencesRepository)

fun providePreferencesRepository(
    context: Context
): IPreferencesRepository = PreferencesRepository(context)

fun provideJobsRepository(
    jobsApi: IJobsDataSource,
    authRepository: IAuthRepository
): IJobsRepository = JobsRepository(jobsApi, authRepository)

fun provideProfileRepository(
    executorApi: IExecutorDataSource,
    authRepository: IAuthRepository
): IProfileRepository = ProfileRepository(executorApi, authRepository)

// API Module
fun provideBaseUrl(): String = BuildConfig.API_URL

fun provideAuthApi(
    baseUrl: String,
    gson: Gson
): IAuthDataSource {
    val loggingInterceptor = HttpLoggingInterceptor()
        .apply { level = HttpLoggingInterceptor.Level.BODY }
    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(IAuthDataSource::class.java)
}

fun provideGson(): Gson = GsonBuilder().create()

fun provideJobApi(): IJobsDataSource {
    return object : IJobsDataSource {}
}

fun provideExecutorApi(): IExecutorDataSource {
    return object : IExecutorDataSource {}
}