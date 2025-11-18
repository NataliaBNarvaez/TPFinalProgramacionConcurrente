// TPO Programacion Concurrente (Cursada 2022) ~ Natalia Narvaez FAI-3198
package TPOConcurrente;

public class Main {
    public static void main(String[] args) {
        int cantVisitantes = 30;
        int individuales = 5;
        int dobles = 5;
        Parque parque = new Parque(individuales, dobles);
        Gomon[] gIndividuales = new Gomon[individuales];
        Thread[] hilosGIndividuales = new Thread[individuales];
        Gomon[] gDobles = new Gomon[dobles];
        Thread[] hilosGDobles = new Thread[dobles];
        Visitante[] visitantes = new Visitante[cantVisitantes];
        Thread[] hilosVisitantes = new Thread[cantVisitantes];
        Thread adminTobogan = new Thread(new AdminTobogan(parque));
        Thread maquinista = new Thread(new Maquinista(parque));
        adminTobogan.start();
        maquinista.start();

        for (int i = 0; i < individuales; i++) {
            gIndividuales[i] = new Gomon(1, parque);
            hilosGIndividuales[i] = new Thread(gIndividuales[i]);
            hilosGIndividuales[i].start();
        }

        for (int i = 0; i < dobles; i++) {
            gDobles[i] = new Gomon(2, parque);
            hilosGDobles[i] = new Thread(gDobles[i]);
            hilosGDobles[i].start();
        }

        for (int i = 0; i < cantVisitantes; i++) {
            visitantes[i] = new Visitante(i, parque);
            hilosVisitantes[i] = new Thread(visitantes[i]);
            hilosVisitantes[i].start();
        }
    }

}
