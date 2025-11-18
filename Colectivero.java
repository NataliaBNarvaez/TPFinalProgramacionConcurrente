package TPOConcurrente;

public class Colectivero implements Runnable {
    private Parque parque;

    public Colectivero(Parque elParque) {
        this.parque = elParque;
    }

    public void run() {
        try {
            while (true) {
                parque.colectivo.arrancarTourColectivo();
                Thread.sleep(3000);
                parque.colectivo.terminarTourColectivo();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
