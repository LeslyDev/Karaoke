import javax.swing.*;

public class MainWindow extends JFrame {
    private MainWindow() {
        setTitle("Караоке");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(827, 465);
        setLocation(400, 150);
        add(new Karaoke());
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow mv = new MainWindow();
    }
}


