import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Karaoke extends JPanel implements ActionListener{
    private Sound currentSound;
    private String currentSong;
    private ArrayList<String> i = new ArrayList<>();
    private JList list;
    private JLabel firstTextString = new JLabel();
    private JLabel secondTextString = new JLabel();
    private Timer timer;
    private Logic logic;
    private boolean songIsStart = false;
    private Image backgroundImage;

    Karaoke() {
        setLayout(null);
        try {
            backgroundImage = ImageIO.read(new File("./picture.jpg"));
        } catch (IOException exc) {
            exc.printStackTrace();
        }
        i.add("DistrictsQuarters");
        i.add("OurSummer");
        list = new JList<>(i.toArray());
        Font stringFont = new Font("Verdana", Font.PLAIN, 15);
        Font labelFont = new Font("Verdana", Font.PLAIN, 24);
        JButton addSong = new JButton("Добавить новую песню");
        addSong.setBounds(620, 267, 190, 30);
        addSong.addActionListener(e -> {
            JFileChooser fileOpen = new JFileChooser(new File("./songs/"));
            int ret = fileOpen.showDialog(null, "Выбрать файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileOpen.getSelectedFile();
                String songName = file.getName().split("\\.")[0];
                if (!i.contains(songName)) {
                    i.add(songName);
                    remove(list);
                    list = new JList<>(i.toArray());
                    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    list.setBounds(630, 100, 170, 100);
                    add(list);
                    list.addListSelectionListener(new listSelectionListener());
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Эта песня уже есть в списке",
                            "Ошибка", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        add(addSong);
        firstTextString.setBounds(70, 150, 400, 50);
        firstTextString.setFont(stringFont);
        add(firstTextString);
        secondTextString.setBounds(70, 192, 400, 50);
        secondTextString.setFont(stringFont);
        add(secondTextString);
        JLabel applicationName = new JLabel();
        applicationName.setBounds(300, 30, 200, 30);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setBounds(630, 100, 170, 100);
        add(list);
        JButton startButton = new JButton("Включить песню");
        startButton.setBounds(620, 207, 190, 30);
        add(startButton);
        JButton stopButton = new JButton("Завершить");
        stopButton.setBounds(620, 237, 190, 30);
        add(stopButton);
        applicationName.setForeground( Color.CYAN );
        applicationName.setText("Караоке");
        applicationName.setFont(labelFont);
        add(applicationName);
        list.addListSelectionListener(new listSelectionListener());
        startButton.addActionListener(e -> {
            try {
                if (!songIsStart) {
                    currentSound.play();
                    timer = new Timer(1, Karaoke.this);
                    logic = new Logic(currentSong);
                    timer.start();
                    songIsStart = true;
                } else {
                    JOptionPane.showMessageDialog(null, "В данный момент песня уже играет",
                            "Ошибка", JOptionPane.PLAIN_MESSAGE);
                }
            } catch (NullPointerException exc) {
                JOptionPane.showMessageDialog(null, "Вы не выбрали песню",
                        "Ошибка", JOptionPane.PLAIN_MESSAGE);
            }
        });
        stopButton.addActionListener(e -> {
            try {
                currentSound.stop();
                timer.stop();
                songIsStart = false;
                firstTextString.setText("");
                secondTextString.setText("");
                timer = new Timer(1, Karaoke.this);
                logic = new Logic(currentSong);
            } catch (NullPointerException exc) {
                JOptionPane.showMessageDialog(null, "Ничего не играет",
                        "Ошибка", JOptionPane.PLAIN_MESSAGE);
            }
        });
        setFocusable(true);
    }

    class listSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            int selected = ((JList<?>)e.getSource()).
                    getSelectedIndex();
            currentSound = new Sound(i.get(selected));
            currentSong = i.get(selected);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] data = logic.getString();
        try {
            firstTextString.setText(data[0]);
            secondTextString.setText(data[1]);
        } catch (ArrayIndexOutOfBoundsException ignored) {}
        timer.setDelay(logic.getDelay());
    }
}
