package net.projecttl.p.x32.api.util

import kotlinx.coroutines.sync.Mutex

/*
 * This class only support by kotlin
 * This feature is Experimental
 */
class AsyncTask(val loop: Boolean = true, val block: suspend () -> Unit) {
    private val memLock = Mutex()
    var isActive = true
        private set

    suspend fun run() {
        do {
            if (memLock.isLocked) {
                if (!isActive) {
                    break
                }

                continue
            }

            block.invoke()
        } while (loop)
    }

    suspend fun kill() {
        memLock.lock()
        isActive = false

        memLock.unlock()
    }
}

/*
 * This class only support by kotlin
 * This feature is Experimental
 */
class AsyncTaskContainer {
    private val tasks = mutableMapOf<Long, AsyncTask>()
    private var tid = 0L

    fun getTask(tid: Long): AsyncTask? {
        return tasks[tid]
    }

    fun createTask(task: AsyncTask) {
        tasks[tid] = task
        println("created task with id: $tid")

        tid++
    }

    suspend fun removeTask(tid: Long) {
        val task = getTask(tid) ?: throw NullPointerException("task $tid is missing or not defined")
        task.kill()

        tasks.remove(tid)
        println("removed task with id: $tid")
    }

    suspend fun killAll() {
        tasks.forEach { (k, t) ->
            t.kill()
            tasks.remove(k)
            println("removed task with id: $tid")
        }
    }
}