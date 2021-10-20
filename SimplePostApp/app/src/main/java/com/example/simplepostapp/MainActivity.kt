package com.example.simplepostapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull

import org.json.JSONException

import org.json.JSONObject
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class MainActivity : AppCompatActivity() {

    lateinit var InputName: EditText
    lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        InputName = findViewById(R.id.edName)
        btn = findViewById(R.id.btn)

        btn.setOnClickListener {
            if(InputName.text.isNotEmpty()){
                addNewName(InputName.text.toString())

            }else{
                Toast.makeText(this,"please enter a name",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addNewName(name: String) {

        CoroutineScope(IO).launch {

            val jsonObject = JSONObject()
            try {
                jsonObject.put("name", name)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            val client = OkHttpClient()
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonObject.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url("https://dojo-recipes.herokuapp.com/custom-people/")
                .post(requestBody)
                .build()

            var response: Response? = null
            try {
                response = client.newCall(request).execute()
                if(response.code == 201){
                    withContext(Main){
                        Toast.makeText(this@MainActivity,"added successfully",Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Main){
                    Toast.makeText(this@MainActivity,"something wrong",Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
