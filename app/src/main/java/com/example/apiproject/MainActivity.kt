package com.example.apiproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Contacts.Photo
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.codepath.asynchttpclient.RequestHeaders
import com.google.gson.Gson
import okhttp3.Headers
import kotlin.random.Random
import com.google.gson.JsonParser
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    var marsImageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val img = findViewById<ImageView>(R.id.imageView)

        getNextImg(button,img)

    }

    private fun getMarsData(textView: TextView,textView2: TextView) {
        val client = AsyncHttpClient()
        val sol = Random.nextInt(1,1000)

        client["https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=${sol}&camera=fhaz&page=1&api_key=6aWKwfuG4gmAG3dSzAz3tWGVPjACTE8DHQ7JHBc9", object :
            JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                if (json != null) {
                    val imgUrl = json.jsonObject
                    val photosArray = imgUrl.getJSONArray("photos")
                    Log.d("image", photosArray.toString())
                    Log.d("image", photosArray.length().toString())
                    if(photosArray.length().toInt()>0) {
                        val photo = Random.nextInt(0,photosArray.length())
                        textView.setText("sol: ${sol}")
                        var firstPhoto: JSONObject = photosArray.getJSONObject(photo)
                        val imgSrc = firstPhoto.getString("img_src")
                        textView2.setText("Earth Date: " + firstPhoto.getString("earth_date"))
                        marsImageUrl = imgSrc.toString()
                    }
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                t: Throwable?
            ) {
                Log.d("Photo failed to receive", errorResponse)
            }

        }]
    }

    private fun getNextImg(button: Button, imageView: ImageView)
    {
        button.setOnClickListener {
            val text1 = findViewById<TextView>(R.id.textView)
            val text2 = findViewById<TextView>(R.id.textView2)
            getMarsData(text1,text2)
            Glide.with(imageView)
                .load(marsImageUrl)
                .fitCenter()
                .into(imageView)

        }
        //Log.d("DEBUG OBJECT:",marsImageUrl)
    }
}