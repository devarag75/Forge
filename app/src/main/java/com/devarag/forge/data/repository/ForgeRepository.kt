package com.devarag.forge.data.repository

import com.devarag.forge.data.dao.FocusSessionDao
import com.devarag.forge.data.dao.TaskDao
import com.devarag.forge.data.entity.FocusSession
import com.devarag.forge.data.entity.Task

class ForgeRepository(
    private val taskDao: TaskDao,
    private val focusSessionDao: FocusSessionDao
) {

    val tasks = taskDao.getAllTasks()

    val sessions = focusSessionDao.getAllSessions()

    suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun addSession(session: FocusSession) {
        focusSessionDao.insertSession(session)
    }
}