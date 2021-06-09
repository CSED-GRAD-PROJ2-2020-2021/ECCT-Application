package com.desireProj.demo

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.desireProj.ble_sdk.Contracts.UploadContract
import com.desireProj.ble_sdk.Presenters.UploadPresenter
import com.desireProj.ble_sdk.R
import com.desireProj.ble_sdk.model.QueryPetsModel
import com.desireProj.ble_sdk.model.UploadPetsModel
import com.desireProj.ble_sdk.tools.Engine
import com.desireProj.ble_sdk.tools.SessionManager

class UploadActivity:AppCompatActivity(),UploadContract.UploadView {
    private lateinit var  uploadResultButton : Button
    private var uploadText : TextView? = null
    private val context : Context = this
    private lateinit var uploadPresenter: UploadPresenter
    private lateinit var engine: Engine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_activity)
        uploadText = findViewById(R.id.upload_result)
        uploadResultButton = findViewById(R.id.button_upload)
        uploadPresenter = UploadPresenter(this,context)
        engine =Engine
        uploadResultButton.setOnClickListener(object : View.OnClickListener{
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onClick(v: View?) {
                val uploadPet :UploadPetsModel = UploadPetsModel("T2",engine.getETLList(engine.currentDate()))
                uploadPresenter.uploadPets(uploadPet)
            }
        })
    }

    override fun onSuccess() {
        uploadText?.setText("uploaded")
    }
}