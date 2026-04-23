package jeziel.lacarreradelsiglo.lacarreradelsiglo.controlador;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import jeziel.lacarreradelsiglo.lacarreradelsiglo.modelo.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

public class Controlador implements Initializable, AnimalListener {
    Animal conejo;
    Animal tortuga;
    Animal capibara1;
    Animal capibara2;
    Semaphore semaforo;
    private final Map<String, ImageView> imagenesCompetidores = new HashMap<>();
    private final Map<String, Label> caminoCompetidores = new HashMap<>();

    @FXML
    private Label ganador;
    @FXML
    private ImageView conejoImagen;
    @FXML
    private ImageView tortugaImagen;
    @FXML
    private ImageView capibaraImagen1;
    @FXML
    private ImageView capibaraImagen2;
    @FXML
    private Label caminoConejo;
    @FXML
    private Label caminoTortuga;

    @Override
    public void actualizarProgreso(String  nombre, int avance){
        Platform.runLater(()->{
            imagenesCompetidores.get(nombre).setTranslateX(avance);
            if(Objects.equals(nombre, "Conejo") || Objects.equals(nombre, "Tortuga")){
                caminoCompetidores.get(nombre).setText("*".repeat(Math.max(0,(int)avance/8)));
            }else{
                caminoCompetidores.get(nombre).setText(" ".repeat(Math.max(0,(int)avance/32)) + caminoCompetidores.get(nombre).getText());
            }
        });
    }

    @Override
    public void alFinalizar(String nombre) {
        if (Objects.equals(ganador.getText(), "")){
            ganador.setText(nombre);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        semaforo = new Semaphore(1);
        capibaraImagen1.setVisible(false);
        capibaraImagen2.setVisible(false);
        capibaraImagen1.setManaged(false);
        capibaraImagen2.setManaged(false);
        imagenesCompetidores.put("Conejo",conejoImagen);
        imagenesCompetidores.put("Tortuga", tortugaImagen);
        imagenesCompetidores.put("capibara1",capibaraImagen1);
        imagenesCompetidores.put("capibara2",capibaraImagen2);
        caminoCompetidores.put("Conejo",caminoConejo);
        caminoCompetidores.put("Tortuga", caminoTortuga);
        caminoCompetidores.put("capibara1",caminoConejo);
        caminoCompetidores.put("capibara2", caminoTortuga);
        ganador.setText("");
        conejo = new AnimalSemaforo("Conejo", this, semaforo);
        tortuga = new AnimalSemaforo("Tortuga", this, semaforo);
        capibara1 = new Animal("capibara1", this);
        capibara2 = new Animal("capibara2", this);



    }

    @FXML
    public void clickEmpezar() {

        ganador.setText("");
        caminoConejo.setText("");
        caminoTortuga.setText("");
        capibaraImagen1.setVisible(false);
        capibaraImagen2.setVisible(false);

        conejo.setAvance(0);
        tortuga.setAvance(0);
        capibara1.setAvance(0);
        capibara2.setAvance(0);

        conejoImagen.setTranslateX(0);
        tortugaImagen.setTranslateX(0);
        capibaraImagen1.setTranslateX(0);
        capibaraImagen2.setTranslateX(0);

        CompletableFuture<Void> carreraConejo = CompletableFuture.runAsync(conejo);
        CompletableFuture<Void> carreraTortuga = CompletableFuture.runAsync(tortuga);

        CompletableFuture.allOf(carreraConejo, carreraTortuga)
                .thenCompose(v -> {
                    return Reiniciar();
                })
                .thenRun(() -> {
                    Platform.runLater(() -> {
                        ganador.setText("¡La carrera del siglo ha terminado!");

                        capibaraImagen1.setVisible(false);
                        capibaraImagen2.setVisible(false);
                    });
                });
    }

    public CompletableFuture<Void> Reiniciar() {
        Platform.runLater(() -> {
            capibaraImagen1.setVisible(true);
            capibaraImagen2.setVisible(true);
            capibaraImagen1.setManaged(true);
            capibaraImagen2.setManaged(true);

            conejoImagen.setTranslateX(0);
            tortugaImagen.setTranslateX(0);
        });

        CompletableFuture<Void> c3 = CompletableFuture.runAsync(capibara1);
        CompletableFuture<Void> c4 = CompletableFuture.runAsync(capibara2);

        return CompletableFuture.allOf(c3, c4);
    }

}
