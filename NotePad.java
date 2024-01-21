import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.undo.UndoManager;

public class NotePad extends JFrame implements ActionListener {
    private final JTextArea textField;
    private final JMenuBar menuBar;
    private final JMenu fileMenu;
    private final JMenuItem newFileItem;
    private final JMenuItem newWindowItem;
    private final JMenuItem openItem;
    private final JMenuItem saveItem;
    private final JMenuItem exitItem;

    private final JMenu editMenu;
    private final JMenuItem undoItem;
    private final JMenuItem cutItem;
    private final JMenuItem copyItem;
    private final JMenuItem deleteItem;
    private final JMenuItem zoomInItem;
    private final JMenuItem zoomOutItem;

    private final UndoManager undoManager;
    private int fontSize = 14;

    public NotePad(String title) {
        super(title);
        setSize(800, 500);
        setLocation(400,200);

        // Initialize components
        textField = new JTextArea();
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        newFileItem = new JMenuItem("New", 'N');
        newWindowItem = new JMenuItem("New Window");
        openItem = new JMenuItem("Open", 'O');
        saveItem = new JMenuItem("Save", 'S');
        exitItem = new JMenuItem("Exit", 'E');

        // Edit menu components
        editMenu = new JMenu("Edit");
        undoItem = new JMenuItem("Undo");
        cutItem = new JMenuItem("Cut");
        copyItem = new JMenuItem("Copy");
        deleteItem = new JMenuItem("Delete");
        zoomInItem = new JMenuItem("Zoom In");
        zoomOutItem = new JMenuItem("Zoom Out");

        // Set font size and color
        Font menuFont = new Font("Arial", Font.PLAIN, 14);
        Color menuColor = Color.black;

        // Apply font and color to menu items
        setMenuItemStyle(fileMenu, menuFont, menuColor);
        setMenuItemStyle(editMenu, menuFont, menuColor);
        setMenuItemStyle(newFileItem, menuFont, menuColor);
        setMenuItemStyle(newWindowItem, menuFont, menuColor);
        setMenuItemStyle(openItem, menuFont, menuColor);
        setMenuItemStyle(saveItem, menuFont, menuColor);
        setMenuItemStyle(exitItem, menuFont, menuColor);
        setMenuItemStyle(undoItem, menuFont, menuColor);
        setMenuItemStyle(cutItem, menuFont, menuColor);
        setMenuItemStyle(copyItem, menuFont, menuColor);
        setMenuItemStyle(deleteItem, menuFont, menuColor);
        setMenuItemStyle(zoomInItem, menuFont, menuColor);
        setMenuItemStyle(zoomOutItem, menuFont, menuColor);

        // Set keyboard shortcuts
        newFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        newWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        zoomInItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        zoomOutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK));

        // Set font for the entire menu bar
        menuBar.setFont(menuFont);

        // Add action listeners
        newFileItem.addActionListener(this);
        newFileItem.setActionCommand("N");
        newWindowItem.addActionListener(this);
        newWindowItem.setActionCommand("NW");
        openItem.addActionListener(this);
        openItem.setActionCommand("O");
        saveItem.addActionListener(this);
        saveItem.setActionCommand("S");
        exitItem.addActionListener(this);
        exitItem.setActionCommand("E");
        undoItem.addActionListener(this);
        undoItem.setActionCommand("Undo");
        cutItem.addActionListener(this);
        cutItem.setActionCommand("Cut");
        copyItem.addActionListener(this);
        copyItem.setActionCommand("Copy");
        deleteItem.addActionListener(this);
        deleteItem.setActionCommand("Delete");
        zoomInItem.addActionListener(e -> zoomIn());
        zoomOutItem.addActionListener(e -> zoomOut());

        // Build menu bar
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        fileMenu.add(newFileItem);
        fileMenu.add(newWindowItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        menuBar.add(editMenu);
        editMenu.add(undoItem);
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(deleteItem);
        editMenu.addSeparator();
        editMenu.add(zoomInItem);
        editMenu.add(zoomOutItem);

        // Add text area with scrolling
        add(new JScrollPane(textField));

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize UndoManager for undo functionality
        undoManager = new UndoManager();
        textField.getDocument().addUndoableEditListener(undoManager);
    }

    // Method to set font and color for JMenuItem
    private void setMenuItemStyle(JMenuItem menuItem, Font font, Color color) {
        menuItem.setFont(font);
        menuItem.setForeground(color);
    }

    // ActionListener implementation
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "N":
                newFile();
                break;
            case "NW":
                newWindow();
                break;
            case "O":
                openFile();
                break;
            case "S":
                saveFile();
                break;
            case "E":
                System.exit(0);
                break;
            case "Undo":
                undo();
                break;
            case "Cut":
                cut();
                break;
            case "Copy":
                copy();
                break;
            case "Delete":
                delete();
                break;
        }
    }

    private void newFile() {
        textField.setText("");
    }

    private void newWindow() {
        new NotePad("NotePad").setVisible(true);
    }

    private void undo() {
        if (undoManager.canUndo()) {
            undoManager.undo();
        }
    }

    private void cut() {
        textField.cut();
    }

    private void copy() {
        textField.copy();
    }

    private void delete() {
        textField.replaceSelection("");
    }

    // Open file method
    private void openFile() {
        JFileChooser fc = new JFileChooser();
        int result = fc.showOpenDialog(this);
        FileInputStream fis = null;
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                fis = new FileInputStream(fc.getSelectedFile());
                int size = fis.available();
                byte[] b = new byte[size];
                fis.read(b);
                textField.setText(new String(b));
                JOptionPane.showMessageDialog(this, "File loaded successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error Opening File.");
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Save file method
    private void saveFile() {
        JFileChooser fc = new JFileChooser();
        int result = fc.showSaveDialog(this);
        FileOutputStream fos = null;
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                fos = new FileOutputStream(fc.getSelectedFile());
                String content = textField.getText();
                fos.write(content.getBytes());
                JOptionPane.showMessageDialog(this, "File Saved successfully!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error Saving File.");
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void zoomIn() {
        fontSize += 2; // Increase font size
        textField.setFont(new Font("Arial", Font.PLAIN, fontSize));
    }

    private void zoomOut() {
        if (fontSize > 2) {
            fontSize -= 2; // Decrease font size
            textField.setFont(new Font("Arial", Font.PLAIN, fontSize));
        }
    }

    public static void main(String[] args) {
        new NotePad("NotePad").setVisible(true);
    }
}
