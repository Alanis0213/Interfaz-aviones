import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

public class VueloDB implements IReservable {
        private int numeroVuelo;
    private String aerolinea;
    private String horaSalida;
    private String destino;
    private int capacidadMaxima;
    private AeropuertoDB aeropuertoPartida;
    private AeropuertoDB aeropuertoLlegada;
    private ArrayList<PasajeroDB> pasajeros;
    private ArrayList<Integer> asientosReservados;

    private static final String url = "jdbc:mysql://localhost:3306/vuelos?serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "S3n42023*";

        public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

        public VueloDB(int numeroVuelo, String aerolinea, String horaSalida, String destino, int capacidadMaxima,
                AeropuertoDB aeropuertoPartida, AeropuertoDB aeropuertoLlegada, ArrayList<PasajeroDB> pasajeros) {
            this.numeroVuelo = numeroVuelo;
            this.aerolinea = aerolinea;
            this.horaSalida = horaSalida;
            this.destino = destino;
            this.capacidadMaxima = capacidadMaxima;
            this.aeropuertoPartida = aeropuertoPartida;
            this.aeropuertoLlegada = aeropuertoLlegada;
            this.pasajeros = pasajeros;
        }

        public int getNumeroVuelo() {
            return numeroVuelo;
        }

        public void setNumeroVuelo(int numeroVuelo) {
            this.numeroVuelo = numeroVuelo;
        }

        public String getAerolinea() {
            return aerolinea;
        }

        public void setAerolinea(String aerolinea) {
            this.aerolinea = aerolinea;
        }

        public String getHoraSalida() {
            return horaSalida;
        }

        public void setHoraSalida(String horaSalida) {
            this.horaSalida = horaSalida;
        }

        public String getDestino() {
            return destino;
        }

        public void setDestino(String destino) {
            this.destino = destino;
        }

        public int getCapacidadMaxima() {
            return capacidadMaxima;
        }

        public void setCapacidadMaxima(int capacidadMaxima) {
            this.capacidadMaxima = capacidadMaxima;
        }

        public AeropuertoDB getAeropuertoPartida() {
            return aeropuertoPartida;
        }

        public void setAeropuertoPartida(AeropuertoDB aeropuertoPartida) {
            this.aeropuertoPartida = aeropuertoPartida;
        }

        public AeropuertoDB getAeropuertoLlegada() {
            return aeropuertoLlegada;
        }

        public void setAeropuertoLlegada(AeropuertoDB aeropuertoLlegada) {
            this.aeropuertoLlegada = aeropuertoLlegada;
        }

        public ArrayList<PasajeroDB> getPasajeros() {
            return pasajeros;
        }

        public void setPasajeros(ArrayList<PasajeroDB> pasajeros) {
            this.pasajeros = pasajeros;
        }

        public static String getUrl() {
            return url;
        }

        public static String getUser() {
            return user;
        }

        public static String getPassword() {
            return password;
        }
        

        public VueloDB() {
        }

