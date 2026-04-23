package jeziel.lacarreradelsiglo.lacarreradelsiglo.modelo;
import javafx.application.Platform;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Animal implements Runnable {
    private String nombre;
    private int avance = 0;
    private AnimalListener listener;
    private Random random = new Random(System.currentTimeMillis());


    public Animal(String nombre, AnimalListener listener) {
        this.nombre = nombre;
        this.listener = listener;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public AnimalListener getListener() {
        return listener;
    }

    public void setListener(AnimalListener listener) {
        this.listener = listener;
    }

    public int getAvance() {
        return avance;
    }

    public void setAvance(int avance) {
        this.avance = avance;
    }

    @Override
    public void run() {

        while(this.avance<=571){
                this.avance+=random.nextInt(21);
                random.setSeed(random.nextLong());
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() ->{
                    listener.actualizarProgreso(this.nombre,this.avance);
                });

                Platform.runLater(()->{
                    listener.alFinalizar(this.nombre);
                });
        }
    }
}
