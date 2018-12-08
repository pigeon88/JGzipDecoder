import javax.swing.*;
import java.awt.*;

public enum LookAndFeel {

    //1、Metal风格(默认)
    MetalLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"),

    //2、Windows风格
    WindowsLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"),

    //3、Windows Classic风格
    WindowsClassicLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"),

    //4、Motif风格
    MotifLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel"),

    //5、Mac风格 (需要在相关的操作系统上方可实现)
    MacLookAndFeel("com.sun.java.swing.plaf.mac.MacLookAndFeel"),

    //6、GTK风格 (需要在相关的操作系统上方可实现)
    GTKLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"),
    //7、可跨平台的默认风格
    CrossPlatformLookAndFeelClassName(UIManager.getCrossPlatformLookAndFeelClassName()),

    //8、当前系统的风格
    SystemLookAndFeelClassName(UIManager.getSystemLookAndFeelClassName());

    LookAndFeel(String className) {
        this.className = className;
    }

    private String className;


    public static void updateComponentTreeUI(Component c, LookAndFeel lookAndFeel) {
        try {
            UIManager.setLookAndFeel(lookAndFeel.className);
            SwingUtilities.updateComponentTreeUI(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
