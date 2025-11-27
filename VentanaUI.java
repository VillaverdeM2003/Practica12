import javax.swing.*;
import java.awt.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class VentanaUI implements InterfazUsuario {

    private JFrame frame;
    private JTextArea mensajeArea;
    private JTextField entradaCampo;
    private JButton enviarBoton;
    private BlockingQueue<String> colaDeEntrada;
    private JPanel tableroPropioPanel;
    private JPanel tableroEnemigoPanel;

    private static final int TAMANIO_TABLERO = 10;
    
    public VentanaUI() {
        this.colaDeEntrada = new LinkedBlockingQueue<>();
        inicializarGUI();
    }

    private void inicializarGUI() {
        frame = new JFrame("BATTLESHIP P2P");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(800, 600); 

        // --- Área de Mensajes (Log) ---
        mensajeArea = new JTextArea(15, 60);
        mensajeArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mensajeArea);
        frame.add(scrollPane, BorderLayout.NORTH);

        // --- Paneles de Tableros ---
        JPanel tablerosContenedor = new JPanel(new GridLayout(1, 2));
        tableroPropioPanel = crearPanelTablero("TU TABLERO");
        tableroEnemigoPanel = crearPanelTablero("TABLERO ENEMIGO");
        tablerosContenedor.add(tableroPropioPanel);
        tablerosContenedor.add(tableroEnemigoPanel);
        frame.add(tablerosContenedor, BorderLayout.CENTER);

        // --- Área de Entrada de Usuario ---
        JPanel entradaPanel = new JPanel();
        entradaCampo = new JTextField(30);
        enviarBoton = new JButton("Enviar Comando");

        enviarBoton.addActionListener(e -> {
            String entrada = entradaCampo.getText();
            if (!entrada.trim().isEmpty()) {
                try {
                    colaDeEntrada.put(entrada); 
                    entradaCampo.setText("");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    mostrarMensaje("Error interno al procesar entrada.");
                }
            }
        });

        entradaPanel.add(entradaCampo);
        entradaPanel.add(enviarBoton);
        frame.add(entradaPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
        mostrarMensaje("GUI Inicializada. Esperando entrada de Nombre.");
    }
    
    private JPanel crearPanelTablero(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        return panel;
    }

    // --- Implementación de InterfazUsuario ---

    @Override
    public void mostrarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> mensajeArea.append(mensaje + "\n"));
    }

    @Override
    public void mostrarTablero(char[][] tablero, String titulo) {
        JPanel panel = "TU TABLERO".equals(titulo) ? tableroPropioPanel : tableroEnemigoPanel;
        
        SwingUtilities.invokeLater(() -> {
            panel.removeAll();

            JPanel gridPanel = new JPanel(new GridLayout(TAMANIO_TABLERO + 1, TAMANIO_TABLERO + 1));
            
            // Etiqueta superior de columnas (0-9)
            gridPanel.add(new JLabel(" ", SwingConstants.CENTER));
            for (int j = 0; j < TAMANIO_TABLERO; j++) {
                JLabel label = new JLabel(String.valueOf(j), SwingConstants.CENTER);
                gridPanel.add(label);
            }

            // Filas
            for (int i = 0; i < TAMANIO_TABLERO; i++) {
                // Etiqueta lateral de filas (0-9)
                JLabel filaLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
                gridPanel.add(filaLabel);
                
                // Celdas
                for (int j = 0; j < TAMANIO_TABLERO; j++) {
                    JButton celdaBoton = new JButton(String.valueOf(tablero[i][j]));
                    celdaBoton.setPreferredSize(new Dimension(30, 30));
                    celdaBoton.setMargin(new Insets(0, 0, 0, 0));
                    
                    celdaBoton.setBackground(getColorParaCelda(tablero[i][j]));
                    celdaBoton.setForeground(Color.WHITE); // Texto blanco para visibilidad

                    // Lógica para disparar solo en el tablero enemigo
                    if ("TABLERO ENEMIGO".equals(titulo) && tablero[i][j] == '?') {
                        final int fila = i;
                        final int columna = j;
                        celdaBoton.addActionListener(e -> {
                            // Se insertan las coordenadas en la cola
                            String coordenadas = fila + "," + columna;
                            try {
                                colaDeEntrada.put(coordenadas);
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                            }
                        });
                    }
                    
                    gridPanel.add(celdaBoton);
                }
            }
            
            panel.add(gridPanel, BorderLayout.CENTER);
            // Agrega una leyenda visual en la parte inferior del panel
            panel.add(new JLabel("  X=Impacto, O=Fallo, ?=Desconocido, Letras=Barcos ", SwingConstants.CENTER), BorderLayout.SOUTH);
            panel.revalidate(); 
            panel.repaint();    
        });
    }

    private Color getColorParaCelda(char c) {
        switch (c) {
            case '~': 
                return new Color(0, 102, 204); // Azul Marino
            case 'X': 
                return new Color(204, 0, 0); // Rojo Fuego
            case 'O': 
                return Color.GRAY;
            case '?': 
                return new Color(50, 50, 50); // Gris Oscuro para Desconocido
            default: // Barco (letras)
                return new Color(139, 69, 19); // Café (Barco)
        }
    }

    @Override
    public String solicitarNombre() {
        String nombre = (String) JOptionPane.showInputDialog(
                frame,
                "Ingresa tu nombre:",
                "Solicitud de Nombre",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                "Jugador1"
        );
        return nombre != null ? nombre : "JugadorDefault";
    }

    @Override
    public String obtenerOpcionModo() {
        mostrarMensaje("\nSelecciona modo: 1. Crear partida (Servidor) | 2. Unirse a partida (Cliente)");
        mostrarMensaje("Ingresa 1 o 2 en el campo de texto y presiona 'Enviar Comando'.");
        
        while (true) {
            try {
                String opcion = colaDeEntrada.take().trim();
                if ("1".equals(opcion) || "2".equals(opcion)) {
                    return opcion;
                } else {
                    mostrarMensaje("Opción inválida. Intenta nuevamente. Ingresa 1 o 2.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return "2";
            }
        }
    }

    @Override
    public String solicitarIP() {
        String ip = (String) JOptionPane.showInputDialog(
                frame,
                "Ingresa la IP del otro jugador:",
                "Solicitud de IP",
                JOptionPane.QUESTION_MESSAGE
        );
        return ip != null ? ip : "127.0.0.1";
    }

    @Override
    public int[] obtenerDisparoJugador(boolean yaDisparado) {
        mostrarMensaje("Es tu turno. Haz clic en una celda '?' del TABLERO ENEMIGO para disparar.");
        
        while (true) {
            try {
                String entrada = colaDeEntrada.take();
                String[] coordenadas = entrada.split(",");

                if (coordenadas.length == 2) {
                    int fila = Integer.parseInt(coordenadas[0].trim());
                    int columna = Integer.parseInt(coordenadas[1].trim());
                    
                    return new int[] { fila, columna };
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (NumberFormatException e) {
                 // Si la entrada no son coordenadas válidas (ej. viene del campo de texto de abajo), la ignora y espera el clic
            }
        }
        return new int[] { -1, -1 }; 
    }

    public void close() {
        SwingUtilities.invokeLater(() -> frame.dispose());
    }
}