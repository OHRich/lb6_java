import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class ClientGUI extends JFrame {

    private static final int PORT = 8888;
    private JTextField integralResultField;
    private DatagramSocket socket;
    private InetAddress serverAddress;

    public ClientGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);

        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName("127.0.0.1");
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to server.");
            return;
        }

        sendMessage("connect");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                sendMessage("disconnect");
            }
        });

        integralResultField = new JTextField("0.0", 10);
        integralResultField.setEditable(false);
        add(integralResultField);

        new Thread(this::receiveMessages).start();
    }

    private void sendMessage(String message) {
        try {
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, PORT);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessages() {
        try {
            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());

                if (received.startsWith("integral")) {
                    String[] parts = received.split(",");
                    double lowerLimit = Double.parseDouble(parts[1]);
                    double upperLimit = Double.parseDouble(parts[2]);
                    double step = Double.parseDouble(parts[3]);

                    int numThread = 9;
                    double stepRun = (upperLimit - lowerLimit) / numThread;
                    ArrayList<Thread> threadArrayList = new ArrayList<>();

                    for (int i = 0; i < numThread; i++){
                        double upperLimitTemp = lowerLimit + stepRun;
                        Thread thread = new Thread(new CalculateIntegral(lowerLimit, upperLimitTemp, step));
                        threadArrayList.add(thread);
                        thread.start();
                        lowerLimit = upperLimitTemp;
                    }
                    for (Thread thread : threadArrayList){
                        thread.join();
                    }

                    double integralResult = CalculateIntegral.getIntegralResult();
                    CalculateIntegral.setIntegralResultNull();
                    integralResultField.setText(String.valueOf(integralResult));

                    sendMessage("integralResult," + integralResult);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientGUI client = new ClientGUI();
            client.setVisible(true);
        });
    }
}
