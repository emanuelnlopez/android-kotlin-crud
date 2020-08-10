package ar.com.emanuellopez.android.crud.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {

    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber): Long

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber): Int

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber): Int

    @Query(value = "delete from subscriber_data_table")
    suspend fun deleteAll(): Int

    @Query(value = "select * from subscriber_data_table")
    fun getAllSubscribers(): LiveData<List<Subscriber>>
}