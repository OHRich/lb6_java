import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

    private static final int PORT = 8888;
    private List<InetSocketAddress> clients = new ArrayList<>();
    private double integralResult = 0.0;
    private int counterClients = 0;

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("Server is running...");

            byte[] receiveBuffer = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                processReceivedData(receivePacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processReceivedData(DatagramPacket receivePacket) {
        InetAddress clientAddress = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();
        InetSocketAddress clientSocketAddress = new InetSocketAddress(clientAddress, clientPort);
        if (!clients.contains(clientSocketAddress)) clients.add(clientSocketAddress);
        String receivedData = new String(receivePacket.getData(), 0, receivePacket.getLength());
        if (receivedData.equals("connect")){
            System.out.println("New client connected: " + clientAddress);
        } else if (receivedData.equals("disconnect")) {
            clients.remove(new InetSocketAddress(clientAddress, clientPort));
            System.out.println("Client disconnected: " + clientAddress + " " + clientPort);
        } else if (receivedData.startsWith("integralResult")){
            String[] parts = receivedData.split(",");
            double partOfResult = Double.parseDouble(parts[1]);
            integralResult += partOfResult;
            System.out.println("Received result from client: " + partOfResult);
            counterClients--;
            if (counterClients == 0){
                System.out.println("Received integralResult from client: " + integralResult);
            }
        }
    }

    public double sendCalculationDataToAll(double lowerLimit, double upperLimit, double step) {
        try (DatagramSocket socket = new DatagramSocket()) {
            if (clients.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No connected clients", "Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                integralResult = 0.0;
                int numClients = clients.size();
                double stepClient = (upperLimit - lowerLimit) / numClients;

                for (InetSocketAddress clientAddress : clients) {
                    counterClients++;
                    double upperLimitTemp = lowerLimit + stepClient;
                    String message = "integral," + lowerLimit + "," + upperLimitTemp + "," + step;
                    byte[] sendData = message.getBytes();

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress.getAddress(), clientAddress.getPort());
                    socket.send(sendPacket);

                    lowerLimit = upperLimitTemp;
                }

                while (counterClients != 0){
                    sleep(1000);
                }

                return integralResult;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }
}
