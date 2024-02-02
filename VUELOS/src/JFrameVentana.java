import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JFrameVentana extends JFrame {
    private JLabel labelImagen;

    public JFrameVentana() {

        setTitle("Vuelos");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icono = new ImageIcon("C:\\Users\\Aprendiz\\Downloads\\proyectoInterfaz - final\\vuelos.jpeg" );
        
        labelImagen = new JLabel(icono);
        getContentPane().add(labelImagen, BorderLayout.CENTER);


        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Menú Pasajeros
        JMenu menuPasajeros = new JMenu("Pasajeros");
        JMenuItem itemAgregarP = new JMenuItem("Agregar un pasajero");
        menuPasajeros.add(itemAgregarP);

        itemAgregarP.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            PasajeroDB nuevoPasajero = PasajeroDB.agregarPasajero();

            if (nuevoPasajero != null) {
                System.out.println("Pasajero agregado: " + nuevoPasajero);
            }
        }
        });
        JMenuItem itemListarP = new JMenuItem("Listar pasajeros");
        menuPasajeros.add(itemListarP);
        
        itemListarP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasajeroDB.listarPasajerosEnVentana();
            }
        });
        
        
        JMenuItem itemEliminarP = new JMenuItem("Eliminar pasajeros");
        menuPasajeros.add(itemEliminarP);
        
        itemEliminarP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String numeroPasaporte = JOptionPane.showInputDialog("Ingrese el número de pasaporte a eliminar:");
        
                if (numeroPasaporte != null && !numeroPasaporte.isEmpty()) {
                    
                    PasajeroDB pasajero = PasajeroDB.buscarPasajeroPorPasaporte(numeroPasaporte);
        
                    if (pasajero != null && pasajero.tieneReservas()) {
                        int opcion = JOptionPane.showConfirmDialog(null,
                                "El pasajero tiene reservas de asientos. ¿Desea eliminar las reservas y al pasajero?",
                                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        
                        if (opcion == JOptionPane.YES_OPTION) {
                            
                            pasajero.eliminarReservaAsiento();
        
                            
                            pasajero = PasajeroDB.buscarPasajeroPorPasaporte(numeroPasaporte);
        
                            if (pasajero == null) {
                                JOptionPane.showMessageDialog(null, "Pasajero eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            } else {
                                JOptionPane.showMessageDialog(null, "Error al eliminar las reservas del pasajero.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No se eliminaron reservas ni al pasajero.", "Operación Cancelada", JOptionPane.INFORMATION_MESSAGE);
                            return; 
                        }
                    }
        
                    
                    boolean eliminacionExitosa = PasajeroDB.eliminarPasajero(numeroPasaporte);
        
                    if (eliminacionExitosa) {
                        JOptionPane.showMessageDialog(null, "Pasajero eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al eliminar el pasajero.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Número de pasaporte no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        

        JMenuItem itemActualizarP = new JMenuItem("Actualizar pasajeros");
        menuPasajeros.add(itemActualizarP);
        itemActualizarP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasajeroDB.actualizarPasajeroDesdeVentana();
            }
        });

        // Submenú Reservar y Calcular
        JMenu submenuNuevo = new JMenu("Reservar y Calcular");
        JMenuItem itemReservarAsiento = new JMenuItem("Reservar un asiento");
        submenuNuevo.add(itemReservarAsiento);
        itemReservarAsiento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasajeroDB.mostrarFormularioReserva();
            }
        });


        JMenuItem itemCalcularPrecio = new JMenuItem("Calcular precio de reserva");
        submenuNuevo.add(itemCalcularPrecio);
        
        itemCalcularPrecio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasajeroDB.mostrarVentanaCalcularPrecio();
            }
        });
        
        JMenuItem itemMostrarReservas = new JMenuItem("Mostrar  reservas");
        submenuNuevo.add(itemMostrarReservas);
        itemMostrarReservas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PasajeroDB.mostrarReservas();
            }
        });
        
        

        menuPasajeros.add(submenuNuevo);
        menuPasajeros.addSeparator();
        JMenuItem itemSalir = new JMenuItem("Salir");

        itemSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuPasajeros.add(itemSalir);
        // menu aeropuertos
        JMenu menuAeropuerto= new JMenu("Aeropuertos");
        JMenuItem itemAgregarA = new JMenuItem("Agregar un aeropuerto");
        menuAeropuerto.add(itemAgregarA);
        itemAgregarA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AeropuertoDB.agregarAeropuerto();
            }
        });
        

        JMenuItem itemListarA = new JMenuItem("listar aeropuertos");
        menuAeropuerto.add(itemListarA);
        itemListarA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AeropuertoDB.mostrarListaAeropuertos();
            }
        });
        

        JMenuItem itemEliminarA = new JMenuItem("Eliminar un aeropuerto");
        menuAeropuerto.add(itemEliminarA);
        itemEliminarA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputID = JOptionPane.showInputDialog(null, "Ingrese el ID del aeropuerto a eliminar:", "Eliminar Aeropuerto", JOptionPane.QUESTION_MESSAGE);
                if (inputID != null && !inputID.isEmpty()) {
                    try {
                        int id = Integer.parseInt(inputID);
                        AeropuertoDB.eliminarAeropuerto(id);;
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JMenuItem itemActualizarA = new JMenuItem("Aactualizar un aeropuerto");
        menuAeropuerto.add(itemActualizarA);
        itemActualizarA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String idString = JOptionPane.showInputDialog("Ingrese el ID del aeropuerto a actualizar:");
                
                try {

                    int id = Integer.parseInt(idString);
                    AeropuertoDB.actualizarAeropuerto(id);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID inválido. Ingrese un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        

        JMenu menuVuelos= new JMenu("vuelos");
        JMenuItem itemAgregarV = new JMenuItem("Agregar un vuelo");
        menuVuelos.add(itemAgregarV);
        itemAgregarV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                VueloDB.agregarVuelo();
            }
        });

        JMenuItem itemListarV = new JMenuItem("listar vuelos");
        menuVuelos.add(itemListarV);
        itemListarV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                VueloDB.listarVuelos(null);
            }
        });


        JMenuItem itemEliminarV = new JMenuItem("Eliminar un vuelo");
        menuVuelos.add(itemEliminarV);
        itemEliminarV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                VueloDB.eliminarVueloInterfaz();
            }
        });


        JMenuItem itemActualizarV = new JMenuItem("Actualizar un vuelo");
        menuVuelos.add(itemActualizarV);
        itemActualizarV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                VueloDB.actualizarVueloInterfaz();
            }
        });



        
        menuBar.add(menuPasajeros);
        menuBar.add(menuAeropuerto);
        menuBar.add(menuVuelos);

        setVisible(true);
    }

    

    public static void main(String[] args) {
        new JFrameVentana();
    }
}
