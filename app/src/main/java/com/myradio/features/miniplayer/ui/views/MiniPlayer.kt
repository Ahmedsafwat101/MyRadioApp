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
import com.myradio.features.miniplayer.utils.RadioManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "MiniPlayer"

@AndroidEntryPoint
class MiniPlayer@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
):ConstraintLayout(context, attrs, defStyleAttr) {


    @Inject
    lateinit var playerManager: RadioManager
    private var binding: MiniPlayerBinding
    var playOrPauseBtn:ImageButton



    fun getData(curChannelName:String?,curChannelImage:String?,curChannelUri:String?){
        //update UI
        binding.channelImg.load(curChannelImage)
        binding.channelNameTxt.text = curChannelName

        //play URI using exoplayer

        if (curChannelUri != null) {
            playerManager.initializePlayer(curChannelUri)
            binding.exoPause.setImageResource(R.drawable.play)
        }
    }

    private fun updateStatus(state:String){
        binding.statusTxt.text =  state
        Log.d(TAG,state)
    }


    fun playOrPause(){
        if(playerManager.player.playWhenReady) {
            playerManager.pause()
            binding.exoPause.setImageResource(R.drawable.pause)
        }else{
            playerManager.play()
            binding.exoPause.setImageResource(R.drawable.play)
        }
    }

    init {
        // get the inflater service from the android system
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // inflate the layout into "this" component
        binding= MiniPlayerBinding.inflate(inflater, this,true)
        playOrPauseBtn = binding.exoPause

    }

}