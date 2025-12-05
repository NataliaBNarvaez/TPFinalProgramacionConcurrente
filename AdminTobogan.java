package TPOConcurrente;

public class AdminTobogan implements Runnable {

    private Parque parque;

    public AdminTobogan(Parque elParque) {
        this.parque = elParque;
    }

    public void run() {
        try {
            while (parque.puedeContinuar()) {
                if (parque.faro.asignarTobogan()) {
                    System.out.println("El administrador del tobogan asigno un tobogan...");
                }
            }
            System.out.println(ColoresSout.YELLOW + "- El Administrador del Tobogan se retira del parque."
                    + ColoresSout.RESET);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
