import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.UnsupportedEncodingException;

public class JGzipDecoder {
    private JPanel mainPanel;
    private JTextArea srcTextArea;
    private JButton gzipDecodeButton;
    private JTextPane rawTextArea;
    private JTextPane jsonTextArea;
    private JComboBox charsetComboBox;

    public static void main(String[] args) {
        JFrame frame = new JFrame("JGzipDecoder");
        frame.setContentPane(new JGzipDecoder().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        /*try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(frame);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        LookAndFeel.updateComponentTreeUI(frame, LookAndFeel.SystemLookAndFeelClassName);
    }

    public JGzipDecoder() {
        charsetComboBox.addItem("UTF-8");
        charsetComboBox.addItem("GBK");
        charsetComboBox.addItem("GB2312");
        charsetComboBox.addItem("ISO-8859-1");
        charsetComboBox.addItem("US-ASCII");
        charsetComboBox.addActionListener(e -> {

        });

        gzipDecodeButton.addActionListener(e -> {
            byte[] byteArraySrc = GzipDecoderUtil.decodeHexByteArray(srcTextArea.getText());
            try {
                byteArraySrc = GzipDecoderUtil.gzipDecode(byteArraySrc);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            try {
                rawTextArea.setText(new String(byteArraySrc, (String)charsetComboBox.getSelectedItem()));
                rawTextArea.setCaretPosition(0);
                jsonTextArea.setText(GzipDecoderUtil.stringToJSON(new String(byteArraySrc, (String)charsetComboBox.getSelectedItem())));
                jsonTextArea.setCaretPosition(0);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        });

        srcTextArea.addMouseListener(newMouseAdapter());
        rawTextArea.addMouseListener(newMouseAdapter());
        jsonTextArea.addMouseListener(newMouseAdapter());
    }

    private MouseAdapter newMouseAdapter() {
        final JPopupMenu popupMenu = new JPopupMenu();  //弹出式菜单
        popupMenu.add(newJMenuItem("复制Hex编码内容", newMenuItemActionListener()));
        popupMenu.add(newJMenuItem("复制Raw内容", newMenuItemActionListener()));
        popupMenu.add(newJMenuItem("复制JSON Text内容", newMenuItemActionListener()));
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getButton() == 3) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        };
    }

    private JMenuItem newJMenuItem(String text, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(text);
        if (listener != null) {
            menuItem.setActionCommand(menuItem.getText());
            menuItem.addActionListener(listener);
        }
        return menuItem;
    }

    private ActionListener newMenuItemActionListener() {
        return e -> {
            switch (e.getActionCommand()) {
                case "复制Hex编码内容":
                    //srcTextArea.copy();
                    copy(srcTextArea.getText());

                    break;
                case "复制Raw内容":
                    //rawTextArea.copy();
                    copy(rawTextArea.getText());
                    break;
                case "复制JSON Text内容":
                    //jsonTextArea.copy();
                    copy(jsonTextArea.getText());
                    break;
            }
        };
    }

    private void copy(String data) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 封装文本内容
        Transferable trans = new StringSelection(data);
        // 把文本内容设置到系统剪贴板
        clipboard.setContents(trans, null);
    }

    /**
     * 从剪贴板中获取文本（粘贴）
     */
    public static String pasteString() {
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // 获取剪贴板中的内容
        Transferable trans = clipboard.getContents(null);

        if (trans != null) {
            // 判断剪贴板中的内容是否支持文本
            if (trans.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    // 获取剪贴板中的文本内容
                    String text = (String) trans.getTransferData(DataFlavor.stringFlavor);
                    return text;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
