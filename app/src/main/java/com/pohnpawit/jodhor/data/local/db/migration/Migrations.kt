package com.pohnpawit.jodhor.data.local.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE photos ADD COLUMN sortOrder INTEGER NOT NULL DEFAULT 0")
        db.execSQL("UPDATE photos SET sortOrder = id")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE dorms ADD COLUMN securityDeposit INTEGER")
        db.execSQL("ALTER TABLE dorms ADD COLUMN advancePayment INTEGER")
        db.execSQL("ALTER TABLE dorms ADD COLUMN contractYears INTEGER")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `phone_numbers` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `dormId` INTEGER NOT NULL,
                `number` TEXT NOT NULL,
                `note` TEXT NOT NULL,
                FOREIGN KEY(`dormId`) REFERENCES `dorms`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL(
            "CREATE INDEX IF NOT EXISTS `index_phone_numbers_dormId` ON `phone_numbers` (`dormId`)"
        )

        db.execSQL(
            """
            INSERT INTO phone_numbers (dormId, number, note)
            SELECT id, contactPhone, '' FROM dorms WHERE contactPhone != ''
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `dorms_new` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `name` TEXT NOT NULL,
                `address` TEXT NOT NULL,
                `priceMonthly` INTEGER,
                `securityDeposit` INTEGER,
                `advancePayment` INTEGER,
                `contractYears` INTEGER,
                `mapUrl` TEXT NOT NULL,
                `notes` TEXT NOT NULL,
                `status` TEXT NOT NULL,
                `isFavorite` INTEGER NOT NULL,
                `isFull` INTEGER NOT NULL,
                `createdAt` INTEGER NOT NULL,
                `viewedAt` INTEGER
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO dorms_new (id, name, address, priceMonthly, securityDeposit, advancePayment,
                contractYears, mapUrl, notes, status, isFavorite, isFull, createdAt, viewedAt)
            SELECT id, name, address, priceMonthly, securityDeposit, advancePayment,
                contractYears, mapUrl, notes,
                CASE WHEN status = 'PLANNED' THEN 'NOT_CONTACTED' ELSE status END,
                isFavorite, 0, createdAt, viewedAt
            FROM dorms
            """.trimIndent()
        )
        db.execSQL("DROP TABLE dorms")
        db.execSQL("ALTER TABLE dorms_new RENAME TO dorms")
    }
}
