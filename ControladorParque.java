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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
