package pe.edu.upeu.calcfx.control;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TictacControl {

    @FXML
    private TableView<Partida> tablaPartidas;
    @FXML
    private TableColumn<Partida, Integer> colNumeroPartida;
    @FXML
    private TableColumn<Partida, String> colJugador1;
    @FXML
    private TableColumn<Partida, String> colJugador2;
    @FXML
    private TableColumn<Partida, String> colGanador;
    @FXML
    private TableColumn<Partida, Integer> colPuntuacion;
    @FXML
    private TableColumn<Partida, String> colEstado;

    @FXML
    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    @FXML
    private Button iniciar;
    @FXML
    private Button anularJuego;
    @FXML
    private Button finalizarPartida;

    @FXML
    private TextField NombJ1;
    @FXML
    private TextField NombJ2;

    @FXML
    private Label contador1;
    @FXML
    private Label contador2;
    @FXML
    private Label jugaturno;

    private Button[][] tablero;
    private boolean turno = true;
    private boolean juegoIniciado = false;

    private int contadorTurnosJ1 = 0;
    private int contadorTurnosJ2 = 0;

    private int numeroPartida = 1;
    private ObservableList<Partida> listaPartidas = FXCollections.observableArrayList();
    private ArrayList<String> listaJugadores = new ArrayList<>();

    @FXML
    public void initialize() {
        System.out.println("BIENVENIDOS AL TIK TAK TOE");
        tablero = new Button[][]{
                {btn00, btn01, btn02},
                {btn10, btn11, btn12},
                {btn20, btn21, btn22}
        };

        configureTableColumns();
        tablaPartidas.setItems(listaPartidas);
        setTableroClickable(false);
    }

    private void configureTableColumns() {
        colNumeroPartida.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumeroPartida()).asObject());
        colJugador1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJugador1()));
        colJugador2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJugador2()));
        colGanador.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGanador()));
        colPuntuacion.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPuntuacion()).asObject());
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado()));
    }

    @FXML
    void accionButon(ActionEvent e) {
        if (!juegoIniciado) {
            return;
        }

        Button b = (Button) e.getSource();

        if (b.getText().isEmpty()) {
            b.setText(turno ? "X" : "O");
            turno = !turno;
            actualizarContadores();

            String ganador = determinarGanador();
            if (ganador != null) {
                jugaturno.setText(ganador.equals("Empate") ? "EMPATE!" : "GANADOR: " + ganador);
                setTableroClickable(false);
            } else {
                actualizarTurno();
            }
        }
    }

    private void actualizarContadores() {
        if (turno) {
            contadorTurnosJ1++;
        } else {
            contadorTurnosJ2++;
        }
        contador1.setText("Turnos J1: " + contadorTurnosJ1);
        contador2.setText("Turnos J2: " + contadorTurnosJ2);
    }

    @FXML
    void imprimir() {
        for (Button[] fila : tablero) {
            for (Button boton : fila) {
                System.out.print(boton.getText() + "\t");
            }
            System.out.println();
        }
    }

    @FXML
    public void iniciarJuego() {
        String nombreJugador1 = NombJ1.getText().trim();
        String nombreJugador2 = NombJ2.getText().trim();

        if (nombreJugador1.isEmpty() || nombreJugador2.isEmpty()) {
            System.out.println("Por favor, ingrese nombres válidos para ambos jugadores.");
            return;
        }

        listaJugadores.clear();
        listaJugadores.add(nombreJugador1);
        listaJugadores.add(nombreJugador2);

        jugaturno.setText("TURNO: " + nombreJugador1);
        contadorTurnosJ1 = 0;
        contadorTurnosJ2 = 0;
        contador1.setText("Turnos J1: " + contadorTurnosJ1);
        contador2.setText("Turnos J2: " + contadorTurnosJ2);

        setTableroClickable(true);
        juegoIniciado = true;
        iniciar.setDisable(true);

        Partida nuevaPartida = new Partida(numeroPartida++, nombreJugador1, nombreJugador2, "N/A", 0, "Jugando");
        listaPartidas.add(nuevaPartida);
        tablaPartidas.setItems(listaPartidas);
    }

    private void actualizarTurno() {
        if (listaJugadores.size() >= 2) {
            String jugadorTurno = turno ? listaJugadores.get(0) : listaJugadores.get(1);
            jugaturno.setText(" " + jugadorTurno);
        } else {
            jugaturno.setText(" ");
        }
    }

    @FXML
    public void anularJuego() {
        if (!juegoIniciado) {
            System.out.println("El juego no ha iniciado, no se puede anular.");
            return;
        }

        System.out.println("Partida anulada");
        limpiarTablero();

        NombJ1.clear();
        NombJ2.clear();

        contadorTurnosJ1 = 0;
        contadorTurnosJ2 = 0;
        contador1.setText(" " + contadorTurnosJ1);
        contador2.setText(" " + contadorTurnosJ2);

        jugaturno.setText("TURNO: -");

        Partida partidaAnulada = new Partida(numeroPartida++, listaJugadores.get(0), listaJugadores.get(1), "N/A", 1, "Anulada");
        listaPartidas.add(partidaAnulada);
        tablaPartidas.setItems(listaPartidas);

        numeroPartida = 1;
        juegoIniciado = false;
        iniciar.setDisable(false);
    }

    @FXML
    void finalizarPartida() {
        // Verifica si el juego está iniciado
        if (!juegoIniciado) {
            System.out.println("El juego no ha iniciado.");
            return;
        }

        String ganador = determinarGanador();
        int puntuacion = calcularPuntuacion();
        String estado = ganador != null ? "Finalizada" : "En juego";

        // Actualiza el estado de la última partida en la lista
        if (!listaPartidas.isEmpty()) {
            Partida ultimaPartida = listaPartidas.get(listaPartidas.size() - 1);
            ultimaPartida.estado.set("Terminada"); // Actualiza el estado a "Terminada"
            ultimaPartida.puntuacion.set(puntuacion); // Actualiza la puntuación
            ultimaPartida.ganador.set(ganador != null ? ganador : "N/A"); // Actualiza el ganador
        }

        anularJuego(); // Limpia el tablero y reinicia el juego
    }

    private void limpiarTablero() {
        for (Button[] fila : tablero) {
            for (Button boton : fila) {
                boton.setText("");
                boton.setDisable(true);
            }
        }
    }

    private void setTableroClickable(boolean clickable) {
        for (Button[] fila : tablero) {
            for (Button boton : fila) {
                boton.setDisable(!clickable);
            }
        }
    }

    private String determinarGanador() {
        String ganador = verificarFilasYColumnas();
        if (ganador == null) {
            ganador = verificarDiagonales();
        }

        if (ganador == null && esTableroLleno()) {
            ganador = "Empate";
        }

        return ganador;
    }

    private String verificarFilasYColumnas() {
        for (int i = 0; i < 3; i++) {
            if (verificarLinea(tablero[i][0], tablero[i][1], tablero[i][2])) {
                return tablero[i][0].getText();
            }
            if (verificarLinea(tablero[0][i], tablero[1][i], tablero[2][i])) {
                return tablero[0][i].getText();
            }
        }
        return null;
    }

    private String verificarDiagonales() {
        if (verificarLinea(tablero[0][0], tablero[1][1], tablero[2][2])) {
            return tablero[0][0].getText();
        }
        if (verificarLinea(tablero[0][2], tablero[1][1], tablero[2][0])) {
            return tablero[0][2].getText();
        }
        return null;
    }

    private boolean verificarLinea(Button b1, Button b2, Button b3) {
        return !b1.getText().isEmpty() && b1.getText().equals(b2.getText()) && b1.getText().equals(b3.getText());
    }

    private boolean esTableroLleno() {
        for (Button[] fila : tablero) {
            for (Button boton : fila) {
                if (boton.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private int calcularPuntuacion() {
        return turno ? contadorTurnosJ1 : contadorTurnosJ2;
    }

    public class Partida {
        private final SimpleIntegerProperty numeroPartida;
        private final SimpleStringProperty jugador1;
        private final SimpleStringProperty jugador2;
        private final SimpleStringProperty ganador;
        private final SimpleIntegerProperty puntuacion;
        private final SimpleStringProperty estado;

        public Partida(int numeroPartida, String jugador1, String jugador2, String ganador, int puntuacion, String estado) {
            this.numeroPartida = new SimpleIntegerProperty(numeroPartida);
            this.jugador1 = new SimpleStringProperty(jugador1);
            this.jugador2 = new SimpleStringProperty(jugador2);
            this.ganador = new SimpleStringProperty(ganador);
            this.puntuacion = new SimpleIntegerProperty(puntuacion);
            this.estado = new SimpleStringProperty(estado);
        }

        public int getNumeroPartida() {
            return numeroPartida.get();
        }

        public String getJugador1() {
            return jugador1.get();
        }

        public String getJugador2() {
            return jugador2.get();
        }

        public String getGanador() {
            return ganador.get();
        }

        public int getPuntuacion() {
            return puntuacion.get();
        }

        public String getEstado() {
            return estado.get();
        }
    }
}