        public static void agregarVuelo() {
            try {
                
                ArrayList<AeropuertoDB> aeropuertos = obtenerAeropuertos();
                JTextField numeroVueloField = new JTextField();
                JTextField aerolineaField = new JTextField();
                JTextField horaSalidaField = new JTextField();
                JTextField destinoField = new JTextField();
                JTextField capacidadMaximaField = new JTextField();
    
                JComboBox<AeropuertoDB> aeropuertoPartidaComboBox = new JComboBox<>(aeropuertos.toArray(new AeropuertoDB[0]));
                JComboBox<AeropuertoDB> aeropuertoLlegadaComboBox = new JComboBox<>(aeropuertos.toArray(new AeropuertoDB[0]));
    
                JPanel panel = new JPanel(new GridLayout(7, 2));
                panel.add(new JLabel("Número de Vuelo:"));
                panel.add(numeroVueloField);
                panel.add(new JLabel("Aerolínea:"));
                panel.add(aerolineaField);
                panel.add(new JLabel("Hora de Salida:"));
                panel.add(horaSalidaField);
                panel.add(new JLabel("Destino:"));
                panel.add(destinoField);
                panel.add(new JLabel("Capacidad Máxima de Pasajeros:"));
                panel.add(capacidadMaximaField);
                panel.add(new JLabel("Aeropuerto de Partida:"));
                panel.add(aeropuertoPartidaComboBox);
                panel.add(new JLabel("Aeropuerto de Llegada:"));
                panel.add(aeropuertoLlegadaComboBox);
    
                
                int option = JOptionPane.showConfirmDialog(
                        null,
                        panel,
                        "Agregar Vuelo",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );
    
                if (option == JOptionPane.OK_OPTION) {
            
                    int numeroVuelo = Integer.parseInt(numeroVueloField.getText());
                    String aerolinea = aerolineaField.getText();
                    String horaSalida = horaSalidaField.getText();
                    String destino = destinoField.getText();
                    int capacidadMaxima = Integer.parseInt(capacidadMaximaField.getText());
    
                    AeropuertoDB aeropuertoPartida = (AeropuertoDB) aeropuertoPartidaComboBox.getSelectedItem();
                    AeropuertoDB aeropuertoLlegada = (AeropuertoDB) aeropuertoLlegadaComboBox.getSelectedItem();

                    VueloDB nuevoVuelo = new VueloDB(numeroVuelo, aerolinea, horaSalida, destino, capacidadMaxima,
                            aeropuertoPartida, aeropuertoLlegada, new ArrayList<>());

                    try (Connection connection = DriverManager.getConnection(url, user, password)) {
                        String sql = "INSERT INTO vuelo (numeroVuelo, aerolinea, horaSalida, destino, capacidadMaxima, aeropuertoPartida, aeropuertoLlegada) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setInt(1, nuevoVuelo.getNumeroVuelo());
                            statement.setString(2, nuevoVuelo.getAerolinea());
                            statement.setString(3, nuevoVuelo.getHoraSalida());
                            statement.setString(4, nuevoVuelo.getDestino());
                            statement.setInt(5, nuevoVuelo.getCapacidadMaxima());
                            statement.setInt(6, aeropuertoPartida.getId());
                            statement.setInt(7, aeropuertoLlegada.getId());
    
                            statement.executeUpdate();
                        }
                        System.out.println();
                        JOptionPane.showMessageDialog(null, "Vuelo registrado exitosamente en la base de datos.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println();
                        JOptionPane.showMessageDialog(null, "Error al registrar el vuelo en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese números válidos para Número de Vuelo y Capacidad Máxima.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    
        private static ArrayList<AeropuertoDB> obtenerAeropuertos() {
            ArrayList<AeropuertoDB> aeropuertos = new ArrayList<>();
        
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String sql = "SELECT * FROM aeropuertos";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    try (ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            int id = resultSet.getInt("ID");
                            String nombre = resultSet.getString("nombre");
                            String ubicacion = resultSet.getString("ubicacion");
        
                            AeropuertoDB aeropuerto = new AeropuertoDB(id, nombre, ubicacion);
                            aeropuertos.add(aeropuerto);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
            return aeropuertos;
        }
        
        public static void listarVuelos(ArrayList<AeropuertoDB> aeropuertos) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM vuelo";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.isBeforeFirst()) {
                        JOptionPane.showMessageDialog(null, "No hay vuelos registrados.", "Lista de Vuelos", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    String[] columnNames = {"Número de Vuelo", "Aerolínea", "Hora de Salida", "Destino", "Capacidad Máxima", "Aeropuerto de Partida", "Aeropuerto de Llegada"};
                    DefaultTableModel model = new DefaultTableModel(columnNames, 0);

                    while (resultSet.next()) {
                        int numeroVuelo = resultSet.getInt("numeroVuelo");
                        String aerolinea = resultSet.getString("aerolinea");
                        String horaSalida = resultSet.getString("horaSalida");
                        String destino = resultSet.getString("destino");
                        int capacidadMaxima = resultSet.getInt("capacidadMaxima");
                        int aeropuertoPartidaId = resultSet.getInt("aeropuertoPartida");
                        int aeropuertoLlegadaId = resultSet.getInt("aeropuertoLlegada");

                        AeropuertoDB aeropuertoPartida = obtenerAeropuertoPorID(connection, aeropuertoPartidaId);
                        AeropuertoDB aeropuertoLlegada = obtenerAeropuertoPorID(connection, aeropuertoLlegadaId);

                        if (aeropuertoPartida != null && aeropuertoLlegada != null) {
                            Object[] rowData = {numeroVuelo, aerolinea, horaSalida, destino, capacidadMaxima, aeropuertoPartida.getNombre(), aeropuertoLlegada.getNombre()};
                            model.addRow(rowData);
                        } else {
                            JOptionPane.showMessageDialog(null, "Error: Aeropuerto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }

                    JTable table = new JTable(model);
                    JScrollPane scrollPane = new JScrollPane(table);
                    JOptionPane.showMessageDialog(null, scrollPane, "Lista de Vuelos", JOptionPane.PLAIN_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al listar los vuelos desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

        
        private static AeropuertoDB obtenerAeropuertoPorID(Connection connection, int id) {
            try {
                String sql = "SELECT * FROM aeropuertos WHERE ID = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, id);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            String nombre = resultSet.getString("nombre");
                            String ubicacion = resultSet.getString("ubicacion");
                            return new AeropuertoDB(id, nombre, ubicacion);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
        

        public static void eliminarVueloInterfaz() {
            try {
                String numeroVueloStr = JOptionPane.showInputDialog("Ingrese el número de vuelo a eliminar:");
        
                if (numeroVueloStr != null && !numeroVueloStr.isEmpty()) {
                    int numeroVuelo = Integer.parseInt(numeroVueloStr);
        
                    VueloDB vuelo = buscarVueloPorNumero(numeroVuelo);
        
                    if (vuelo != null) {
                        
                        VueloDB vueloDB = new VueloDB();  
                        vueloDB.setNumeroVuelo(vuelo.getNumeroVuelo());  
                        if (vueloDB.tienePasajerosReservados()) {
                            JOptionPane.showMessageDialog(null, "No se puede eliminar el vuelo. Tiene pasajeros reservados.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                            Object[][] data = {{vuelo.getNumeroVuelo(), vuelo.getAerolinea(), vuelo.getHoraSalida(),
                                    vuelo.getDestino(), vuelo.getCapacidadMaxima()}};
                            String[] columnNames = {"Número de Vuelo", "Aerolínea", "Hora de Salida", "Destino", "Capacidad Máxima"};
        
                            JTable table = new JTable(data, columnNames);
                            JScrollPane scrollPane = new JScrollPane(table);
        
                            JPanel panel = new JPanel(new BorderLayout());
                            panel.add(scrollPane, BorderLayout.CENTER);
        
                            int confirmacion = JOptionPane.showOptionDialog(null, panel,
                                    "Información del Vuelo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                                    null, null, null);
        
                            if (confirmacion == JOptionPane.OK_OPTION) {
                                eliminarVuelo(numeroVuelo);
                                JOptionPane.showMessageDialog(null, "Vuelo eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró un vuelo con el número proporcionado.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Número de vuelo inválido. Ingrese un número entero.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public boolean tienePasajerosReservados() {
            try (Connection connection = getConnection()) {
                String sql = "SELECT COUNT(*) FROM reservar WHERE numeroVuelo = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, this.getNumeroVuelo());
        
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            int count = resultSet.getInt(1);
                            return count > 0;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
        

    public static void eliminarVuelo(int numeroVuelo) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String deleteQuery = "DELETE FROM vuelo WHERE numeroVuelo = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                statement.setInt(1, numeroVuelo);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println();
                    JOptionPane.showMessageDialog(null, "Vuelo eliminado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    
                    JOptionPane.showMessageDialog(null, "No se encontró un vuelo con el número proporcionado.", "error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println();
            JOptionPane.showMessageDialog(null, "Error al eliminar el vuelo desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void actualizarVueloInterfaz() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            
            String numeroVueloInput = JOptionPane.showInputDialog("Ingrese el número de vuelo:");
            if (numeroVueloInput == null || numeroVueloInput.isEmpty()) {
                
                return;
            }
    
            int numeroVuelo = Integer.parseInt(numeroVueloInput);
    
            String selectQuery = "SELECT * FROM vuelo WHERE numeroVuelo = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setInt(1, numeroVuelo);
    
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                    
                        JPanel panel = new JPanel(new BorderLayout());
    
                        DefaultTableModel tableModel = new DefaultTableModel();
                        tableModel.addColumn("Campo");
                        tableModel.addColumn("Valor");

                        tableModel.addRow(new Object[]{"Número de Vuelo", resultSet.getInt("numeroVuelo")});
                        tableModel.addRow(new Object[]{"Aerolínea", resultSet.getString("aerolinea")});
                        tableModel.addRow(new Object[]{"Hora de Salida", resultSet.getString("horaSalida")});
                        tableModel.addRow(new Object[]{"Destino", resultSet.getString("destino")});
                        tableModel.addRow(new Object[]{"Capacidad Máxima", resultSet.getInt("capacidadMaxima")});
    
                        int aeropuertoPartidaIdActual = resultSet.getInt("aeropuertoPartida");
                        int aeropuertoLlegadaIdActual = resultSet.getInt("aeropuertoLlegada");
    
                        
                        AeropuertoDB aeropuertoPartidaActual = buscarAeropuertoPorID(connection, aeropuertoPartidaIdActual);
                        AeropuertoDB aeropuertoLlegadaActual = buscarAeropuertoPorID(connection, aeropuertoLlegadaIdActual);
    
                        tableModel.addRow(new Object[]{"Aeropuerto de Partida", aeropuertoPartidaActual != null ? aeropuertoPartidaActual.getNombre() : "No encontrado"});
                        tableModel.addRow(new Object[]{"Aeropuerto de Llegada", aeropuertoLlegadaActual != null ? aeropuertoLlegadaActual.getNombre() : "No encontrado"});
                        JTable table = new JTable(tableModel);
                        table.setEnabled(false);
    
                        panel.add(new JScrollPane(table), BorderLayout.CENTER);
                        JTextField nuevaAerolineaField = new JTextField();
                        JTextField nuevaHoraSalidaField = new JTextField();
                        JTextField nuevoDestinoField = new JTextField();
                        JTextField nuevaCapacidadMaximaField = new JTextField();
    
                        JComboBox<AeropuertoDB> nuevoAeropuertoPartidaComboBox = new JComboBox<>(obtenerListaAeropuertosDesdeBaseDeDatos(connection).toArray(new AeropuertoDB[0]));
                        JComboBox<AeropuertoDB> nuevoAeropuertoLlegadaComboBox = new JComboBox<>(obtenerListaAeropuertosDesdeBaseDeDatos(connection).toArray(new AeropuertoDB[0]));
    
                        JLabel nuevaAerolineaLabel = new JLabel("Nueva Aerolínea:");
                        JLabel nuevaHoraSalidaLabel = new JLabel("Nueva Hora de Salida:");
                        JLabel nuevoDestinoLabel = new JLabel("Nuevo Destino:");
                        JLabel nuevaCapacidadMaximaLabel = new JLabel("Nueva Capacidad Máxima:");
                        JLabel nuevoAeropuertoPartidaLabel = new JLabel("Nuevo Aeropuerto de Partida:");
                        JLabel nuevoAeropuertoLlegadaLabel = new JLabel("Nuevo Aeropuerto de Llegada:");
    
                        JPanel formPanel = new JPanel(new GridLayout(7, 2));
                        formPanel.add(nuevaAerolineaLabel);
                        formPanel.add(nuevaAerolineaField);
                        formPanel.add(nuevaHoraSalidaLabel);
                        formPanel.add(nuevaHoraSalidaField);
                        formPanel.add(nuevoDestinoLabel);
                        formPanel.add(nuevoDestinoField);
                        formPanel.add(nuevaCapacidadMaximaLabel);
                        formPanel.add(nuevaCapacidadMaximaField);
                        formPanel.add(nuevoAeropuertoPartidaLabel);
                        formPanel.add(nuevoAeropuertoPartidaComboBox);
                        formPanel.add(nuevoAeropuertoLlegadaLabel);
                        formPanel.add(nuevoAeropuertoLlegadaComboBox);
    
                        panel.add(formPanel, BorderLayout.SOUTH);
    
                        int option = JOptionPane.showConfirmDialog(null, panel, "Actualizar Vuelo", JOptionPane.OK_CANCEL_OPTION);
    
                        if (option == JOptionPane.OK_OPTION) {
                            
                            String nuevaAerolinea = nuevaAerolineaField.getText();
                            String nuevaHoraSalida = nuevaHoraSalidaField.getText();
                            String nuevoDestino = nuevoDestinoField.getText();
                            int nuevaCapacidadMaxima = Integer.parseInt(nuevaCapacidadMaximaField.getText());
                            AeropuertoDB nuevoAeropuertoPartida = (AeropuertoDB) nuevoAeropuertoPartidaComboBox.getSelectedItem();
                            AeropuertoDB nuevoAeropuertoLlegada = (AeropuertoDB) nuevoAeropuertoLlegadaComboBox.getSelectedItem();

                            String updateQuery = "UPDATE vuelo SET aerolinea = ?, horaSalida = ?, destino = ?, " +
                                    "capacidadMaxima = ?, aeropuertoPartida = ?, aeropuertoLlegada = ? " +
                                    "WHERE numeroVuelo = ?";
                            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                                updateStatement.setString(1, nuevaAerolinea);
                                updateStatement.setString(2, nuevaHoraSalida);
                                updateStatement.setString(3, nuevoDestino);
                                updateStatement.setInt(4, nuevaCapacidadMaxima);
                                updateStatement.setInt(5, nuevoAeropuertoPartida.getId());
                                updateStatement.setInt(6, nuevoAeropuertoLlegada.getId());
                                updateStatement.setInt(7, numeroVuelo);
    
                                int rowsAffected = updateStatement.executeUpdate();
                                if (rowsAffected > 0) {
                                    JOptionPane.showMessageDialog(null, "Información del vuelo actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "No se encontró un vuelo con el número proporcionado.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Actualización cancelada.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró un vuelo con el número proporcionado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar el vuelo en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static AeropuertoDB buscarAeropuertoPorID(Connection connection, int id) {
        String sql = "SELECT * FROM aeropuertos WHERE ID = ?";
    
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int aeropuertoId = resultSet.getInt("ID");
                    String nombre = resultSet.getString("nombre");
                    String ubicacion = resultSet.getString("ubicacion");
    
                    return new AeropuertoDB(aeropuertoId, nombre, ubicacion);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return null;
    }
    
    public static ArrayList<AeropuertoDB> obtenerListaAeropuertosDesdeBaseDeDatos(Connection connection) {
        ArrayList<AeropuertoDB> aeropuertos = new ArrayList<>();
        String sql = "SELECT * FROM aeropuertos";
    
        try (PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {
    
            while (resultSet.next()) {
                int aeropuertoId = resultSet.getInt("ID");
                String nombre = resultSet.getString("nombre");
                String ubicacion = resultSet.getString("ubicacion");
    
                AeropuertoDB aeropuerto = new AeropuertoDB(aeropuertoId, nombre, ubicacion);
                aeropuertos.add(aeropuerto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return aeropuertos;
    }
    
    
    public static VueloDB obtenerVueloPorNumero(int numeroVuelo) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String selectQuery = "SELECT * FROM vuelo WHERE numeroVuelo = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setInt(1, numeroVuelo);
    
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int numeroVueloActual = resultSet.getInt("numeroVuelo");
                        String aerolineaActual = resultSet.getString("aerolinea");
                        String horaSalidaActual = resultSet.getString("horaSalida");
                        String destinoActual = resultSet.getString("destino");
                        int capacidadMaximaActual = resultSet.getInt("capacidadMaxima");
                        int aeropuertoPartidaIdActual = resultSet.getInt("aeropuertoPartida");
                        int aeropuertoLlegadaIdActual = resultSet.getInt("aeropuertoLlegada");
    
                        AeropuertoDB aeropuertoPartidaActual = buscarAeropuertoPorID(connection, aeropuertoPartidaIdActual);
                        AeropuertoDB aeropuertoLlegadaActual = buscarAeropuertoPorID(connection, aeropuertoLlegadaIdActual);
    
                        return new VueloDB(numeroVueloActual, aerolineaActual, horaSalidaActual, destinoActual,
                                capacidadMaximaActual, aeropuertoPartidaActual, aeropuertoLlegadaActual, new ArrayList<>());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public VueloDB(int numeroVuelo) {
        this.numeroVuelo = numeroVuelo;
    }

    @Override
    public boolean reservarAsiento(int numeroAsiento) {
        if (numeroAsiento >= 1 && numeroAsiento <= capacidadMaxima) {
            if (!asientoReservado(numeroAsiento)) {
                
                asientosReservados.add(numeroAsiento);
                System.out.println("Reserva exitosa. Asiento " + numeroAsiento + " reservado para el pasajero.");
                return true;
            } else {
                System.out.println("El asiento " + numeroAsiento + " ya está reservado. Elija otro asiento.");
            }
        } else {
            System.out.println("Número de asiento inválido. Elija un número de asiento entre 1 y " + capacidadMaxima + ".");
        }
        return false;
    }

    public static boolean vueloExiste(int numeroVuelo, ArrayList<VueloDB> vuelos) {
        for (VueloDB vuelo : vuelos) {
            if (vuelo.getNumeroVuelo() == numeroVuelo) {
                return true;
            }
        }
        return false;
    }

    public boolean asientoReservado(int numeroAsiento) {
        return asientosReservados.contains(numeroAsiento);
    }

    public boolean eliminarReservaAsiento(int numeroAsiento) {
        Integer asiento = Integer.valueOf(numeroAsiento);
        if (asientosReservados.contains(asiento)) {
            asientosReservados.remove(asiento);
            System.out.println("Reserva del asiento " + numeroAsiento + " eliminada.");
            return true;
        } else {
            System.out.println("El asiento " + numeroAsiento + " no está reservado.");
            return false;
        }
    }

    public static VueloDB buscarVueloPorNumero(int numeroVuelo) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM vuelo WHERE numeroVuelo = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, numeroVuelo);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String aerolinea = resultSet.getString("aerolinea");
                        String horaSalida = resultSet.getString("horaSalida");
                        String destino = resultSet.getString("destino");
                        int capacidadMaxima = resultSet.getInt("capacidadMaxima");
                        int aeropuertoPartidaId = resultSet.getInt("aeropuertoPartida");
                        int aeropuertoLlegadaId = resultSet.getInt("aeropuertoLlegada");

                        
                        AeropuertoDB aeropuertoPartida = obtenerAeropuertoPorID(connection, aeropuertoPartidaId);
                        AeropuertoDB aeropuertoLlegada = obtenerAeropuertoPorID(connection, aeropuertoLlegadaId);

                        return new VueloDB(numeroVuelo, aerolinea, horaSalida, destino, capacidadMaxima,
                                aeropuertoPartida, aeropuertoLlegada, new ArrayList<>());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void eliminarReservaAsiento(PasajeroDB pasajero) {
        // TODO Auto-generated method stub
        
    }

    public void setAsientosReservados(ArrayList<Integer> asientosReservados) {
        this.asientosReservados = asientosReservados;
    }


}
