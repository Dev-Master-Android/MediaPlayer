package com.example.mediaplayer

import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.mediaplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private var songList = mutableListOf(R.raw.android, R.raw.mist, R.raw.dino)
    private var currentSongIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backgroundAnimation(binding.linearLayout)
        playSound(songList[0])
    }

    private fun playSound(song: Int) {

        binding.playButton.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, song)
                initializeSeekBar()
            }
            mediaPlayer?.start()
        }

        binding.pauseButton.setOnClickListener {
            if (mediaPlayer != null) {
                mediaPlayer?.pause()
            }
        }

        binding.stopButton.setOnClickListener {
            if (mediaPlayer != null) {
                mediaPlayer?.stop()
                mediaPlayer?.reset()
                mediaPlayer?.release()
                mediaPlayer = null
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        binding.nextButton.setOnClickListener {
            currentSongIndex = (currentSongIndex + 1) % songList.size
            mediaPlayer?.reset()
            mediaPlayer = MediaPlayer.create(this, songList[currentSongIndex])
            initializeSeekBar()
            mediaPlayer?.start()
        }

        binding.previousButton.setOnClickListener {
            currentSongIndex = if (currentSongIndex - 1 < 0) songList.size - 1 else currentSongIndex - 1
            mediaPlayer?.reset()
            mediaPlayer = MediaPlayer.create(this, songList[currentSongIndex])
            initializeSeekBar()
            mediaPlayer?.start()
        }

        binding.volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val volume = progress / 100f
                    mediaPlayer?.setVolume(volume, volume)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

    }
    private fun initializeSeekBar() {
        binding.seekBar.max = mediaPlayer!!.duration

        val handler = android.os.Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seekBar.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                }catch (e: Exception){
                    binding.seekBar.progress = 0
                }
            }
        }, 0)
    }
    private fun backgroundAnimation(layout: LinearLayout) {
        val animation : AnimationDrawable = layout.background as AnimationDrawable
        animation.apply {
            setEnterFadeDuration(1000)
            setExitFadeDuration(3000)
            start()
        }
    }
}