import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;


public class AeropuertoDB {
    private int id;
    private String nombre;
    private String ubicacion;

    private static final String url = "jdbc:mysql://localhost:3306/vuelos?serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = "S3n42023*";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public AeropuertoDB(int id, String nombre, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }
    

    public AeropuertoDB(String nombre, String ubicacion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

public static void agregarAeropuerto() {
    try {
        
        JTextField nombreField = new JTextField();
        JTextField ubicacionField = new JTextField();

        Object[] message = {
                "Nombre del aeropuerto:", nombreField,
                "Ubicación del aeropuerto:", ubicacionField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Agregar Aeropuerto", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
        
            String nombreAeropuerto = nombreField.getText();
            String ubicacionAeropuerto = ubicacionField.getText();

            
            AeropuertoDB nuevoAeropuerto = new AeropuertoDB(nombreAeropuerto, ubicacionAeropuerto);

        
            String sql = "INSERT INTO aeropuertos (nombre, ubicacion) VALUES (?, ?)";
            try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, nuevoAeropuerto.getNombre());
                statement.setString(2, nuevoAeropuerto.getUbicacion());
                statement.executeUpdate();

                
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        nuevoAeropuerto.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("No se pudo obtener el ID generado.");
                    }
                }
            }

            JOptionPane.showMessageDialog(null, "Aeropuerto creado exitosamente con ID: " + nuevoAeropuerto.getId());
        } else {
            JOptionPane.showMessageDialog(null, "Operación cancelada.");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

    public static void mostrarListaAeropuertos() {
        try (Connection connection = getConnection()) {
            String sql = "SELECT * FROM aeropuertos";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        JOptionPane.showMessageDialog(null, "No hay aeropuertos registrados.", "Lista de Aeropuertos", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }


                    DefaultTableModel tableModel = new DefaultTableModel();
                    tableModel.addColumn("ID");
                    tableModel.addColumn("Nombre");
                    tableModel.addColumn("Ubicación");

                    
                    do {
                        Object[] rowData = {
                                resultSet.getInt("ID"),
                                resultSet.getString("nombre"),
                                resultSet.getString("ubicacion")
                        };
                        tableModel.addRow(rowData);
                    } while (resultSet.next());

                    
                    JTable table = new JTable(tableModel);

                    
                    JScrollPane scrollPane = new JScrollPane(table);

                    
                    JFrame frame = new JFrame("Lista de Aeropuertos");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
                    frame.setSize(400, 200);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return nombre; 
    }

    public static void eliminarAeropuerto(int id) {
        try (Connection connection = getConnection()) {
            if (aeropuertoAsociadoAUnVuelo(connection, id)) {
                JOptionPane.showMessageDialog(null, "No se puede eliminar el aeropuerto porque está asociado a un vuelo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "SELECT * FROM aeropuertos WHERE ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String nombre = resultSet.getString("nombre");
                        String ubicacion = resultSet.getString("ubicacion");
    
  
                        JFrame frame = new JFrame("Eliminar Aeropuerto");
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
                        JPanel panel = new JPanel(new GridLayout(3, 2));
                        panel.add(new JLabel("ID:"));
                        panel.add(new JLabel(Integer.toString(id)));
                        panel.add(new JLabel("Nombre:"));
                        panel.add(new JTextField(nombre));
                        panel.add(new JLabel("Ubicación:"));
                        panel.add(new JTextField(ubicacion));
    
                        int option = JOptionPane.showConfirmDialog(frame, panel, "Eliminar Aeropuerto", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            
                            String updatedNombre = ((JTextField) panel.getComponent(3)).getText();
                            String updatedUbicacion = ((JTextField) panel.getComponent(5)).getText();
    
                            sql = "DELETE FROM aeropuertos WHERE ID = ?";
                            try (PreparedStatement deleteStatement = connection.prepareStatement(sql)) {
                                deleteStatement.setInt(1, id);
                                deleteStatement.executeUpdate();
                            }
    
                            System.out.println("Aeropuerto eliminado exitosamente.");
                            JOptionPane.showMessageDialog(null, "Aeropuerto eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            System.out.println("Eliminación cancelada.");
                            JOptionPane.showMessageDialog(null, "Eliminación cancelada.", "Cancelar", JOptionPane.CANCEL_OPTION);
                        }
                    } else {
                        System.out.println("No se encontró un aeropuerto con el ID proporcionado.");
                        JOptionPane.showMessageDialog(null, "No se encontró un aeropuerto con el ID proporcionado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean aeropuertoAsociadoAUnVuelo(Connection connection, int aeropuertoId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM vuelo WHERE aeropuertoPartida = ? OR aeropuertoLlegada = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, aeropuertoId);
            statement.setInt(2, aeropuertoId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }
    

    public static void actualizarAeropuerto(int id) {
        try (Connection connection = getConnection()) {
            AeropuertoDB aeropuertoSeleccionado = null;
            String sql = "SELECT * FROM aeropuertos WHERE ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int aeropuertoId = resultSet.getInt("ID");
                        String nombre = resultSet.getString("nombre");
                        String ubicacion = resultSet.getString("ubicacion");
    
                        aeropuertoSeleccionado = new AeropuertoDB(aeropuertoId, nombre, ubicacion);
                    }
                }
            }
    
            if (aeropuertoSeleccionado != null) {
                
                JPanel panel = new JPanel(new BorderLayout());
    
                
                DefaultTableModel tableModel = new DefaultTableModel();
                tableModel.addColumn("Campo");
                tableModel.addColumn("Valor");
                tableModel.addRow(new Object[]{"ID", aeropuertoSeleccionado.getId()});
                tableModel.addRow(new Object[]{"Nombre", aeropuertoSeleccionado.getNombre()});
                tableModel.addRow(new Object[]{"Ubicación", aeropuertoSeleccionado.getUbicacion()});
    
                
                JTable table = new JTable(tableModel);
                table.setEnabled(false); 
    
                
                panel.add(new JScrollPane(table), BorderLayout.CENTER);
    
                JTextField nuevoNombreField = new JTextField(aeropuertoSeleccionado.getNombre());
                JTextField nuevaUbicacionField = new JTextField(aeropuertoSeleccionado.getUbicacion());
    
                JLabel nuevoNombreLabel = new JLabel("Nuevo Nombre:");
                JLabel nuevaUbicacionLabel = new JLabel("Nueva Ubicación:");
                JPanel formPanel = new JPanel(new GridLayout(2, 2));
                formPanel.add(nuevoNombreLabel);
                formPanel.add(nuevoNombreField);
                formPanel.add(nuevaUbicacionLabel);
                formPanel.add(nuevaUbicacionField);
    

                panel.add(formPanel, BorderLayout.SOUTH);
    
                
                int option = JOptionPane.showConfirmDialog(null, panel, "Actualizar Aeropuerto", JOptionPane.OK_CANCEL_OPTION);
    
                if (option == JOptionPane.OK_OPTION) {
                    
                    String nuevoNombre = nuevoNombreField.getText();
                    String nuevaUbicacion = nuevaUbicacionField.getText();
    
                    
                    aeropuertoSeleccionado.setNombre(nuevoNombre);
                    aeropuertoSeleccionado.setUbicacion(nuevaUbicacion);
    
                    
                    sql = "UPDATE aeropuertos SET nombre = ?, ubicacion = ? WHERE ID = ?";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, nuevoNombre);
                        statement.setString(2, nuevaUbicacion);
                        statement.setInt(3, aeropuertoSeleccionado.getId());
                        statement.executeUpdate();
                    }
    
                    JOptionPane.showMessageDialog(null, "Información actualizada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Actualización cancelada.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró un aeropuerto con el ID proporcionado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static boolean aeropuertoExiste( ArrayList<AeropuertoDB> aeropuertos, Connection connection) {
        try {
            String sql = "SELECT * FROM aeropuertos WHERE ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static AeropuertoDB buscarAeropuertoPorID(int id) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
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
    

}
