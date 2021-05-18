package com.desireProj.ble_sdk.database
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues


private const val DATABASE_NAME = "DESIRE DATABASE"
private const val DATABASE_VERSION = 1
private const val TABLE_RTL = "RTL"
private const val TABLE_ETL = "ETL"
private const val COL_PET = "pet"
private const val COL_DAY = "day"
private const val COL_TIME = "time"

class DataBaseHandler(context: Context):SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_RTL = "CREATE TABLE if not exists $TABLE_RTL ($COL_PET VARCHAR(64) PRIMARY KEY);"
        // INTEGER in sqlite save values of 8 bytes, same as Long in Kotlin/Java
        val CREATE_TABLE_ETL = "CREATE TABLE if not exists $TABLE_ETL ($COL_PET VARCHAR(64) PRIMARY KEY, " +
                "$COL_DAY DATE, $COL_TIME INTEGER);"
        db!!.execSQL(CREATE_TABLE_RTL)
        db!!.execSQL(CREATE_TABLE_ETL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*
    * Creating a RTL item
    */
    fun insertRtlItem(rtl: RTLItem) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COL_PET, rtl.pet)

        // insert row
        db.insert(TABLE_ETL, null, values)
    }

    /*
    * get RTL list
    */
    fun getRtlItems() :ArrayList<RTLItem>{
        val rtlList = ArrayList<RTLItem>()
        val selectAllQuery = "SELECT * FROM $TABLE_RTL"
        val db = this.readableDatabase

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

        return(rtlList)
    }

    /*
    * empty RTL table without deleting it
    */
    fun emptyRtlTable() {
        val db = this.writableDatabase
        val emptyTableQuery = "DELETE FROM $TABLE_RTL"

        // empty RTL table
        db.execSQL(emptyTableQuery)
    }

    /*
    * Creating a ETL item
    */
    fun insertEtlItem(etl: ETLItem) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COL_PET, etl.pet)
        values.put(COL_DAY, etl.day)
        values.put(COL_TIME, etl.time)

        // insert row
        db.insert(TABLE_ETL, null, values)
    }

    /*
    * get ETL list
    */
    fun getEtlItems() :ArrayList<ETLItem>{
        val etlList = ArrayList<ETLItem>()
        val selectAllQuery = "SELECT * FROM $TABLE_ETL"
        val db = this.readableDatabase

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

        return(etlList)
    }

    /*
    * empty ETL table without deleting it
    */
    fun emptyEtlTable() {
        val db = this.writableDatabase
        val emptyTableQuery = "DELETE FROM $TABLE_ETL"

        // empty ETL table
        db.execSQL(emptyTableQuery)
    }

    fun deleteExpiredEtlTable() {
        // TODO add day threshold to delete before it

        val db = this.writableDatabase
        db.delete(
            TABLE_ETL, COL_DAY + " = ?",
            arrayOf("")
        )
    }

}