package TPOConcurrente;

import java.util.Random;

public class Visitante implements Runnable {
    private int nroDePulsera;
    private Parque parque;
    private boolean almorzo, merendo, continuar;

    public Visitante(Parque elParque) {
        this.parque = elParque;
        this.almorzo = false;
        this.merendo = false;
    }

    public void run() {
        try {
            // if (elegir() == 1) { // elige llegar en tour folklorico
            // if (parque.colectivo.irEnColectivo()) {
            // parque.colectivo.bajarseDelColectivo();
            // }
            // } // sino llega de forma particular

            parque.entrarAlParque();
            continuar = parque.puedeEntrar();
            this.nroDePulsera = parque.recibirPulseraYPasarMolinete();

            while (continuar) {
                Random random = new Random();
                int atraccionAVisitar = random.nextInt(5);

                switch (4) {
                    case 0: // Visita la tienda
                        if (parque.shop.adquirirSouvenir()) {
                            parque.shop.pagarEnCaja();
                        }
                        break;

                    case 1: // Hace snorkel
                        if (parque.snorkell.adquirirEquipoSnorkell()) {
                            System.out.println(ColoresSout.BLUE + "~~ El visitante " +
                                    Thread.currentThread().getName()
                                    + " esta haciendo snorkell ~~" + ColoresSout.RESET);
                            Thread.sleep(3000);
                            parque.snorkell.devolverEquipoSnorkell();
                        }
                        break;

                    case 2: // Va a un restaurante
                        int restaurante = random.nextInt(3);
                        if (!almorzo) {
                            if (parque.restaurantes[restaurante].entrarAComer("almorzar")) {
                                almorzo = true;
                                Thread.sleep(random.nextInt(6000));
                                parque.restaurantes[restaurante].salirDelRestaurante();
                            }
                        } else if (!merendo) {
                            if (parque.restaurantes[restaurante].entrarAComer("merendar")) {
                                merendo = true;
                                Thread.sleep(random.nextInt(5000));
                                parque.restaurantes[restaurante].salirDelRestaurante();
                            }
                        }
                        break;

                    case 3: // Visita el faro mirador y desciende en tobogan
                        int tobogan = parque.faro.subirEscalera();
                        if (tobogan == 1 || tobogan == 2) {
                            System.out
                                    .println(ColoresSout.YELLOW + "// El visitante " + Thread.currentThread().getName()
                                            + " se esta tirando por el tobogan " + tobogan + " //" + ColoresSout.RESET);
                            Thread.sleep(2000);
                            parque.faro.tirarseDelTobogan(tobogan);
                            Thread.sleep(3000);
                        }

                        // del de abajo
                        if (elegir() == 0) { // Decide ir en bicicleta
                            if (parque.standBicis.irEnBici()) {
                                Thread.sleep(random.nextInt(4000, 7000));
                                parque.standBicis.dejarBicicleta();
                            }
                        } else {// Decide ir en tren
                            if (parque.tren.irEnTren()) {
                                parque.tren.bajarseDelTren();
                            }
                        }

                        break;

                    case 4: // Participa de la carrera de gomones por el rio

                        if (parque.carreraGomones.pedirGomon(elegir() + 1)) {
                            parque.carreraGomones.pedirPertenencias();
                        }
                        break;

                    default:
                        break;
                }
                continuar = parque.puedeContinuar();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        parque.retirarse();
    }

    private int elegir() {
        Random random = new Random();
        return random.nextInt(2);
    }
}
