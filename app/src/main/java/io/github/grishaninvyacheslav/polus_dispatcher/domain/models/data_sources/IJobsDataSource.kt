package io.github.grishaninvyacheslav.polus_dispatcher.domain.models.data_sources

import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.Customer
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.JobEntity
import io.github.grishaninvyacheslav.polus_dispatcher.domain.entities.RequiredVehicle

interface IJobsDataSource {
    fun isExecutorJobsUpdated(executorId: String): Boolean {
        return false
    }

    fun executorJobs(executorId: String): List<JobEntity> {
        Thread.sleep(2000)
        return listOf(
            JobEntity(
                jobId = "b8efa17c-d478-4f48-b4a5-077e8f3edb1f",
                startDate = "1667120400",
                endDate = "1667149200",
                status = "planned",
                lon = "30.346379",
                lat = "59.934262",
                customer = Customer(
                    "d478-b8efa17c-4f48-b4a5-077e8f3edb1f",
                    "78005553535",
                    "Йцукен Фыва"
                ),
                requiredVehicle = RequiredVehicle(
                    characteristic = "Погрузчик_Вилочный_Диз_3т/6м",
                    model = "KOMATSU FD30T-17"
                ),
                executorId = "146d48e4-2ed0-40db-88dc-56cfcc98e263",
                title = "Заказ на Самосвал для работ на Карьере номер 9"
            ),
            JobEntity(
                jobId = "73f5886e-363f-4cfc-ba99-8fdf5f89c924",
                startDate = "1666688400",
                endDate = "1666717200",
                status = "planned",
                lon = "42.035278",
                lat = "56.868903",
                customer = Customer(
                    "d478-b8efa17c-4f48-b4a5-077e8f3edb1f",
                    "78005553535",
                    "Йцукен Фыва"
                ),
                requiredVehicle = RequiredVehicle(
                    characteristic = "Погрузчик_Вилочный_Диз_3т/6м",
                    model = "KOMATSU FD30T-17"
                ),
                executorId = "146d48e4-2ed0-40db-88dc-56cfcc98e263",
                title = "Заказ на Самосвал для работ на Карьере номер 9"
            ),
            JobEntity(
                jobId = "73f5886e-363f-4cfc-ba99-8fdf5f89c924",
                startDate = "1666947600",
                endDate = "1666976400",
                status = "planned",
                lon = "-0.125603",
                lat = "51.501171",
                customer = Customer(
                    "d478-b8efa17c-4f48-b4a5-077e8f3edb1f",
                    "78005553535",
                    "Йцукен Фыва"
                ),
                requiredVehicle = RequiredVehicle(
                    characteristic = "Погрузчик_Вилочный_Диз_3т/6м",
                    model = "KOMATSU FD30T-17"
                ),
                executorId = "146d48e4-2ed0-40db-88dc-56cfcc98e263",
                title = "Заказ на Самосвал для работ на Карьере номер 9"
            )
        )
    }
}