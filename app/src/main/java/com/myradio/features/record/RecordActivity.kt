package com.myradio.features.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.myradio.R
import com.myradio.utils.FileBuilderManager.writeBitmapToFile

private const val TAG = "RecordActivity"
class RecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        var byteArray:ByteArray =  ByteArray(10)
        byteArray[0] = 4;
        byteArray[1] = 4;
        byteArray[2] = 4;
        byteArray[3] = 4;





        try {
            writeBitmapToFile(this.applicationContext,byteArray)

            Log.i(TAG,"Yeah :)"+byteArray.toString());

        }catch (throwable:Throwable){
            Log.i(TAG,"screwed Up");
        }
    }
}