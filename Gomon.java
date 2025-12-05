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
                if (parque.puedeContinuar()) {
                    // para que no continuen la ejecucion si fueron despertados porque cerró la
                    // actividad
                    parque.carreraGomones.carrera(tipo);
                    Thread.sleep(random.nextInt(3000, 4000));
                    parque.carreraGomones.terminarCarrera(tipo);
                }
            }
            System.out.println(ColoresSout.PASTEL_PINK +
                    "- El gomon: " + Thread.currentThread().getName() + " fue guardado en el deposito."
                    + ColoresSout.RESET);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
