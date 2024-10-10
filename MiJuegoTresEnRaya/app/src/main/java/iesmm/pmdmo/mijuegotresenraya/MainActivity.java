package iesmm.pmdmo.mijuegotresenraya;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private JuegoTresEnRaya juego;
    private Button[] botones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        juego = new JuegoTresEnRaya();
        botones = new Button[9];

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
    }

    private void jugarHumano(int index) {
        if (juego.moverFicha(JuegoTresEnRaya.JUGADOR, index)) {
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
            botones[index].setText("X");
        } else if (juego.getTablero()[index] == JuegoTresEnRaya.MAQUINA) {
            botones[index].setText("O");
        }
    }

    private void reiniciarJuego() {
        juego.limpiarTablero();
        for (Button boton : botones) {
            boton.setText("");
        }
    }
}
