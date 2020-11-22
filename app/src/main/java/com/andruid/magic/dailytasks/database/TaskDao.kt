package com.andruid.magic.dailytasks.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM tasks ORDER BY status, time DESC")
    fun getTasks(): PagingSource<Int, Task>

    @Query("UPDATE tasks SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: Int)

    @Query("SELECT COUNT(*) FROM tasks WHERE time >= :from AND time <= :to")
    fun getTotalTasksCount(from: Long, to: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE status = :status AND time >= :from AND time <= :to")
    fun getStatusTasksCount(status: Int, from: Long, to: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE status = :status")
    fun getStatusTasksCount(status: Int): Flow<Int>

    @Query("SELECT * FROM tasks WHERE status = 1 AND title LIKE '%' || :query || '%'")
    fun searchCompletedTasks(query: String): PagingSource<Int, Task>
}