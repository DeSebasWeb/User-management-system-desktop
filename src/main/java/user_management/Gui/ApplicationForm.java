package user_management.Gui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import user_management.modelo.Usuario;
import user_management.servicio.IUsuarioServicio;
import user_management.servicio.UsuarioServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class ApplicationForm extends JFrame{
    private JPanel panelPrincipal;
    private JTable usuariosTabla;
    private JTextField campoCedula;
    private JTextField campoNombre;
    private JTextField campoApellido;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton buscarButton;
    private JButton mostrarUsuariosButton;
    private JButton limpiarButton;
    private JComboBox tipoMembresia;
    private DefaultTableModel tablaModeloUsuarios;
    IUsuarioServicio usuarioServicio;
    private Integer idUsuario;
    private Usuario usuarioTemp;

    @Autowired
    public ApplicationForm(UsuarioServicio usuarioServicio){
        this.usuarioServicio= usuarioServicio;
        iniciarForma();

        usuariosTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                rellenarCamposUsuario();
            }
        });
        guardarButton.addActionListener(e -> {
            guardarUsuario();
        });
        eliminarButton.addActionListener(e -> {
            eliminarUsuario();
        });
        buscarButton.addActionListener(e -> {
            buscarUsuario();
        });
    }

    private void iniciarForma(){
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }

    private void createUIComponents() {
        this.tablaModeloUsuarios = new DefaultTableModel(0,5){
          @Override
          public boolean isCellEditable(int row, int column){
              return false;
          }
        };
        String[] cabecero = {"Id","Cedula", "Nombre", "Apellido", "Membresia"};
        this.tablaModeloUsuarios.setColumnIdentifiers(cabecero);
        this.usuariosTabla = new JTable(tablaModeloUsuarios);
        this.usuariosTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mostrarUsuariosTabla();
    }

    private void mostrarUsuariosTabla(){
        this.tablaModeloUsuarios.setRowCount(0);
        var usuarios = this.usuarioServicio.mostrarUsuarios();
        usuarios.forEach(usuario ->{
            Object[] renglonUsuario = {usuario.getId(), usuario.getCedula(), usuario.getNombre(), usuario.getApellido(), usuario.getMembresia()};
            this.tablaModeloUsuarios.addRow(renglonUsuario);
        });
    }

    private void rellenarCamposUsuario(){
        var campo = usuariosTabla.getSelectedRow();
        if(campo != -1){
            var id = usuariosTabla.getModel().getValueAt(campo, 0).toString();
            this.idUsuario = Integer.parseInt(id);
            var cedula = usuariosTabla.getModel().getValueAt(campo, 1).toString();
            this.campoCedula.setText(cedula);
            var nombre = usuariosTabla.getModel().getValueAt(campo, 2).toString();
            this.campoNombre.setText(nombre);
            var apellido = usuariosTabla.getModel().getValueAt(campo, 3).toString();
            this.campoApellido.setText(apellido);
            var membresia = usuariosTabla.getModel().getValueAt(campo, 4).toString();
            this.tipoMembresia.setSelectedItem(membresia);
            this.usuarioTemp = new Usuario(this.idUsuario, Integer.parseInt(cedula), nombre, apellido, membresia);
        }
    }

    private void guardarUsuario(){
        if (this.campoCedula.equals("")){
            mostrarMensaje("Ingrese un numero de cedula");
        }
        if(this.campoNombre.equals("")){
            mostrarMensaje("Ingrese el nombre del usuario");
        }

        var cedulaUsuario = Integer.parseInt(this.campoCedula.getText());
        var nombreUsuario = this.campoNombre.getText();
        var apellidoUsuario = this.campoApellido.getText();
        var membresiaUsuario = this.tipoMembresia.getSelectedItem().toString();
        Usuario usuario = new Usuario(this.idUsuario, cedulaUsuario, nombreUsuario, apellidoUsuario, membresiaUsuario);

        this.usuarioServicio.guardarUsuario(usuario);

        if (this.idUsuario == null){
            mostrarMensaje("Se ha creado exitosamente el usuario");
        } else{
            mostrarMensaje("Se ha actualizado el cliente exitosamente");
        }
        limpiarDatos();
        mostrarUsuariosTabla();

    }

    private void limpiarDatos(){
        this.campoCedula.setText("");
        this.campoNombre.setText("");
        this.campoApellido.setText("");
        this.tipoMembresia.setSelectedItem("Basico");
        this.idUsuario = null;
        this.usuarioTemp = null;
        this.usuariosTabla.getSelectionModel().clearSelection();
    }

    private void mostrarMensaje(String texto){
        JOptionPane.showMessageDialog(this,texto);
    }

    private void eliminarUsuario(){
        if (this.usuarioTemp != null){
            this.usuarioServicio.eliminarUsuario(this.usuarioTemp);
            mostrarMensaje("Cliente eliminado con exito.");
            mostrarUsuariosTabla();
            limpiarDatos();
        }else {
            mostrarMensaje("No se ha seleccionado ningun usuario a eliminar\nPor favor seleccione uno.");
        }
    }

    private void buscarUsuario(){
        if(this.campoCedula.equals("")){
            mostrarMensaje("Escriba la cedula del usuario a consultar.");
        }else{
            Integer cedulaUsuario = Integer.parseInt(this.campoCedula.getText());
            Usuario mostrarUsuario = this.usuarioServicio.buscarUsuario(cedulaUsuario);
            if (mostrarUsuario == null){
                mostrarMensaje("El cliente que busca no existe, intente nuevamente.");
            }else {
                this.tablaModeloUsuarios.setRowCount(0);
                Object[] renglon = {mostrarUsuario.getId(), mostrarUsuario.getCedula(), mostrarUsuario.getNombre(), mostrarUsuario.getApellido(), mostrarUsuario.getMembresia()};
                this.tablaModeloUsuarios.addRow(renglon);
            }
        }
    }
}
