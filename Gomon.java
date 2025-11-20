package TPOConcurrente;

import java.util.Random;

public class Gomon implements Runnable {

    private Parque parque;
    private int tipo;
    private Random random;

    public Gomon(int t, Parque elParque) {
        this.tipo = t; // individual=1 ; doble=2
        this.parque = elParque;
        this.random = new Random();
    }

    public void run() {
        try {
            while (parque.puedeContinuar()) {
                if (tipo == 1) {
                    parque.carreraGomones.esperaIndiv();
                } else {
                    parque.carreraGomones.esperaDoble();
                }
                parque.carreraGomones.carrera(tipo);
                Thread.sleep(random.nextInt(3000, 4000));
                parque.carreraGomones.terminarCarrera(tipo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
