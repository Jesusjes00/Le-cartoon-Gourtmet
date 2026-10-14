package Restaurante_Completo.LeCartoon_Original;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Buttons extends JButton {
    private int arcWidth = 20;
    private int arcHeight = 20;
    private Color colorFondo;
    private Color colorHover;
    private Color colorBordeCustom = null;
    private boolean mouseOver = false;

    public Buttons(String texto, int arcWidth, int arcHeight, Color background, Color Hover) {
        super(texto);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(true);
        colorFondo = background;
        colorHover = Hover;

        // Detectar cuando el ratón entra y sale
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mouseOver = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseOver = false;
                repaint();
            }
        });
    }

    public void setColorFondo(Color color) {
        this.colorFondo = color;
    }

    // Para definir un color de contorno específico 
    public void setColorBordeCustom(Color colorBorde) {
        this.colorBordeCustom = colorBorde;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Cambiar color si el ratón está encima
        Color colorActual = mouseOver ? colorHover : colorFondo;
        g2d.setColor(colorActual);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

        // Dibujar texto
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        

        Color colorBordeActual;
        if (colorBordeCustom != null) {

            colorBordeActual = mouseOver ? colorHover : colorBordeCustom; 
        } else {
            colorBordeActual = mouseOver ? colorHover : colorFondo;
        }
        
        g2d.setColor(colorBordeActual);
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
    }

	public int getArcWidth() {
		return arcWidth;
	}

	public void setArcWidth(int arcWidth) {
		this.arcWidth = arcWidth;
	}

	public int getArcHeight() {
		return arcHeight;
	}

	public void setArcHeight(int arcHeight) {
		this.arcHeight = arcHeight;
	}

	public Color getColorHover() {
		return colorHover;
	}

	public void setColorHover(Color colorHover) {
		this.colorHover = colorHover;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public Color getColorFondo() {
		return colorFondo;
	}

	public Color getColorBordeCustom() {
		return colorBordeCustom;
	}
    
    
}