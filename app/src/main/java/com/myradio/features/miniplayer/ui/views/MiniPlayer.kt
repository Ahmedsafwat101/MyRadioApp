package com.myradio.features.miniplayer.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.myradio.R
import com.myradio.databinding.MiniPlayerBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "MiniPlayer"

@AndroidEntryPoint
class MiniPlayer@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
):ConstraintLayout(context, attrs, defStyleAttr) {



    private var binding: MiniPlayerBinding
    var playOrPauseBtn:ImageButton



    fun getData(curChannelName:String?,curChannelImage:String?,curChannelUri:String?){
        //update UI
        binding.channelImg.load(curChannelImage)
        binding.channelNameTxt.text = curChannelName

        play()
    }

    fun play(){
        playOrPauseBtn.setImageResource(R.drawable.pause)
    }

    fun pause(){
        playOrPauseBtn.setImageResource(R.drawable.play)
    }
    init {
        // get the inflater service from the android system
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // inflate the layout into "this" component
        binding= MiniPlayerBinding.inflate(inflater, this,true)
        playOrPauseBtn = binding.exoPause

    }

}