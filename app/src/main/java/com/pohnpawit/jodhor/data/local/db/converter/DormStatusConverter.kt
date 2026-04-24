package com.pohnpawit.jodhor.data.local.db.converter

import androidx.room.TypeConverter
import com.pohnpawit.jodhor.data.model.DormStatus

class DormStatusConverter {
    @TypeConverter
    fun fromDormStatus(value: DormStatus): String = value.name

    @TypeConverter
    fun toDormStatus(value: String): DormStatus = DormStatus.valueOf(value)
}
