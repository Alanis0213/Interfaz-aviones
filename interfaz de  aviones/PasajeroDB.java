import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;



public class PasajeroDB implements IPagable  {
    private String nombre;
    private String apellido;
    private String numeroIdentificacion;
    private String numeroPasaporte;
    private double cantidadEquipaje;
    private ArrayList<VueloDB> vuelosReservados  = new ArrayList<>();
    private  ArrayList<PasajeroDB> pasajeros; 

    private static final String url="jdbc:mysql://localhost:3306/vuelos?serverTimezone=UTC";
    private static final String user="root";
    private static final String password="S3n42023*";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public PasajeroDB() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getNumeroPasaporte() {
        return numeroPasaporte;
    }

    public void setNumeroPasaporte(String numeroPasaporte) {
        this.numeroPasaporte = numeroPasaporte;
    }

    public double getCantidadEquipaje() {
        return cantidadEquipaje;
    }

    public void setCantidadEquipaje(double cantidadEquipaje) {
        this.cantidadEquipaje = cantidadEquipaje;
    }

    public ArrayList<VueloDB> getVuelosReservados() {
        return vuelosReservados;
    }

    public void setVuelosReservados(ArrayList<VueloDB> vuelosReservados) {
        this.vuelosReservados = vuelosReservados;
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

    public PasajeroDB(String nombre, String apellido, String numeroIdentificacion, String numeroPasaporte,
            double cantidadEquipaje, ArrayList<VueloDB> vuelosReservados, ArrayList<PasajeroDB> pasajeros) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroIdentificacion = numeroIdentificacion;
        this.numeroPasaporte = numeroPasaporte;
        this.cantidadEquipaje = cantidadEquipaje;
        this.vuelosReservados = vuelosReservados;
        this.pasajeros = pasajeros;
    }

    public PasajeroDB(String numeroPasaporte, String numeroIdentificacion, String nombre, String apellido, double cantidadEquipaje) {
        this.numeroPasaporte = numeroPasaporte;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cantidadEquipaje = cantidadEquipaje;
        this.vuelosReservados = new ArrayList<>();
    }
    
    public static PasajeroDB agregarPasajero() {
    JTextField pasaporteField = new JTextField();
    JTextField identificacionField = new JTextField();
    JTextField nombreField = new JTextField();
    JTextField apellidoField = new JTextField();
    JTextField equipajeField = new JTextField();

    Object[] message = {
            "Pasaporte:", pasaporteField,
            "Identificación:", identificacionField,
            "Nombre:", nombreField,
            "Apellido:", apellidoField,
            "cantidad Equipaje:", equipajeField
    };

    int option = JOptionPane.showConfirmDialog(null, message, "Agregar Pasajero", JOptionPane.OK_CANCEL_OPTION);

    if (option == JOptionPane.OK_OPTION) {

        String numeroPasaporte = pasaporteField.getText();
        String numeroIdentificacion = identificacionField.getText();
        String nombre = nombreField.getText();
        String apellido = apellidoField.getText();
        double equipaje = Double.parseDouble(equipajeField.getText());

        if (pasajeroExiste(numeroPasaporte)) {
            JOptionPane.showMessageDialog(null, "El número de pasaporte ya existe. No se puede agregar el pasajero.");
            return null;
        }

        PasajeroDB nuevoPasajero = new PasajeroDB(numeroPasaporte,numeroIdentificacion,nombre,apellido, equipaje);
        nuevoPasajero.setNumeroPasaporte(numeroPasaporte);

        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO pasajeros (pasaporte, numeroIdentidad, nombre, apellido, equipaje) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nuevoPasajero.getNumeroPasaporte());
                statement.setString(2, nuevoPasajero.getNumeroIdentificacion());
                statement.setString(3, nuevoPasajero.getNombre());
                statement.setString(4, nuevoPasajero.getApellido());
                statement.setDouble(5, nuevoPasajero.getCantidadEquipaje());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        JOptionPane.showMessageDialog(null, "Se agrego el pasajero", "Exito", JOptionPane.INFORMATION_MESSAGE);
        return nuevoPasajero;
    }

