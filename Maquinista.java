package TPOConcurrente;

public class Maquinista implements Runnable {
    private Parque parque;

    public Maquinista(Parque elParque) {
        this.parque = elParque;
    }

    public void run() {
        try {
            while (parque.puedeContinuar()) {
                parque.tren.arrancarRecorridoTren();
                Thread.sleep(5000);
                parque.tren.terminarRecorridoTren();
            }
            parque.tren.cerrarTren();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
