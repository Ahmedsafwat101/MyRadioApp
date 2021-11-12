package com.myradio.features.miniplayer.ui.views

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.myradio.R
import com.myradio.databinding.MiniPlayerBinding
import dagger.hilt.android.AndroidEntryPoint
import android.os.Parcel
import androidx.annotation.RequiresApi


private const val TAG = "MiniPlayer"


@AndroidEntryPoint
class MiniPlayer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    private var binding: MiniPlayerBinding
    var playOrPauseBtn: ImageButton

    var channelName: String? = null
    var channelImage: String? = null
    var isPlaying:Boolean = false



    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()

        // Wrap our super class's state with our own.
        val myState = SavedState(superState)
        myState.channelImage = this.channelImage
        myState.channelName = this.channelName
        myState.isPlaying = this.isPlaying

        // Return our state along with our super class's state.
        return myState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState

        // Let our super class process state before we do because we should
        // depend on our super class, we shouldn't imply that our super class
        // might need to depend on us.
        super.onRestoreInstanceState(savedState.superState)


        // Grab our properties out of our SavedState.
        this.channelImage = savedState.channelImage
        this.channelName = savedState.channelName
        this.isPlaying = savedState.isPlaying


        // Update our visuals in whatever way we want, like...
        //requestLayout()

        //invalidate()
        binding.channelImg.load(this.channelImage)
        binding.channelNameTxt.text = this.channelName
        if(this.isPlaying)
            playOrPauseBtn.setImageResource(R.drawable.pause)
        else
            playOrPauseBtn.setImageResource(R.drawable.play)



    }


    fun getData(curChannelName: String?, curChannelImage: String?, curChannelUri: String?) {
        Log.d(TAG, "SSSS")

        channelImage = curChannelImage
        channelName = curChannelName


        binding.channelImg.load(curChannelImage)
        binding.channelNameTxt.text = curChannelName

        play()
    }

    fun play() {
        isPlaying = true
        playOrPauseBtn.setImageResource(R.drawable.pause)
    }

    fun pause() {
        isPlaying = false
        playOrPauseBtn.setImageResource(R.drawable.play)
    }



    init {
        // get the inflater service from the android system
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // inflate the layout into "this" component
        binding = MiniPlayerBinding.inflate(inflater, this, true)
        playOrPauseBtn = binding.exoPause
        isSaveEnabled = true



    }

    private  class SavedState : BaseSavedState {
        var channelName: String? = null
        var channelImage: String? = null
        var isPlaying:Boolean = false

        constructor(superState: Parcelable?) : super(superState)

        @RequiresApi(Build.VERSION_CODES.Q)
        private constructor(`in`: Parcel) : super(`in`) {
            channelName = `in`.readString()
            channelImage = `in`.readString()
            isPlaying = `in`.readBoolean()
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(channelName)
            out.writeString(channelImage)
            out.writeBoolean(isPlaying)

        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState?> = object : Parcelable.Creator<SavedState?> {
                @RequiresApi(Build.VERSION_CODES.Q)
                override fun createFromParcel(`in`: Parcel): SavedState? {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

}


