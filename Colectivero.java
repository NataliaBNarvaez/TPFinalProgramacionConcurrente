package TPOConcurrente;

public class Colectivero implements Runnable {
    private Parque parque;

    public Colectivero(Parque elParque) {
        this.parque = elParque;
    }

    public void run() {
        try {
            while (parque.puedeEntrar()) {
                parque.colectivo.arrancarTourColectivo();
                Thread.sleep(3000);
                parque.colectivo.terminarTourColectivo();
            }
            parque.colectivo.cerrarColectivo();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
