package jeziel.lacarreradelsiglo.lacarreradelsiglo.modelo;

import javafx.application.Platform;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class AnimalSemaforo extends Animal{
    private Semaphore semaforo;
    private Random random;

    public AnimalSemaforo(String nombre, AnimalListener listener, Semaphore semaforo) {
        super(nombre, listener);
        this.semaforo = semaforo;
        random = new Random();
    }

    @Override
    public void run() {

        while(this.getAvance() <=571){
            try{
                semaforo.acquire();

                this.setAvance(this.getAvance() + random.nextInt(21));
                random.setSeed(random.nextLong());
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() ->{
                    this.getListener().actualizarProgreso(this.getNombre(),this.getAvance());
                });
                semaforo.release();
            } catch (RuntimeException | InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                Platform.runLater(()->{
                    this.getListener().alFinalizar(this.getNombre());
                });}
        }
    }
}


