package iesmm.pmdmo.mijuegotresenraya;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private JuegoTresEnRaya juego;
    private Button[] botones;
    private MediaPlayer player; // Para el sonido de efecto
    private MediaPlayer backgroundPlayer; // Para la música de fondo
    private RelativeLayout layout; // Variable para el layout

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        juego = new JuegoTresEnRaya();
        botones = new Button[9];
        layout = findViewById(R.id.layout_main); // Ahora debería funcionar correctamente

        // Inicializar el color de fondo a gris
        layout.setBackgroundColor(getResources().getColor(R.color.gray)); // Asegúrate de definir este color en colors.xml

        // Asignar botones de la interfaz a la matriz de botones
        for (int i = 0; i < 9; i++) {
            String buttonID = "btn" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            botones[i] = findViewById(resID);
            final int index = i;

            botones[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jugarHumano(index);
                }
            });
        }

        // Inicializar música de fondo
        backgroundPlayer = MediaPlayer.create(this, R.raw.background_music);
        backgroundPlayer.setLooping(true);
        backgroundPlayer.start();
    }

    private void jugarHumano(int index) {
        if (juego.moverFicha(JuegoTresEnRaya.JUGADOR, index)) {
            reproducirSonidoEfecto();
            actualizarBoton(index);
            int ganador = juego.comprobarGanador();

            if (ganador == 1) {
                Toast.makeText(this, "¡Has ganado!", Toast.LENGTH_SHORT).show();
                reiniciarJuego();
            } else if (ganador == -1) {
                Toast.makeText(this, "¡Es un empate!", Toast.LENGTH_SHORT).show();
                reiniciarJuego();
            } else {
                jugarMaquina();
            }
        }
    }

    private void jugarMaquina() {
        int movimiento = encontrarMovimientoMaquina();
        if (juego.moverFicha(JuegoTresEnRaya.MAQUINA, movimiento)) {
            reproducirSonidoEfecto();
            actualizarBoton(movimiento);
            int ganador = juego.comprobarGanador();
            if (ganador == -1) {
                Toast.makeText(this, "¡Es un empate!", Toast.LENGTH_SHORT).show();
                reiniciarJuego();
            } else if (ganador == 2) {
                Toast.makeText(this, "¡La máquina ha ganado!", Toast.LENGTH_SHORT).show();
                reiniciarJuego();
            }
        }
    }

    private int encontrarMovimientoMaquina() {
        // Simple IA: selecciona una casilla aleatoria vacía
        int movimiento;
        do {
            movimiento = (int) (Math.random() * 9);
        } while (juego.getTablero()[movimiento] != JuegoTresEnRaya.BLANCO);
        return movimiento;
    }

    private void actualizarBoton(int index) {
        if (juego.getTablero()[index] == JuegoTresEnRaya.JUGADOR) {
            botones[index].setBackgroundResource(R.drawable.jugador); // Establecer imagen del jugador
        } else if (juego.getTablero()[index] == JuegoTresEnRaya.MAQUINA) {
            botones[index].setBackgroundResource(R.drawable.maquina); // Establecer imagen de la máquina
        }
    }

    private void reiniciarJuego() {
        juego.limpiarTablero();
        for (Button boton : botones) {
            boton.setBackgroundResource(0); // Limpiar el fondo del botón
        }
        // Restaurar el color de fondo
        layout.setBackgroundColor(getResources().getColor(R.color.gray)); // Asegúrate de que gray esté definido
    }

    private void reproducirSonidoEfecto() {
        if (player != null) {
            player.release();
        }
        player = MediaPlayer.create(this, R.raw.effect);
        player.start();
        player.setOnCompletionListener(mp -> {
            mp.release();
            player = null; // Liberar el recurso
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundPlayer != null) {
            backgroundPlayer.release();
            backgroundPlayer = null; // Liberar recurso
        }
    }
}
