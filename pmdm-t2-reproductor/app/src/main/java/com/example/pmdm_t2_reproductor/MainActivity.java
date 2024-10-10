package com.example.pmdm_t2_reproductor;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int score = 0;  // Variable para almacenar la puntuación
    private TextToSpeech textToSpeech;  // Variable para TextToSpeech

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar TextToSpeech
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Idioma a Español
                    int result = textToSpeech.setLanguage(new Locale("es", "ES"));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(MainActivity.this, "El idioma no es compatible", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error al inicializar TextToSpeech", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Botones de las notas musicales
        Button btnDo = findViewById(R.id.btn_do);
        Button btnRe = findViewById(R.id.btn_re);
        Button btnMi = findViewById(R.id.btn_mi);
        Button btnFa = findViewById(R.id.btn_fa);
        Button btnSol = findViewById(R.id.btn_sol);
        Button btnLa = findViewById(R.id.btn_la);
        Button btnSi = findViewById(R.id.btn_si);

        // Botón para mostrar puntuación
        Button btnShowScore = findViewById(R.id.btn_show_score);

        // Listeners para los botones de las notas
        btnDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumarPunto();
                hablarNota("DO");
            }
        });

        btnRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumarPunto();
                hablarNota("RE");
            }
        });

        btnMi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumarPunto();
                hablarNota("MI");
            }
        });

        btnFa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumarPunto();
                hablarNota("FA");
            }
        });

        btnSol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumarPunto();
                hablarNota("SOL");
            }
        });

        btnLa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumarPunto();
                hablarNota("LA");
            }
        });

        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sumarPunto();
                hablarNota("SI");
            }
        });

        // Listener para el botón de mostrar puntuación
        btnShowScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarPuntuacion();
            }
        });
    }

    // Método para sumar puntos
    private void sumarPunto() {
        score++;  // Aumenta la puntuación en 1 cada vez que se pulsa una nota
    }

    // Método para mostrar la puntuación
    private void mostrarPuntuacion() {
        // Muestra un Toast con la puntuación actual
        Toast.makeText(MainActivity.this, "Puntuación actual: " + score, Toast.LENGTH_SHORT).show();
    }

    // Método para cantar la nota
    private void hablarNota(String nota) {
        if (textToSpeech != null) {
            textToSpeech.speak(nota, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    // Destruir TextToSpeech cuando la app se cierre
    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}