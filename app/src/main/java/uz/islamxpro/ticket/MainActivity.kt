package uz.islamxpro.ticket

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.*
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt
import uz.islamxpro.ticket.databinding.ActivityMainBinding
import uz.islamxpro.utils.Back.isHome


class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private lateinit var binding: ActivityMainBinding
    private var isClock: Boolean = false
    private lateinit var handler: Handler
    private var money: Int = 99

    @SuppressLint("ResourceType", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        isHome = true

        handler = Handler()
        binding.moneyTxt.text = "$money $"
        onResume()
        onStart()
    }

    @SuppressLint("MissingPermission")
    private fun vibrateFalse() {
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(300)
        }
    }

    @SuppressLint("MissingPermission")
    private fun vibrateUnlock() {
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(150)
        }
    }

    private fun checkUnlock() {
        Snackbar.make(binding.root, "Unlock \uD83D\uDD13", Snackbar.LENGTH_SHORT).show()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorr = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        Snackbar.make(
            binding.root,
            "Close proximity sensor, and wait paying process!",
            Snackbar.LENGTH_LONG
        ).show()

        if (isClock) {
            val sensorEventListener: SensorEventListener = object : SensorEventListener {
                @SuppressLint("InflateParams", "SetTextI18n")
                override fun onSensorChanged(sensorEvent: SensorEvent) {
                    if (sensorEvent.values[0] < sensorr.maximumRange) {
                        binding.progressPay.visibility = View.VISIBLE
                        handler.postDelayed({
                            vibrateUnlock()
                            binding.progressPay.visibility = View.GONE

                            money -= 3
                            binding.moneyTxt.text = "$money $"
                                val snackbar = Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG)

                                // inflate the custom_snackbar_view created previously
                                val customSnackView: View =
                                    layoutInflater.inflate(R.layout.snackbar_layout, null)

                                snackbar.view.setBackgroundColor(Color.TRANSPARENT)

                                val snackbarLayout = snackbar.view as SnackbarLayout

                                snackbarLayout.setPadding(0, 0, 0, 0)

                                val bGotoWebsite: Button =
                                    customSnackView.findViewById(R.id.gotoWebsiteButton)

                                bGotoWebsite.setOnClickListener {
                                    val view =
                                        View.inflate(this@MainActivity, R.layout.info_dialog, null)
                                    val builder = AlertDialog.Builder(this@MainActivity)
                                    builder.setView(view)
                                    val dialog = builder.create()
                                    dialog.show()
                                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                                    snackbar.dismiss()
                                }

                            snackbarLayout.addView(customSnackView, 0)
                                snackbar.show()

                        }, 800)
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor, i: Int) {
                }
            }
            sensorManager.registerListener(sensorEventListener, sensorr, 2 * 1000 * 1000)
        }
    }

    override fun onResume() {
        super.onResume()
        //Valets introduction
        MaterialTapTargetPrompt.Builder(this@MainActivity)
            .setTarget(R.id.visa_img)
            .setPrimaryText("It is your valets")
            .setSecondaryText("You can change for tap this")
            .setPromptStateChangeListener { prompt, state ->
                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                    //FingerPrint introduction
                    MaterialTapTargetPrompt.Builder(this@MainActivity)
                        .setTarget(R.id.usd)
                        .setPrimaryText("Please close your proximity sensor for pay bus ticket")
                        .setSecondaryText("Use after unlock fingerprint")
                        .setPromptStateChangeListener { prompt, state ->
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                // Location introduction
                                MaterialTapTargetPrompt.Builder(this@MainActivity)
                                    .setTarget(R.id.location)
                                    .setPrimaryText("Here your bus station, where you pay for bus ticket")
                                    .setSecondaryText("Tap for close")
                                    .setPromptStateChangeListener { prompt, state ->
                                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                            // Change valets introduction
                                            MaterialTapTargetPrompt.Builder(this@MainActivity)
                                                .setTarget(R.id.change)
                                                .setPrimaryText("You can transfers with your any valets")
                                                .setSecondaryText("Tap for close")
                                                .setPromptStateChangeListener { prompt, state ->
                                                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                                        // How to pay? Introduction
                                                        MaterialTapTargetPrompt.Builder(this@MainActivity)
                                                            .setTarget(R.id.finger_print)
                                                            .setPrimaryText("You can unlock with your fingerprint")
                                                            .setSecondaryText("Long click for unlock pay")
                                                            .setPromptStateChangeListener { prompt, state ->
                                                                if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                                                                    //Checking fingerprint and paying process
                                                                    handler.postDelayed({
                                                                    }, 200)
                                                                }
                                                            }
                                                            .show()
                                                    }
                                                }
                                                .show()
                                        }
                                    }
                                    .show()
                            }
                        }
                        .show()
                }
            }
            .show()


        binding.fingerPrint.setOnClickListener {
            binding.fingerPrint.setImageResource(
                R.drawable.fingerprint_false
            )
            vibrateFalse()
            isClock = false
            Snackbar.make(
                binding.root,
                "Long click! Try again.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        binding.fingerPrint.setOnLongClickListener {
            binding.fingerPrint.setImageResource(
                R.drawable.fingerprint_unlock
            )
            vibrateUnlock()
            isClock = true
            handler.postDelayed(
                {
                    binding.fingerPrint.visibility =
                        View.GONE
                },
                500
            )
            checkUnlock()
            true
        }
    }
    override fun onBackPressed() {
        if (isHome) {
            finishAffinity()
        } else {
            super.onBackPressed()
        }
    }
}