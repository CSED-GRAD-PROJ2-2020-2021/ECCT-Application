package com.desireProj.ble_sdk.database
import android.content.Context
import android.content.ContentValues
import com.desireProj.ble_sdk.model.Utilities
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteOpenHelper
import java.security.*
import javax.crypto.*


private const val DATABASE_NAME = "DESIRE DATABASE"
private const val DATABASE_PASSKEY = "PASS KEY"
private const val DATABASE_VERSION = 1
private const val TABLE_RTL = "RTL"
private const val TABLE_ETL = "ETL"
private const val COL_PET = "pet"
private const val COL_DAY = "day"
private const val COL_TIME = "time"

private const val CREATE_TABLE_RTL = "CREATE TABLE if not exists $TABLE_RTL ($COL_PET VARCHAR(64) PRIMARY KEY);"
// INTEGER in sqlite save values of 8 bytes, same as Long in Kotlin/Java
private const val CREATE_TABLE_ETL = "CREATE TABLE if not exists $TABLE_ETL ($COL_PET VARCHAR(64) PRIMARY KEY, " +
        "$COL_DAY DATE, $COL_TIME INTEGER);"

private const val DELETE_TABLE_RTL = "DROP TABLE if exists $TABLE_RTL;"
private const val DELETE_TABLE_ETL = "DROP TABLE if exists $TABLE_ETL;"

// TODO pass context to Utilities class

object DataBaseHandler:
    SQLiteOpenHelper(Utilities.context,DATABASE_NAME,null,DATABASE_VERSION) {

    var pubicPassKey: ByteArray? = generatePublicKey()

    private fun generatePublicKey() :ByteArray {
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(128, SecureRandom())
        val originalKey = keygen.generateKey()
        return(originalKey.encoded)
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_RTL)
        db?.execSQL(CREATE_TABLE_ETL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(DELETE_TABLE_RTL)
        db?.execSQL(DELETE_TABLE_ETL)
        onCreate(db)
    }

    /*
    * Creating a RTL item
    */
    fun insertRtlItem(rtl: RTLItem) {
        SQLiteDatabase.loadLibs(Utilities.context)
        val db = this.getWritableDatabase(DATABASE_PASSKEY)

        val values = ContentValues()
        values.put(COL_PET, rtl.pet)

        // insert row
        db.insert(TABLE_RTL, null, values)
        db.close()
    }

    /*
    * get RTL list
    */
    fun getRtlItems() :ArrayList<RTLItem>{
        SQLiteDatabase.loadLibs(Utilities.context)

        val rtlList = ArrayList<RTLItem>()
        val selectAllQuery = "SELECT * FROM $TABLE_RTL"
        val db = this.getReadableDatabase(DATABASE_PASSKEY)

        val c = db.rawQuery(selectAllQuery, null)

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                val pet = c.getString(c.getColumnIndex(COL_PET))
                var rtl = RTLItem(pet)
                // adding to rtl list
                rtlList.add(rtl)
            } while (c.moveToNext())
        }

        c.close()
        db.close()

        return(rtlList)
    }

    /*
    * empty RTL table without deleting it
    */
    fun emptyRtlTable() {
        SQLiteDatabase.loadLibs(Utilities.context)

        val db = this.getWritableDatabase(DATABASE_PASSKEY)
        val emptyTableQuery = "DELETE FROM $TABLE_RTL"

        // empty RTL table
        db.execSQL(emptyTableQuery)
        db.close()
    }

    /*
    * Creating a ETL item
    */
    fun insertEtlItem(etl: ETLItem) {
        SQLiteDatabase.loadLibs(Utilities.context)
        val db = this.getWritableDatabase(DATABASE_PASSKEY)

        val values = ContentValues()
        values.put(COL_PET, etl.pet)
        values.put(COL_DAY, etl.day)
        values.put(COL_TIME, etl.time)

        // insert row
        db.insert(TABLE_ETL, null, values)
        db.close()
    }

    /*
    * get ETL list
    */
    fun getEtlItems() :ArrayList<ETLItem>{
        SQLiteDatabase.loadLibs(Utilities.context)

        val etlList = ArrayList<ETLItem>()
        val selectAllQuery = "SELECT * FROM $TABLE_ETL"
        val db = this.getReadableDatabase(DATABASE_PASSKEY)

        val c = db.rawQuery(selectAllQuery, null)

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                val pet = c.getString(c.getColumnIndex(COL_PET))
                val day = c.getString(c.getColumnIndex(COL_DAY))
                val time = c.getLong(c.getColumnIndex(COL_TIME)) // TODO check if working well
                var etl = ETLItem(pet, day, time)
                // adding to rtl list
                etlList.add(etl)
            } while (c.moveToNext())
        }

        c.close()
        db.close()

        return(etlList)
    }

    /*
    * empty ETL table without deleting it
    */
    fun emptyEtlTable() {
        SQLiteDatabase.loadLibs(Utilities.context)
        val db = this.getWritableDatabase(DATABASE_PASSKEY)
        val emptyTableQuery = "DELETE FROM $TABLE_ETL"

        // empty ETL table
        db.execSQL(emptyTableQuery)
        db.close()
    }

    fun deleteExpiredEtlTable() {
        SQLiteDatabase.loadLibs(Utilities.context)
        // TODO add day threshold to delete before it

        val db = this.getWritableDatabase(DATABASE_PASSKEY)
        db.delete(
            TABLE_ETL, COL_DAY + " = ?",
            arrayOf("")
        )


        db.close()
    }




}