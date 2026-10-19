package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import ui.theme.Tarjeta;
import ui.theme.Theme;

/** Aviso flotante abajo a la derecha que se cierra solo. Para éxitos y avisos; los errores siguen como diálogo. */
public final class Toast {

    private static final int DURACION_MS = 2500;
    private static final int MARGEN = 24;
    private static JWindow actual;            // solo un toast a la vez

    private Toast() { }

    public static void exito(Component origen, String mensaje) { mostrar(origen, mensaje, Theme.SALVIA, "✓"); }
    public static void aviso(Component origen, String mensaje) { mostrar(origen, mensaje, Theme.MOSTAZA, "!"); }

    private static void mostrar(Component origen, String mensaje, Color fondo, String simbolo) {
        Window dueño = (origen instanceof Window w) ? w
                     : (origen != null ? SwingUtilities.getWindowAncestor(origen) : null);
        if (dueño == null) dueño = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        if (actual != null) actual.dispose();           // reemplaza el anterior

        // Contenido: una Tarjeta (borde + sombra dura) del color del tipo
        Tarjeta caja = new Tarjeta(new BorderLayout(12, 0));
        caja.setBackground(fondo);
        JLabel icono = new JLabel(simbolo);
        icono.setFont(Theme.mono(22));
        icono.setForeground(Theme.TINTA);
        JLabel texto = new JLabel("<html><body style='width:260px'>" + escapar(mensaje) + "</body></html>");
        texto.setFont(Theme.titulo(15));
        texto.setForeground(Theme.TINTA);
        caja.add(icono, BorderLayout.WEST);
        caja.add(texto, BorderLayout.CENTER);

        JWindow ventana = new JWindow(dueño);
        ventana.setFocusableWindowState(false);          // no le roba el foco a la app
        GraphicsConfiguration gc = (dueño != null) ? dueño.getGraphicsConfiguration()
                                                   : ventana.getGraphicsConfiguration();
        if (gc.getDevice().isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT)) {
            ventana.setBackground(new Color(0, 0, 0, 0)); // esquinas de la sombra transparentes
        }
        ventana.setContentPane(caja);
        ventana.pack();

        // Abajo a la derecha de la parte VISIBLE de la ventana (sin barra de tareas)
        Rectangle pantalla = gc.getBounds();
        Insets barra = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        Rectangle util = new Rectangle(pantalla.x + barra.left, pantalla.y + barra.top,
                pantalla.width - barra.left - barra.right, pantalla.height - barra.top - barra.bottom);
        Rectangle area = (dueño != null) ? dueño.getBounds().intersection(util) : util;
        ventana.setLocation(area.x + area.width - ventana.getWidth() - MARGEN,
                            area.y + area.height - ventana.getHeight() - MARGEN);

        caja.addMouseListener(new MouseAdapter() {       // clic = cerrar antes
            @Override public void mouseClicked(MouseEvent e) { ventana.dispose(); }
        });

        ventana.setVisible(true);
        actual = ventana;

        Timer reloj = new Timer(DURACION_MS, e -> ventana.dispose());
        reloj.setRepeats(false);
        reloj.start();
    }

    private static String escapar(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
