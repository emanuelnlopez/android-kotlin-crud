package ar.com.emanuellopez.android.crud

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.emanuellopez.android.crud.db.Subscriber
import ar.com.emanuellopez.android.crud.db.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository): ViewModel(), Observable {

    val subscribers= repository.subscribers

    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber

    @Bindable
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()
    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()
    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    private fun resetForm() {
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate() {
        if(inputName.value == null) {
            statusMessage.value = Event("Please enter subscriber's name")
        } else if(inputEmail.value == null) {
            statusMessage.value = Event("Please enter subscriber's email")
        } else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value).matches()) {
            statusMessage.value = Event("Please enter a valid email address")
        } else {
            if (isUpdateOrDelete) {
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value !!
                update(subscriberToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!

                insert(Subscriber(0, name, email))
            }

            inputName.value = null
            inputEmail.value = null
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(subscriberToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    fun insert(subscriber: Subscriber) = viewModelScope.launch {
        val newRowId = repository.insert(subscriber)
        if (newRowId > -1) {
            statusMessage.value = Event("Suscriber inserted successfully $newRowId")
        } else {
            statusMessage.value = Event("Error occurred!")
        }
    }

    fun update(subscriber: Subscriber) = viewModelScope.launch {
        val numberOfRowsUpdated = repository.update(subscriber)
        if (numberOfRowsUpdated > 0) {
            resetForm()
            statusMessage.value = Event("$numberOfRowsUpdated suscribers updated successfully")
        } else {
            statusMessage.value = Event("Error occurred!")
        }
    }

    fun delete(subscriber: Subscriber) = viewModelScope.launch {
        val numberOfRowsDeleted = repository.delete(subscriber)
        if (numberOfRowsDeleted > 0) {
            resetForm()
            statusMessage.value = Event("$numberOfRowsDeleted suscribers deleted successfully")
        } else {
            statusMessage.value = Event("Error occurred!")
        }
    }

    fun clearAll() = viewModelScope.launch {
        val numberOfRowsDeleted = repository.deleteAll()
        if (numberOfRowsDeleted > 0) {
            statusMessage.value = Event("$numberOfRowsDeleted suscribers deleted successfully")
        } else {
            statusMessage.value = Event("Error occurred!")
        }
    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}