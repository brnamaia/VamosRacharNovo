package com.example.constraintlayout

import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.constraintlayout.ui.theme.GasolinaOuAlcoolTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GasolinaOuAlcoolTheme {
                GasolinaOuAlcoolApp()
            }
        }
    }
}

// Função para mostrar Toast (não é composable)
fun showToast(context: android.content.Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun GasolinaOuAlcoolApp() {
    var precoGasolina by remember { mutableStateOf("") }
    var precoAlcool by remember { mutableStateOf("") }
    var percentual by remember { mutableStateOf(0.75) }
    var resultado by remember { mutableStateOf("") }

    val context = LocalContext.current // Contexto para o Toast

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.LocalGasStation,
            contentDescription = "Ícone de compartilhamento",
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colors.onBackground // Muda automaticamente com o tema
        )

        Text(
            text = "Gasolina ou Álcool?",
            fontSize = 24.sp,
            color = MaterialTheme.colors.onBackground // Muda automaticamente com o tema
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Preço da gasolina
        OutlinedTextField(
            value = precoGasolina,
            onValueChange = { precoGasolina = it },
            label = { Text("Preço da gasolina") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colors.onBackground,
                cursorColor = MaterialTheme.colors.primary,
                focusedLabelColor = MaterialTheme.colors.primary,
                unfocusedLabelColor = MaterialTheme.colors.onBackground
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Preço do álcool
        OutlinedTextField(
            value = precoAlcool,
            onValueChange = { precoAlcool = it },
            label = { Text("Preço do álcool") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colors.onBackground,
                cursorColor = MaterialTheme.colors.primary,
                focusedLabelColor = MaterialTheme.colors.primary,
                unfocusedLabelColor = MaterialTheme.colors.onBackground
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Switch para ajustar percentual
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "75%",
                fontSize = 16.sp,
                color = MaterialTheme.colors.onBackground // Muda automaticamente com o tema
            )
            Switch(
                checked = percentual == 0.75,
                onCheckedChange = {
                    percentual = if (it) 0.75 else 0.70
                    showToast(context, "Percentual ajustado para ${(percentual * 100).toInt()}%")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão Calcular
        Button(onClick = {
            val gasolina = precoGasolina.toDoubleOrNull()
            val alcool = precoAlcool.toDoubleOrNull()
            if (gasolina != null && alcool != null) {
                resultado = if (alcool <= gasolina * percentual) {
                    "Álcool é a melhor opção."
                } else {
                    "Gasolina é a melhor opção."
                }
            } else {
                resultado = "Por favor, insira valores válidos."
            }
        }) {
            Text(text = "CALCULAR")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Resultado
        Text(
            text = resultado,
            fontSize = 20.sp,
            color = MaterialTheme.colors.onBackground // Muda automaticamente com o tema
        )
    }
}
