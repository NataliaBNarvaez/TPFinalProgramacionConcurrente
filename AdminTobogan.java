package TPOConcurrente;

public class AdminTobogan implements Runnable {

    private Parque parque;

    public AdminTobogan(Parque elParque) {
        this.parque = elParque;
    }

    public void run() {
        try {
            while (true) {
                parque.faro.asignarTobogan();
                System.out.println("El administrador del tobogan asigno un tobogan...");
                Thread.sleep(300);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