    return null;
}

    public static void listarPasajerosEnVentana() {
    try (Connection connection = getConnection()) {
        String sql = "SELECT * FROM pasajeros";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    JOptionPane.showMessageDialog(null, "No hay pasajeros registrados.", "Lista de Pasajeros", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                String[] columnas = {"Número de Pasaporte", "Número de Identificación", "Nombre", "Apellido"};
                DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

                do {
                    String numeroPasaporte = resultSet.getString("pasaporte");                   
                    String numeroIdentificacion = resultSet.getString("numeroIdentidad");
                    String nombre = resultSet.getString("nombre");
                    String apellido = resultSet.getString("apellido");

                    modelo.addRow(new Object[]{numeroPasaporte, numeroIdentificacion, nombre, apellido,});
                } while (resultSet.next());

                JTable tabla = new JTable(modelo);
                JOptionPane.showMessageDialog(null, new JScrollPane(tabla), "Lista de Pasajeros", JOptionPane.PLAIN_MESSAGE);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public boolean eliminarReservaAsiento() {
    String numeroAsientoStr = JOptionPane.showInputDialog("Ingrese el número de asiento:");

    if (numeroAsientoStr != null && !numeroAsientoStr.isEmpty()) {
        try {
            int numeroAsiento = Integer.parseInt(numeroAsientoStr);

            if (verificarAsientoReservadoEnBD(numeroAsiento)) {
                int respuesta = JOptionPane.showConfirmDialog(null,
                        "¿Desea eliminar la reserva del asiento " + numeroAsiento + "?", "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (respuesta == JOptionPane.YES_OPTION) {
                    
                    try (Connection connection = getConnection()) {
                        String sql = "DELETE FROM reservar WHERE numeroAsiento = ?";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setInt(1, numeroAsiento);
                            statement.executeUpdate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al eliminar la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    JOptionPane.showMessageDialog(null, "Reserva del asiento " + numeroAsiento + " eliminada exitosamente.",
                            "Éxito al eliminar Reserva", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Eliminación de la reserva cancelada.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "El asiento " + numeroAsiento + " no está reservado por ningún pasajero.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ingrese un número válido para el número de asiento.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    } else {
        JOptionPane.showMessageDialog(null, "Ingrese un valor válido para el número de asiento.", "Error",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }
}

    public static boolean eliminarPasajero(String numeroPasaporte) {
        try (Connection connection = PasajeroDB.getConnection()) {
            String sql = "SELECT * FROM pasajeros WHERE pasaporte = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, numeroPasaporte);
                ResultSet resultSet = statement.executeQuery();
    
                if (resultSet.next()) {
                    PasajeroDB pasajero = new PasajeroDB(
                            resultSet.getString("pasaporte"),
                            resultSet.getString("numeroIdentidad"),
                            resultSet.getString("nombre"),
                            resultSet.getString("apellido"),
                            resultSet.getDouble("Equipaje")
                    );
    
                    if (pasajero.tieneReservas()) {
                        int opcion = JOptionPane.showConfirmDialog(null,
                                "El pasajero tiene reservas de asientos. ¿Desea eliminar las reservas y al pasajero?",
                                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    
                        if (opcion == JOptionPane.YES_OPTION) {
                            sql = "DELETE FROM reservar WHERE pasaporte = ?";
                            try (PreparedStatement statementEliminar = connection.prepareStatement(sql)) {
                                statementEliminar.setString(1, pasajero.getNumeroPasaporte());
                                statementEliminar.executeUpdate();
                            }
                        } else {
                            return false; 
                        }
                    }
    
                    sql = "DELETE FROM pasajeros WHERE pasaporte = ?";
                    try (PreparedStatement statementEliminar = connection.prepareStatement(sql)) {
                        statementEliminar.setString(1, pasajero.getNumeroPasaporte());
                        statementEliminar.executeUpdate();
                        return true; 
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se encontró un pasajero con el número de pasaporte proporcionado.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return false; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }
    
    public static void actualizarPasajeroDesdeVentana() {

        String numeroPasaporte = JOptionPane.showInputDialog("Ingrese el número de pasaporte del pasajero a actualizar:");

        if (numeroPasaporte != null && !numeroPasaporte.isEmpty()) {

            PasajeroDB pasajero = PasajeroDB.buscarPasajeroPorPasaporte(numeroPasaporte);

            if (pasajero != null) {
                // Crear una ventana emergente para la actualización
                JTextField nuevoNumeroIdentificacionField = new JTextField(pasajero.getNumeroIdentificacion());
                JTextField nuevoNombreField = new JTextField(pasajero.getNombre());
                JTextField nuevoApellidoField = new JTextField(pasajero.getApellido());
                JTextField nuevoEquipajeField = new JTextField(Double.toString(pasajero.getCantidadEquipaje()));

                Object[] message = {
                        "Nuevo número de identificación:", nuevoNumeroIdentificacionField,
                        "Nuevo nombre:", nuevoNombreField,
                        "Nuevo apellido:", nuevoApellidoField,
                        "Nuevo equipaje:", nuevoEquipajeField
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Actualizar Pasajero", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    try {
                        // Obtener los valores ingresados por el usuario
                        String nuevoNumeroIdentificacion = nuevoNumeroIdentificacionField.getText();
                        String nuevoNombre = nuevoNombreField.getText();
                        String nuevoApellido = nuevoApellidoField.getText();
                        double nuevoEquipaje = Double.parseDouble(nuevoEquipajeField.getText());

                        // Actualizar la información del pasajero
                        pasajero.actualizarInformacionPasajero(numeroPasaporte, nuevoNumeroIdentificacion, nuevoNombre, nuevoApellido, nuevoEquipaje);

                        JOptionPane.showMessageDialog(null, "Información del pasajero actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Formato de equipaje no válido. Ingrese un número.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un pasajero con el número de pasaporte proporcionado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Número de pasaporte no válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void actualizarInformacionPasajero(String numeroPasaporte, String nuevoNumeroIdentificacion, String nuevoNombre, String nuevoApellido, double nuevoEquipaje) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE pasajeros SET numeroIdentidad = ?, nombre = ?, apellido = ?, equipaje=? WHERE pasaporte = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nuevoNumeroIdentificacion);
                statement.setString(2, nuevoNombre);
                statement.setString(3, nuevoApellido);
                statement.setDouble(4, nuevoEquipaje);
                statement.setString(5, numeroPasaporte);
                statement.executeUpdate();
            }

            System.out.println("Información actualizada exitosamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static PasajeroDB buscarPasajeroPorPasaporte( String numeroPasaporte) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT * FROM pasajeros WHERE pasaporte = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, numeroPasaporte);
    
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String id = resultSet.getString("numeroIdentidad");
                        String nombre = resultSet.getString("nombre");
                        String apellido = resultSet.getString("apellido");
                        Double cantidadEquipaje = resultSet.getDouble("equipaje");
                        String numeroPasaporteDB = resultSet.getString("pasaporte");

                        return new PasajeroDB(id, nombre, apellido, numeroPasaporteDB, cantidadEquipaje);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean tieneReservas() {
        if (getNumeroPasaporte() != null) {
            try (Connection connection = getConnection()) {
                String sql = "SELECT COUNT(*) FROM reservar WHERE pasaporte = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, getNumeroPasaporte());
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
        }
        return false;
    }
    
    
    private static boolean pasajeroExiste(String numeroPasaporte) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT COUNT(*) FROM pasajeros WHERE pasaporte = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, numeroPasaporte);
    
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
    

    @Override
    public double calcularReserva() {
        return 100 + (cantidadEquipaje * 10);
    }

    public boolean verificarAsientoReservadoEnBD(int numeroAsiento) {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM reservar WHERE numeroAsiento = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, numeroAsiento);
    
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next(); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    

    
    public double calcularPrecioReserva() {
        return 100 + (getCantidadEquipaje() * 10);
    }
    public static void mostrarVentanaCalcularPrecio() {
        JFrame frame = new JFrame("Calcular Precio de Reserva");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        JPanel panel = new JPanel(new GridLayout(2, 2));
    
        JTextField pasaporteField = new JTextField();
        panel.add(new JLabel("Número de Pasaporte:"));
        panel.add(pasaporteField);
    
        int option = JOptionPane.showConfirmDialog(frame, panel, "Calcular Precio de Reserva", JOptionPane.OK_CANCEL_OPTION);
    
        if (option == JOptionPane.OK_OPTION) {
            String numeroPasaporte = pasaporteField.getText();
    
            
            PasajeroDB pasajero = PasajeroDB.buscarPasajeroPorPasaporte(numeroPasaporte);
    
            if (pasajero != null) {
                double precioReserva = pasajero.calcularPrecioReserva();
                JOptionPane.showMessageDialog(frame, "Precio de Reserva: $" + precioReserva, "Precio de Reserva", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Pasajero no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    
        frame.dispose(); 
    }
    
    public static void mostrarFormularioReserva() {
        JFrame frame = new JFrame("Reservar Asiento");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        JPanel panel = new JPanel(new GridLayout(4, 2));
    
        JTextField vueloField = new JTextField();
        JTextField pasaporteField = new JTextField();
        JTextField asientoField = new JTextField();
    
        panel.add(new JLabel("Número de Vuelo:"));
        panel.add(vueloField);
        panel.add(new JLabel("Número de Pasaporte:"));
        panel.add(pasaporteField);
        panel.add(new JLabel("Número de Asiento:"));
        panel.add(asientoField);
    
        Object[] options = {"OK", "Cancelar"};
    
        int result = JOptionPane.showOptionDialog(frame, panel, "Reservar Asiento",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, null);
    
        if (result == JOptionPane.OK_OPTION) {
            try {
                int numeroVuelo = Integer.parseInt(vueloField.getText());
                String numeroPasaporte = pasaporteField.getText();
                int numeroAsiento = Integer.parseInt(asientoField.getText());
    
                PasajeroDB pasajero = new PasajeroDB();
                VueloDB vuelo = VueloDB.buscarVueloPorNumero(numeroVuelo);
    
                boolean reservaExitosa = pasajero.reservarAsiento(vuelo, numeroAsiento, numeroPasaporte);
    
                if (reservaExitosa) {
                    JOptionPane.showMessageDialog(frame, "Reserva exitosa.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Error al reservar asiento.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            frame.dispose(); 
        }
    }
    
    public boolean reservarAsiento(VueloDB vuelo, int numeroAsiento, String numeroPasaporte) {
        if (vuelo != null) {
            try (Connection connection = getConnection()) {
                
                PasajeroDB pasajero = buscarPasajeroPorPasaporte(numeroPasaporte);
                if (pasajero == null) {
                    JOptionPane.showMessageDialog(null, "El pasaporte del pasajero no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if (verificarAsientoDisponibleEnBD(vuelo, numeroAsiento)) {
                    String sql = "INSERT INTO reservar (numeroVuelo, numeroAsiento, pasaporte) VALUES (?, ?, ?)";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setInt(1, vuelo.getNumeroVuelo());
                        statement.setInt(2, numeroAsiento);
                        statement.setString(3, numeroPasaporte);
    
                        statement.executeUpdate();
                    }
                    vuelosReservados.add(vuelo);
                    JOptionPane.showMessageDialog(null, "Reserva exitosa. Asiento " + numeroAsiento + " reservado para el pasajero.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Reserva exitosa. Asiento " + numeroAsiento + " reservado para el pasajero.");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "El asiento " + numeroAsiento + " no está disponible. Elija otro asiento.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println();
            JOptionPane.showMessageDialog(null, "El vuelo no existe.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    private static boolean verificarAsientoDisponibleEnBD(VueloDB vuelo, int numeroAsiento) throws SQLException {
        String sql = "SELECT * FROM reservar WHERE numeroVuelo = ? AND numeroAsiento = ?";
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, vuelo.getNumeroVuelo());
            statement.setInt(2, numeroAsiento);
            try (ResultSet resultSet = statement.executeQuery()) {
                return !resultSet.next();
            }
        }
    }
    
    
    public static void mostrarReservas() {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM reservar";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

                DefaultTableModel tableModel = new DefaultTableModel();
                JTable table = new JTable(tableModel);

                tableModel.addColumn("Número de Vuelo");
                tableModel.addColumn("Número de Asiento");
                tableModel.addColumn("Pasaporte");

                while (resultSet.next()) {
                    int numeroVuelo = resultSet.getInt("numeroVuelo");
                    int numeroAsiento = resultSet.getInt("numeroAsiento");
                    String pasaporte = resultSet.getString("pasaporte");

                    tableModel.addRow(new Object[]{numeroVuelo, numeroAsiento, pasaporte});
                }

                JFrame frame = new JFrame("Reservas");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new JScrollPane(table), BorderLayout.CENTER);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
