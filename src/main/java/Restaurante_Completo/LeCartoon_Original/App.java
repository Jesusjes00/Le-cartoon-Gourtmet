package Restaurante_Completo.LeCartoon_Original;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

public class App {
	public static void main(String[] args) {
	    FlatLightLaf.setup();                       //  FlatLaf
	    UIManager.put("Button.arc", 0);             // esquinas rectas (neo-brutalista)
	    UIManager.put("Component.arc", 0);          // combos, spinners
	    UIManager.put("TextComponent.arc", 0);      // campos de texto
	    UIManager.put("ScrollBar.width", 10);
	    Login_Window.main(args);
	}
}
