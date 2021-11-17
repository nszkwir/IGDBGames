package com.spitzer.igdbgames.ui.video

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.spitzer.igdbgames.databinding.VideoFragmentBinding

class VideoFragment : Fragment() {

    private var _binding: VideoFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VideoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {

        binding.gameVideoProgressBar.visibility = View.VISIBLE

        val mediaControls = MediaController(context)
        mediaControls.setAnchorView(binding.gameVideo)

        binding.gameVideo.apply {
            setVideoURI(
                Uri.parse(
                    //"http://distribution.bbb3d.renderfarming.net/video/mp4/bbb_sunflower_1080p_30fps_normal.mp4"
                "http://www.demonuts.com/Demonuts/smallvideo.mp4"
                )
            )
            setMediaController(mediaControls)
            setOnPreparedListener { mediaPlayer ->
                mediaPlayer.isLooping = true
                binding.gameVideoProgressBar.visibility = View.GONE
                binding.gameVideo.start()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
