package TPOConcurrente;

public class AdminCarreraGomones implements Runnable {

    private Parque parque;

    public AdminCarreraGomones(Parque elParque) {
        this.parque = elParque;
    }

    public void run() {
        try {
            while (parque.puedeContinuar()) {
                if (parque.carreraGomones.iniciarCarrera()) {
                    parque.carreraGomones.esperaFinalizarCarrera();
                }
            }
            System.out.println(ColoresSout.RED + "- El Administrador del las Carreras de Gomones se retira del parque."
                    + ColoresSout.RESET);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}