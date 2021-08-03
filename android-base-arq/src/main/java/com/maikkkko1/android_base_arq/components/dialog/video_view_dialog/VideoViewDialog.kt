package com.maikkkko1.android_base_arq.components.dialog.video_view_dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.maikkkko1.android_base_arq.databinding.DialogVideoViewBinding

class VideoViewDialog : DialogFragment() {
    private var binding: DialogVideoViewBinding? = null

    private var exoPlayer: SimpleExoPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return DialogVideoViewBinding.inflate(inflater, container, false).let {
            binding = it
            binding?.lifecycleOwner = this
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoUrl = arguments?.getString(VIDEO_URL_KEY) ?: ""

        with(SimpleExoPlayer.Builder(requireContext()).build()) {
            exoPlayer = this

            binding?.playerView?.player = this

            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            play()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        exoPlayer?.release()
    }

    companion object {
        private const val VIDEO_URL_KEY = "videoUrl"

        fun newInstance(videoUrl: String) = VideoViewDialog().apply {
            arguments = Bundle().apply { putString(VIDEO_URL_KEY, videoUrl) }
        }
    }
}