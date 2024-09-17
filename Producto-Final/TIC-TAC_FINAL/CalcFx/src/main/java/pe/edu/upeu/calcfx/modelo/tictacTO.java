package pe.edu.upeu.calcfx.modelo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class tictacTO {

    public class Jugador {
        private String nombre;
        private int puntuacion;

        public Jugador(String nombre, int puntuacion) {
            this.nombre = nombre;
            this.puntuacion = puntuacion;
        }

        // Getters y setters
        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public int getPuntuacion() {
            return puntuacion;
        }

        public void setPuntuacion(int puntuacion) {
            this.puntuacion = puntuacion;
        }

        @Override
        public String toString() {
            return "Nombre: " + nombre + ", Puntuación: " + puntuacion;
        }
    }
    public class RegistroTurno {
        private String jugador;
        private String simbolo;
        private String posicion;

        public RegistroTurno(String jugador, String simbolo, String posicion) {
            this.jugador = jugador;
            this.simbolo = simbolo;
            this.posicion = posicion;
        }

        public String getJugador() {
            return jugador;
        }

        public void setJugador(String jugador) {
            this.jugador = jugador;
        }

        public String getSimbolo() {
            return simbolo;
        }

        public void setSimbolo(String simbolo) {
            this.simbolo = simbolo;
        }

        public String getPosicion() {
            return posicion;
        }

        public void setPosicion(String posicion) {
            this.posicion = posicion;
        }
    }


}
