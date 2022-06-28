package com.civilcam.di

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.civilcam.data.local.ContactsStorage
import com.civilcam.data.local.MediaStorage
import org.koin.dsl.module

private const val PREFERENCES_FILE_NAME = "standardmedia.prefs"
private val key = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

val storageModule = module {

    single {
        kotlin.runCatching {
            EncryptedSharedPreferences.create(
                PREFERENCES_FILE_NAME,
                key,
                get(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }.getOrDefault(
            get<Context>().getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
        )
    }

//	single {
//		AccountStorage(
//            androidApplication()
//                .getSystemService(Context.ACCOUNT_SERVICE).cast(),
//            get(),
//            get()
//        )
//	}
//
//	single { SharedPreferencesStorage(get()) }
//
//	single { ObjectsSharedPreferencesStorage(get(), get()) }
//
//	single { AssetsStorage(get()) }

    single { ContactsStorage(get()) }

    single { MediaStorage(get()) }
}