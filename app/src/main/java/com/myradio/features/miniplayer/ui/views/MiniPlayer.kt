package com.myradio.features.miniplayer.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.myradio.databinding.MiniPlayerBinding
import com.myradio.features.miniplayer.utils.RadioManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MiniPlayer@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
):ConstraintLayout(context, attrs, defStyleAttr) {

    @Inject
    lateinit var playerManager: RadioManager
    private var binding: MiniPlayerBinding

    fun getData(curChannelName:String?,curChannelImage:String?,curChannelUri:String?){
        //update UI
        binding.channelImg.load(curChannelImage)
        binding.channelNameTxt.text = curChannelName

        if (curChannelUri != null) {
            playerManager.initializePlayer(curChannelUri,context)
        }


        //play URI using exoplayer
    }

    fun updateStatus(curChannelStatus:String){
        binding.statusTxt.text = curChannelStatus
    }


    init {

        // get the inflater service from the android system
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // inflate the layout into "this" component
        binding= MiniPlayerBinding.inflate(inflater, this,true)
    }

}