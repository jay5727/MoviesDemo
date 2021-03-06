package com.jay.moviesdemo.api

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.jay.moviesdemo.executor.AppExecutors
import com.jay.moviesdemo.model.NetworkResponseModel

import timber.log.Timber

/**
 * NetworkBoundResource which use LiveData
 * Checks if data exist in DB
 * 1)If not ,fetch from server & dump in DB & then return from DB as LiveData
 * 2)If present return as is LiveData¬
 */
abstract class NetworkBoundResource<ResultType,
        RequestType : NetworkResponseModel>
internal constructor() {

    /**
     * The final result LiveData
     */
    private val result: MediatorLiveData<Resource<ResultType>> = MediatorLiveData()

    init {
        Timber.d("Injection NetworkBoundRepository")
        AppExecutors.diskIO().execute {
            val loadedFromDB = this.loadFromDb()
            AppExecutors.mainThread().execute {
                result.addSource(loadedFromDB) { data ->
                    result.removeSource(loadedFromDB)
                    if (shouldFetch(data)) {
                        // Send loading state to UI
                        result.postValue(Resource.loading(null))
                        fetchFromNetwork(loadedFromDB)
                    } else {
                        result.addSource<ResultType>(loadedFromDB) { newData ->
                            setValue(Resource.success(newData))
                        }
                    }
                }
            }
        }
    }

    /**
     * Fetch the data from network and persist into DB and then
     * send it back to UI.
     */
    private fun fetchFromNetwork(loadedFromDB: LiveData<ResultType>) {
        val apiResponse = fetchService()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(apiResponse) { response ->
            response?.let {
                when (response.isSuccessful) {
                    true -> {
                        AppExecutors.diskIO().execute {
                            response.body?.let {
                                saveFetchData(it)
                                val loaded = loadFromDb()
                                AppExecutors.mainThread().execute {
                                    result.addSource(loaded) { newData ->
                                        newData?.let {
                                            setValue(Resource.success(newData))
                                        }
                                    }
                                }
                            }
                        }
                    }
                    false -> {
                        AppExecutors.mainThread().execute {
                            result.removeSource(loadedFromDB)
                            onFetchFailed(response.message)
                            response.message?.let {
                                result.addSource<ResultType>(loadedFromDB) { newData ->
                                    setValue(Resource.error(it, newData))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        result.value = newValue
    }

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @WorkerThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread//createCall
    protected abstract fun fetchService(): LiveData<ApiResponse<RequestType>>

    @WorkerThread
    protected abstract fun saveFetchData(items: RequestType)

    @MainThread
    protected abstract fun onFetchFailed(message: String?)
}
