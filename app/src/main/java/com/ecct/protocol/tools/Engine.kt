package com.ecct.protocol.tools

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ecct.protocol.contracts.LoggerContract
import com.ecct.protocol.ble.BleAdvertiser
import com.ecct.protocol.ble.BleScanner
import com.ecct.protocol.database.*
import com.ecct.protocol.diffieHellman.KeyExchanger
import com.ecct.protocol.model.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


object Engine {
    private lateinit var bleScanner: BleScanner
    private lateinit var bleAdvertiser: BleAdvertiser
    private lateinit var keyExchanger: KeyExchanger
    private lateinit var collectedEbid: CollectedEbid
    private lateinit var loggerDataList: LoggerDataList
    private lateinit var loggerPresenter: LoggerContract.LoggerPresenter
    // TODO to be private
    lateinit var collectedPets: CollectedPets
    private lateinit var dataBaseHandler: DataBaseHandler

    
    init {
        collectedEbid = CollectedEbid(this)
        collectedPets = CollectedPets(this)
        keyExchanger = KeyExchanger(this)
        dataBaseHandler = DataBaseHandler

        loggerDataList = LoggerDataList(this)

        bleScanner = BleScanner(this)
        bleAdvertiser = BleAdvertiser()
    }
    fun generateNewKey(){
        keyExchanger.generateNewKeys()
    }
    fun startAdvertising(){
        bleAdvertiser.startAdvertising(keyExchanger.publicKeyByteArray!!)
    }
    fun stopAdvertising(){
        bleAdvertiser.stopAdvertising()
    }
    fun startScanning(){
        bleScanner.startScanning()
    }
    fun stopScanning(){
        bleScanner.stopScanning()
    }

    fun receiveEbid(dataReceived: ByteArray, rssi: Int) {
        collectedEbid.receiveEbid(dataReceived, rssi)
    }

    fun getPublicKey(): ByteArray? {
        return (keyExchanger.publicKeyByteArray)
    }

    fun generateSecret(received: ByteArray):ByteArray {
        return (keyExchanger.generateSecret(received))
    }

    fun generatePet(ebid: EbidReceived) {
        collectedPets.receivedPet(ebid)
    }

    fun addToLogger(petVal: String) {
        Log.e("Engine: addToLogger: ", "petVal = $petVal")
        loggerPresenter.onPetsValueReceived(petVal)
    }

    fun clearEbidMap() {
        this.collectedEbid.clearMap()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun sendPetsToDatabase() {
        this.collectedPets.sendPetsToDatabase(dataBaseHandler)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun removeExpiredPetsFromDatabase() {
        this.dataBaseHandler.removeExpiredPets()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun updateDatabasePassword() {
        this.dataBaseHandler.updatePassword()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getRTLList(): List<String> {
        val rtlList:MutableList<String> = mutableListOf()
        for(rtlItem in this.dataBaseHandler.getRtlItems()){
            val uploadedRTL:String = rtlItem.pet
            rtlList.add(uploadedRTL)
        }
        return rtlList
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getETLList(uploadDate:String): List<UploadETL> {
        val etlList:MutableList<UploadETL> = mutableListOf()
        for(etlItem in this.dataBaseHandler.getEtlItems()){
            val uploadedETL:UploadETL = UploadETL(etlItem.pet,etlItem.day,etlItem.duration
            ,etlItem.rssi,uploadDate)
            etlList.add(uploadedETL)
        }
        return etlList
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getETLTable(): ArrayList<ETLItem> {
        return(dataBaseHandler.getEtlItems())
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getRTLTable(): ArrayList<RTLItem> {
        return(dataBaseHandler.getRtlItems())
    }

    fun setLoggerPresenter(loggerPresenter:LoggerContract.LoggerPresenter? ){
        this.loggerPresenter = loggerPresenter!!
    }
    fun currentDate():String{
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val cal = Calendar.getInstance()
        val date = sdf.format(cal.time)
        return date;
    }
}
