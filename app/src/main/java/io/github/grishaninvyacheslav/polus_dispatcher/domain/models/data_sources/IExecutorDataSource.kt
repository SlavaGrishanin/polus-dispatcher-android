package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.data_sources

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.ExecutorEntity

interface IExecutorDataSource {
    fun getExecutor(executorId: String): ExecutorEntity {
        Thread.sleep(2000)
        return ExecutorEntity(
            executorId = "146d48e4-2ed0-40db-88dc-56cfcc98e263",
            name = "Енгуш Лорпав",
            login = "78005553535",
            lon = "59.865000", lat = "92.915556",
            status = "тс-движется-к-заказу",
            roles = listOf("Погрузчик_Вилочный_Диз_3т/6м", "Погрузчик_Телескопический"),
            jobId = "b8efa17c-d478-4f48-b4a5-077e8f3edb1f"
        )
    }

    fun updateExecutor(executorId: String, profile: ExecutorEntity){
        Thread.sleep(2000)
    }
}