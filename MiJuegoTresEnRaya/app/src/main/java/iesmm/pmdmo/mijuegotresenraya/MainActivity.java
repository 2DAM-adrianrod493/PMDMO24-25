package iesmm.pmdmo.mijuegotresenraya;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.TextView; // Importa TextView

public class MainActivity extends AppCompatActivity {

    private JuegoTresEnRaya juego;
    private Button[] botones;
    private MediaPlayer player; // Para el sonido de efecto
    private MediaPlayer backgroundPlayer; // Para la música de fondo
    private RelativeLayout layout; // Variable para el layout
    private ImageButton btnPauseMusic; // Botón para pausar la música

    // Variables para puntuaciones y TextViews
    private TextView textJugador;
    private TextView textMaquina;
    private TextView textPartidas;
    private int puntuacionJugador = 0;
    private int puntuacionMaquina = 0;
    private int partidasJugadas = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        juego = new JuegoTresEnRaya();
        botones = new Button[9];
        layout = findViewById(R.id.layout_main);

        // Inicializar el color de fondo
        layout.setBackgroundColor(getResources().getColor(R.color.gray));

        // Inicializar TextViews para puntuaciones
        textJugador = findViewById(R.id.textJugador);
        textMaquina = findViewById(R.id.textMaquina);
        textPartidas = findViewById(R.id.textPartidas);

        // Asignar botones de la interfaz a la matriz de botones
        for (int i = 0; i < 9; i++) {
            String buttonID = "btn" + (i + 1);
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

        // Botón para pausar la música de fondo
        btnPauseMusic = findViewById(R.id.btnPauseMusic);
        btnPauseMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backgroundPlayer.isPlaying()) {
                    backgroundPlayer.pause();
                    btnPauseMusic.setImageResource(android.R.drawable.ic_media_play);
                } else {
                    backgroundPlayer.start();
                    btnPauseMusic.setImageResource(android.R.drawable.ic_media_pause);
                }
            }
        });
    }

    private void jugarHumano(int index) {
        if (juego.moverFicha(JuegoTresEnRaya.JUGADOR, index)) {
            reproducirSonidoEfecto();
            actualizarBoton(index);
            int ganador = juego.comprobarGanador();

            if (ganador == 1) {
                puntuacionJugador++; // Aumentar puntuación del jugador
                Toast.makeText(this, getString(R.string.player_wins), Toast.LENGTH_SHORT).show();
                actualizarPuntuaciones();
                reiniciarJuego();
            } else if (ganador == -1) {
                Toast.makeText(this, getString(R.string.draw), Toast.LENGTH_SHORT).show();
                reiniciarJuego();
            } else {
                jugarMaquina();
            }
        }
    }

    private void jugarMaquina() {
        int movimiento = juego.getMovimientoMaquina();
        if (juego.moverFicha(JuegoTresEnRaya.MAQUINA, movimiento)) {
            reproducirSonidoEfecto();
            actualizarBoton(movimiento);
            int ganador = juego.comprobarGanador();
            if (ganador == 2) {
                puntuacionMaquina++; // Aumentar puntuación de la máquina
                Toast.makeText(this, getString(R.string.machine_wins), Toast.LENGTH_SHORT).show();
                actualizarPuntuaciones();
                reiniciarJuego();
            } else if (ganador == -1) {
                Toast.makeText(this, getString(R.string.draw), Toast.LENGTH_SHORT).show();
                reiniciarJuego();
            }
        }
    }

    private void actualizarPuntuaciones() {
        textJugador.setText("Jugador: " + puntuacionJugador);
        textMaquina.setText("Máquina: " + puntuacionMaquina);
        partidasJugadas++;
        textPartidas.setText("Partidas: " + partidasJugadas);
    }

    private void actualizarBoton(int index) {
        if (juego.getTablero()[index] == JuegoTresEnRaya.JUGADOR) {
            botones[index].setBackgroundResource(R.drawable.jugador); // Imagen del jugador
        } else if (juego.getTablero()[index] == JuegoTresEnRaya.MAQUINA) {
            botones[index].setBackgroundResource(R.drawable.maquina); // Imagen de la máquina
        }
    }

    private void reiniciarJuego() {
        juego.limpiarTablero();
        for (Button boton : botones) {
            boton.setBackgroundResource(0); // Limpiar fondo
        }
        layout.setBackgroundColor(getResources().getColor(R.color.gray));
    }

    private void reproducirSonidoEfecto() {
        if (player != null) {
            player.release();
        }
        player = MediaPlayer.create(this, R.raw.effect);
        player.start();
        player.setOnCompletionListener(mp -> {
            mp.release();
            player = null;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundPlayer != null) {
            backgroundPlayer.release();
            backgroundPlayer = null;
        }
    }
}
