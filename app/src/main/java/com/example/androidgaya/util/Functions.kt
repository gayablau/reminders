package com.example.androidgaya.util

import com.example.androidgaya.repositories.models.ReminderEntity
import com.example.androidgaya.repositories.models.UserEntity
import com.example.androidgaya.repositories.types.ReminderJson
import com.example.androidgaya.repositories.types.UserJson

class Functions {
    fun jsonToRemEntity(reminderJson: ReminderJson): ReminderEntity {
        return ReminderEntity(reminderJson.id,
                reminderJson.header,
                reminderJson.description,
                reminderJson.user,
                reminderJson.time,
                reminderJson.createdAt)
    }

    fun jsonToUserEntity(userJson: UserJson): UserEntity {
        return UserEntity(userJson.userId, userJson.username)
    }

    fun remEntityToJson(reminderEntity: ReminderEntity): ReminderJson {
        return ReminderJson(reminderEntity.id,
                reminderEntity.header,
                reminderEntity.description,
                reminderEntity.user,
                reminderEntity.time,
                reminderEntity.createdAt)
    }

    fun userEntityToJson(userEntity: UserEntity): UserJson {
        return UserJson(userEntity.id, userEntity.username)
    }
}