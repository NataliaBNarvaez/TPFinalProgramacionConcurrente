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
            System.out.println(ColoresSout.PASTEL_MINT + "- El colectivero se retira del parque." + ColoresSout.RESET);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
