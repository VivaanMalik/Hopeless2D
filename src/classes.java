import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.text.NumberFormatter;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class classes 
{
    static class OPButton extends JButton 
    {

        private Color hoverBackgroundColor;
        private Color pressedBackgroundColor;
        private int arcsize;

        public OPButton() {
            this(null);
        }

        public OPButton(String text) {
            super(text);
            super.setContentAreaFilled(false);
        }

        @Override
        public void setContentAreaFilled(boolean b) {
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (getModel().isPressed()) {
                g.setColor(pressedBackgroundColor);
            } else if (getModel().isRollover()) {
                g.setColor(hoverBackgroundColor);
            } else {
                g.setColor(getBackground());
            }
            g.fillRoundRect(0, 0, getWidth(), getHeight(), arcsize, arcsize);
            super.paintComponent(g);
        }

        public Color getHoverBackgroundColor() {
            return hoverBackgroundColor;
        }

        public void setHoverBackgroundColor(Color hoverBackgroundColor) {
            this.hoverBackgroundColor = hoverBackgroundColor;
        }

        public Color getPressedBackgroundColor() {
            return pressedBackgroundColor;
        }

        public void setPressedBackgroundColor(Color pressedBackgroundColor) {
            this.pressedBackgroundColor = pressedBackgroundColor;
        }

        public int getArcSize() {
            return arcsize;
        }

        public void setArcSize(int arcsize) {
            this.arcsize = arcsize;
        }
    }

    static class OPTextField extends JTextField
    {
        private int arcsize;

        public OPTextField() {
            this(null);
        }

        public OPTextField(String text) {
            super(text);
            setOpaque(false);
        }
        

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth(), getHeight(), arcsize, arcsize);
            super.paintComponent(g);
        }

        public int getArcSize() {
            return arcsize;
        }

        public void setArcSize(int arcsize) {
            this.arcsize = arcsize;
        }
    }

    static class OPFormattedTextFieldForNumbers extends JFormattedTextField
    {
        private int arcsize;

        public OPFormattedTextFieldForNumbers() {
            this(null);
        }

        public OPFormattedTextFieldForNumbers(NumberFormatter mask) {
            super(mask);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth(), getHeight(), arcsize, arcsize);
            super.paintComponent(g);
        }

        public int getArcSize() {
            return arcsize;
        }

        public void setArcSize(int arcsize) {
            this.arcsize = arcsize;
        }
    }

    static class OPSplitPaneDivider extends BasicSplitPaneDivider
    {
        public OPSplitPaneDivider(BasicSplitPaneUI ui) 
        {
            super(ui);
        }

        @Override
        public void paint(Graphics g)
        {
            Rectangle clip = g.getClipBounds();
            g.setColor(getBackground());
            g.fillRect(clip.x, clip.y, clip.width, clip.height);
            super.paint(g);
        }
    }

    static class OPLabel extends JLabel
    {
        public OPLabel() 
        {
            this(null);
        }

        public OPLabel(String text) 
        {
            super(text);
        }

        public void paint(Graphics g) 
        {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g.setFont(getFont());
            g.setColor(getForeground());
            // g.drawString(getText(), 0, 0);
            super.paint(g);
        }
    }

    static class RoundRectangle extends JComponent
    {
        @Override
        public void paint(Graphics g) 
        {
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paint(g);
        }
    }

    static class MyScrollBarUI extends BasicScrollBarUI 
    {
        private final Dimension d = new Dimension();
      
        @Override
        protected JButton createDecreaseButton(int orientation) 
        {
            return new JButton() 
            {
                @Override
                public Dimension getPreferredSize() 
                {
                    return d;
                }
            };
        }
      
        @Override
        protected JButton createIncreaseButton(int orientation) 
        {
            return new JButton() 
            {
                @Override
                public Dimension getPreferredSize() 
                {
                    return d;
                }
            };
        }
      
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) 
        {
        }
      
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) 
        {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color color = utils.highlight_color;
            JScrollBar sb = (JScrollBar) c;
            if (!sb.isEnabled() || r.width > r.height) {
                return;
            } else if (isDragging) {
                color = utils.DarkColor(0.15f);
            } else if (isThumbRollover()) {
                color = utils.DarkColor(0.1f);
            } else {
                color = utils.DarkColor(0.2f);
            }
            g2.setPaint(color);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 20, 20);
            // g2.setPaint(utils.DarkColor(0.05f));
            // g2.drawRoundRect(r.x, r.y, r.width-1, r.height-1, 20, 20);
            g2.dispose();
        }
      
        @Override
        protected void setThumbBounds(int x, int y, int width, int height) {
          super.setThumbBounds(x, y, width, height);
          scrollbar.repaint();
        }
    }
}
