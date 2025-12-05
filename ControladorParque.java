package TPOConcurrente;

public class ControladorParque implements Runnable {
    private Parque parque;

    public ControladorParque(Parque elParque) {
        this.parque = elParque;
    }

    public void run() {
        try {
            parque.abrirParque();
            parque.cerrarIngresoParque();
            parque.cerrarActividades();
            System.out.println(ColoresSout.CYAN + "- El controlador del parque se ha retirado." + ColoresSout.RESET);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
