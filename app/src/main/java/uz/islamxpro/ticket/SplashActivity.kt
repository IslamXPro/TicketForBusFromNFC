package uz.islamxpro.ticket

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import uz.islamxpro.ticket.databinding.ActivitySplashBinding
import uz.islamxpro.utils.Back.isHome

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var handler: Handler

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler()

        handler.postDelayed({
            val anim = AnimationUtils.loadAnimation(this,R.anim.anim_bus)
            binding.busImg.startAnimation(anim)
            binding.progress.progress = 42
            binding.progTxt.text = "42%"
            handler.postDelayed({
                val anim1 = AnimationUtils.loadAnimation(this,R.anim.anim_bus1)
                binding.busImg.startAnimation(anim1)
                binding.progress.progress = 65
                binding.progTxt.text = "65%"
                handler.postDelayed({
                    val anim2 = AnimationUtils.loadAnimation(this,R.anim.anim_bus2)
                    binding.busImg.startAnimation(anim2)
                    handler.postDelayed({
                        binding.progress.progress = 93
                        binding.progTxt.text = "93%"
                        startActivity(Intent(this,MainActivity::class.java))
                    },300)
                },1000)
            },600)
        },600)

    }

    override fun onStart() {
        super.onStart()
        isHome = false
    }
}