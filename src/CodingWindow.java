import java.awt.Dimension;
import javax.swing.text.AttributeSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import java.awt.Component;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;


public class CodingWindow extends classes
{
    static class CustomUndoPlainDocument extends PlainDocument 
    {
        private CompoundEdit compoundEdit;
        @Override 
        protected void fireUndoableEditUpdate(UndoableEditEvent e) 
        {
            if (compoundEdit == null) 
            {
                super.fireUndoableEditUpdate(e);
            } 
            else 
            {
                compoundEdit.addEdit(e.getEdit());
            }
        }
        @Override 
        public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException 
        {
            if (length == 0) 
            {
                super.replace(offset, length, text, attrs);
            } 
            else
            {
                compoundEdit = new CompoundEdit();
                super.fireUndoableEditUpdate(new UndoableEditEvent(this, compoundEdit));
                super.replace(offset, length, text, attrs);
                compoundEdit.end();
                compoundEdit = null;
            }
        }
    }
    public static class SaveButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            windows.saveCodeedFileAuto(txtarea.getText());
        }
    }
    static JTextArea txtarea;
    static String txt;
    static String text = null;
    static String bigtext = "LMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO\nLMAO";
    static int sizeofline = 25;
    static KeyStroke undoKeyStroke = KeyStroke.getKeyStroke("control Z");
    static KeyStroke redoKeyStroke = KeyStroke.getKeyStroke("control Y");
    public static Component SetupWindow(int WIDTH, int Height, String CodeText)
    {   
        text = CodeText;
        // int lines = text.split("\n").length;
        String[] lines = text.split("\n");
        JPanel mainp = new JPanel();
        mainp.setBackground(utils.DarkColor(0.1f));
        mainp.setLayout(new BoxLayout(mainp, BoxLayout.Y_AXIS));
        JPanel p = new JPanel();
        p.setMinimumSize(new Dimension(WIDTH, sizeofline*lines.length));
        p.setMaximumSize(new Dimension(WIDTH*69, sizeofline*lines.length));
        // p.setPreferredSize(new Dimension(WIDTH, sizeofline*lines.length));
        p.setBackground(utils.DarkColor(0.1f));
        p.setLayout(new GridLayout(lines.length, 1));
        JPanel remainingp = new JPanel();
        remainingp.setMinimumSize(new Dimension(WIDTH, Height-(sizeofline*lines.length)));
        remainingp.setBackground(utils.DarkColor(0.1f));
        String txt = "";
        for (String i : lines)
        {
            txt+=i+"\n";
        }
        p.removeAll();
        txtarea = new JTextArea(txt);
        txtarea.setDocument(new CustomUndoPlainDocument());
        txtarea.setText(txt);
        txtarea.setForeground(utils.highlight_color);
        txtarea.setBackground(utils.DarkColor(0.115f));
        txtarea.setFont(utils.Consolas(14));
        txtarea.setBounds(0, 0, WIDTH, Height);
        UndoManager undomanager = new UndoManager();
        txtarea.getDocument().addUndoableEditListener(undomanager);
        // UNDOOOOOOOO
        txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(undoKeyStroke, "undoKeyStroke");
        txtarea.getActionMap().put("undoKeyStroke", (Action) new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undomanager.undo();
                } catch (CannotUndoException cue) {}
            }
        });
        // RIDOOOOOOOOOOOO
        txtarea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(redoKeyStroke, "redoKeyStroke");
        txtarea.getActionMap().put("redoKeyStroke", (Action) new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    undomanager.redo();
                } catch (CannotRedoException cre) {}
            }
        });
        txtarea.getDocument().addUndoableEditListener(new UndoableEditListener() 
        {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) 
            {
                undomanager.addEdit(e.getEdit());
            }
        });
        txtarea.getDocument().addDocumentListener(new DocumentListener() 
        {

            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) 
            {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) 
            {
                // windows.saveCodeedFileAuto(txtarea.getText());
            }
            
        });
        p.setLayout(null);
        p.add(txtarea);
        
        
        mainp.add(p);
        if (Height-(lines.length*sizeofline)>0)
        {
            mainp.add(remainingp);
        }
        JScrollPane jsp = new JScrollPane(txtarea);
        jsp.setBackground(null);
        jsp.getVerticalScrollBar().setUnitIncrement(Math.round(sizeofline/1.5f));
        return (Component) jsp;
    }

    public static String openfile(String path, String name)
    {
        try
        {
            text = "";
            if (name!=null)
            {
                List<String> lines=new ArrayList<String>();
                lines=Files.readAllLines(Paths.get(path+"\\"+name));
                String[] updatedLines=lines.toArray(new String[0]);
                for (int i = 0; i < updatedLines.length; i ++)
                {
                    text+=updatedLines[i]+"\n";
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return text;
    }
}
