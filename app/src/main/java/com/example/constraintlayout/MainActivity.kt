package com.example.constraintlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.util.*
import android.content.Intent
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var edtPessoas: EditText
    private lateinit var tvResultado: TextView
    private var ttsSucess: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtConta = findViewById(R.id.edtConta)
        edtPessoas = findViewById(R.id.edtPessoas)
        tvResultado = findViewById(R.id.tvResultado)

        edtConta.addTextChangedListener(this)
        edtPessoas.addTextChangedListener(this)

        // Initialize TTS engine
        tts = TextToSpeech(this, this)

        // Botão de compartilhamento
        val shareButton: FloatingActionButton = findViewById(R.id.floatingActionButtonShare)
        shareButton.setOnClickListener {
            val resultText = tvResultado.text.toString()
            val shareText = "Confira este valor dividido: $resultText"
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(shareIntent, "Compartilhar via"))
        }

        // Botão de som
        val soundButton: FloatingActionButton = findViewById(R.id.floatingActionButtonSound)
        soundButton.setOnClickListener {
            val resultText = tvResultado.text.toString()
            if (tts.isSpeaking) {
                tts.stop()
            }
            tts.speak("O valor é $resultText", TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Não precisa fazer nada aqui
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // Não precisa fazer nada aqui
    }

    override fun afterTextChanged(s: Editable?) {
        calcularDivisao()
    }

    private fun calcularDivisao() {
        val valorTotal = edtConta.text.toString().toDoubleOrNull() ?: 0.0
        val numPessoas = edtPessoas.text.toString().toIntOrNull() ?: 1

        if (numPessoas > 0) {
            val resultado = valorTotal / numPessoas
            val formatador = java.text.NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            tvResultado.text = formatador.format(resultado)
        } else {
            tvResultado.text = "R$ 0,00"
        }
    }

    fun clickFalar(v: View) {
        if (tts.isSpeaking) {
            tts.stop()
        }
        if (ttsSucess) {
            tts.speak(tvResultado.text.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onDestroy() {
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.getDefault()
            ttsSucess = true
        } else {
            ttsSucess = false
        }
    }
}
