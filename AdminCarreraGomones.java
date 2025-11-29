package TPOConcurrente;

public class AdminCarreraGomones implements Runnable {

    private Parque parque;

    public AdminCarreraGomones(Parque elParque) {
        this.parque = elParque;
    }

    public void run() {
        try {
            while (parque.puedeContinuar()) {
                parque.carreraGomones.iniciarCarrera();
                parque.carreraGomones.esperaFinalizarCarrera();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}