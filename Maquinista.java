package TPOConcurrente;

public class Maquinista implements Runnable {
    private Parque parque;

    public Maquinista(Parque elParque) {
        this.parque = elParque;
    }

    public void run() {
        try {
            while (true) {
                parque.tren.arrancarRecorridoTren();
                Thread.sleep(5000);
                parque.tren.terminarRecorridoTren();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
