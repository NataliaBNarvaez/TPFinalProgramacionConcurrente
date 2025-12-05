package TPOConcurrente;

public class Maquinista implements Runnable {
    private Parque parque;

    public Maquinista(Parque elParque) {
        this.parque = elParque;
    }

    public void run() {
        try {
            while (parque.puedeContinuar()) {
                if (parque.tren.arrancarRecorridoTren()) {
                    Thread.sleep(5000);
                    parque.tren.terminarRecorridoTren();
                }
            }
            System.out.println(ColoresSout.PASTEL_PURPLE + "- El Maquinista se retira del parque."
                    + ColoresSout.RESET);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
