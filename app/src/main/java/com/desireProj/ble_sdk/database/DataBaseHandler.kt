package com.desireProj.ble_sdk.database
import android.content.ContentValues
import android.os.Build
import androidx.annotation.RequiresApi
import com.desireProj.ble_sdk.model.Utilities
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


private const val DATABASE_NAME = "DESIRE DATABASE"
private const val DATABASE_PASSKEY = "PASS KEY"
private const val DATABASE_VERSION = 1
private const val TABLE_RTL = "RTL"
private const val TABLE_ETL = "ETL"
private const val COL_PET = "pet"
private const val COL_DAY = "day"
private const val COL_TIME = "time"
private const val COL_RSSI = "rssi"

private const val CREATE_TABLE_RTL = "CREATE TABLE if not exists $TABLE_RTL ($COL_PET VARCHAR(64) PRIMARY KEY, " +
        "$COL_DAY DATE);"
// INTEGER in sqlite save values of 8 bytes, same as Long in Kotlin/Java
private const val CREATE_TABLE_ETL = "CREATE TABLE if not exists $TABLE_ETL ($COL_PET VARCHAR(64) PRIMARY KEY, " +
        "$COL_DAY DATE, $COL_TIME INTEGER, $COL_RSSI INTEGER);"

private const val DELETE_TABLE_RTL = "DROP TABLE if exists $TABLE_RTL;"
private const val DELETE_TABLE_ETL = "DROP TABLE if exists $TABLE_ETL;"

// TODO pass context to Utilities class

@RequiresApi(Build.VERSION_CODES.M)
object DataBaseHandler:
    SQLiteOpenHelper(Utilities.context,DATABASE_NAME,null,DATABASE_VERSION) {

    private var passKey: PassKeyEncDec? = null

    init {
        passKey = PassKeyEncDec
        initiate()
    }

    /*
        used to create the database and set its password, as onCreate is not invoked
        until the first read or write operation
     */
    private fun initiate() {
        if (passKey!!.loadEncryptedPassword() == null) {
            SQLiteDatabase.loadLibs(Utilities.context)
            passKey!!.initiate()
            val db = this.getReadableDatabase(passKey!!.getPasswordString())
            db.close()
        }
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
        val db = this.getWritableDatabase(passKey!!.getPasswordString())

        val values = ContentValues()
        values.put(COL_PET, rtl.pet)
        values.put(COL_DAY, rtl.day)

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
        val db = this.getReadableDatabase(passKey!!.getPasswordString())

        val cursor = db.rawQuery(selectAllQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val pet: String = cursor.getString(cursor.getColumnIndex(COL_PET))
                val day: String = cursor.getString(cursor.getColumnIndex(COL_DAY))
                val rtl = RTLItem(pet, day)
                // adding to rtl list
                rtlList.add(rtl)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return(rtlList)
    }

    /*
    * empty RTL table without deleting it
    */
    fun emptyRtlTable() {
        SQLiteDatabase.loadLibs(Utilities.context)

        val db = this.getWritableDatabase(passKey!!.getPasswordString())
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
        val db = this.getWritableDatabase(passKey!!.getPasswordString())

        val values = ContentValues()
        values.put(COL_PET, etl.pet)
        values.put(COL_DAY, etl.day)
        values.put(COL_TIME, etl.time)
        values.put(COL_RSSI, etl.rssi)

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
        val db = this.getReadableDatabase(passKey!!.getPasswordString())

        val cursor = db.rawQuery(selectAllQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val pet = cursor.getString(cursor.getColumnIndex(COL_PET))
                val day = cursor.getString(cursor.getColumnIndex(COL_DAY))
                val time = cursor.getLong(cursor.getColumnIndex(COL_TIME)) // TODO check if working well
                val rssi = cursor.getLong(cursor.getColumnIndex(COL_RSSI))
                var etl = ETLItem(pet, day, time, rssi.toInt())
                // adding to rtl list
                etlList.add(etl)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return(etlList)
    }

    /*
    * empty ETL table without deleting it
    */
    fun emptyEtlTable() {
        SQLiteDatabase.loadLibs(Utilities.context)
        val db = this.getWritableDatabase(passKey!!.getPasswordString())
        val emptyTableQuery = "DELETE FROM $TABLE_ETL"

        // empty ETL table
        db.execSQL(emptyTableQuery)
        db.close()
    }

    /*
        delete pets with date before 14 days from the given table
     */
    fun deleteExpiredPets(table: String) {
        SQLiteDatabase.loadLibs(Utilities.context)

        val threshold: String = getExpirationDate()

        val db = this.getWritableDatabase(passKey!!.getPasswordString())
        db.delete(
            table, "$COL_DAY < ?",
            arrayOf(threshold)
        )

        db.close()
    }

    /*
        return expiration date of the pets
     */
    private fun getExpirationDate() :String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -14)
        val date = sdf.format(cal.time)

        return (date)
    }

    fun updatePassword() {
        // TODO check if needed
    }

    fun clearDatabase() {
        emptyRtlTable()
        emptyEtlTable()
    }

}