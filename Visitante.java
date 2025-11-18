package TPOConcurrente;

import java.util.Random;

public class Visitante implements Runnable {
    private int nroDePulsera;
    private Parque parque;
    private boolean almorzo, merendo;

    public Visitante(int unNro, Parque elParque) {
        this.nroDePulsera = unNro;
        this.parque = elParque;
        this.almorzo = false;
        this.merendo = false;
    }

    public void run() {
        try {
            while (true) { // Hay q poner condicion de corte con el horario del parque
                Random random = new Random();
                int atraccionAVisitar = random.nextInt(5);
                switch (4) {
                    case 0: // Visita la tienda
                        parque.shop.adquirirSouvenir();
                        if (elegir() == 0) {
                            parque.shop.pagarEnCaja1();
                        } else {
                            parque.shop.pagarEnCaja2();
                        }
                        break;

                    case 1: // Hace snorkel
                        parque.snorkell.adquirirEquipoSnorkell();
                        System.out.println(ColoresSout.BLUE + "~~ El visitante " +
                                Thread.currentThread().getName()
                                + " esta haciendo snorkell ~~" + ColoresSout.RESET);
                        Thread.sleep(10000);
                        parque.snorkell.devolverEquipoSnorkell();
                        break;

                    case 2: // Va a un restaurante
                        int restaurante = random.nextInt(3);
                        if (!almorzo) {
                            parque.restaurantes[restaurante].entrarAComer("almorzar");
                            almorzo = true;
                            Thread.sleep(random.nextInt(4));
                            parque.restaurantes[restaurante].salirDelRestaurante();
                        } else if (!merendo) {
                            parque.restaurantes[restaurante].entrarAComer("merendar");
                            merendo = true;
                            Thread.sleep(random.nextInt(2));
                            parque.restaurantes[restaurante].salirDelRestaurante();
                        }
                        break;

                    case 3: // Visita el faro mirador y desciende en tobogan
                        System.out.println("El visitante " + Thread.currentThread().getName()
                                + " esta esperando subir");
                        int tobogan = parque.faro.subirEscalera();
                        System.out.println(ColoresSout.YELLOW + "// El visitante " + Thread.currentThread().getName()
                                + " se esta tirando por el tobogan " + tobogan + " //" + ColoresSout.RESET);
                        Thread.sleep(2000);
                        parque.faro.tirarseDelTobogan(tobogan);
                        System.out.println("El visitante " + Thread.currentThread().getName()
                                + " libero un tobogan ");
                        break;

                    case 4: // Participa de la carrera de gomones por el rio
                        /*
                         * if (elegir() == 0) {
                         * parque.standBicis.irEnBici();
                         * Thread.sleep(random.nextInt(4000, 7000));
                         * parque.standBicis.dejarBicicleta();
                         * } else {
                         * parque.tren.irEnTren();
                         * parque.tren.bajarseDelTren();
                         * }
                         */

                        parque.carreraGomones.pedirGomon(elegir() + 1);

                        parque.carreraGomones.pedirPertenencias();

                        break;

                    default:
                        break;
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int elegir() {
        Random random = new Random();
        return random.nextInt(2);
    }
}
