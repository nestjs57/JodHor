package com.pohnpawit.jodhor.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pohnpawit.jodhor.data.local.db.converter.DormStatusConverter
import com.pohnpawit.jodhor.data.local.entity.DormEntity
import com.pohnpawit.jodhor.data.local.entity.PhotoEntity

@Database(
    entities = [DormEntity::class, PhotoEntity::class],
    version = 3,
    exportSchema = true,
)
@TypeConverters(DormStatusConverter::class)
abstract class JodHorDatabase : RoomDatabase() {
    abstract fun dormDao(): DormDao
    abstract fun photoDao(): PhotoDao
}
