/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pinger;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kakas
 */
public class Pinger {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, IOException, InterruptedException {
        try ( // TODO code application logic here
                DatagramSocket ds = new DatagramSocket(3402)) {
            Random random = new Random();
            while (true) {
                byte[] buf = new byte[100];
                DatagramPacket dp = new DatagramPacket(buf, 100);
                try {
                    ds.receive(dp);
                } catch (IOException ex) {
                    Logger.getLogger(Pinger.class.getName()).log(Level.SEVERE, null, ex);
                }
                byte[] receivedData = dp.getData();
                BufferedReader b = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(receivedData)));
                System.out.println("Received from " + dp.getAddress() + ": " + b.readLine());
                
                int r = random.nextInt(100);
                if (r % 5 == 0) {
                    System.out.println("Reply not sent");
                    continue;
                }
                Thread.sleep(random.nextInt(100) * 10);
                InetAddress client = dp.getAddress();
                int port = dp.getPort();
                DatagramPacket response = new DatagramPacket(receivedData, receivedData.length, client, port);
                ds.send(response);
                System.out.println("Reply Sent");

            }
        }
    }

}
