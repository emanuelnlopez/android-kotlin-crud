package ar.com.emanuellopez.android.crud.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {

    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber)

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber)

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber)

    @Query(value = "delete from subscriber_data_table")
    suspend fun deleteAll()

    @Query(value = "select * from subscriber_data_table")
    fun getAllSubscribers(): LiveData<List<Subscriber>>
}