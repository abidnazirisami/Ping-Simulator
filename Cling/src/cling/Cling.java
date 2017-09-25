/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cling;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author kakas
 */
public class Cling {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
        try ( // TODO code application logic here
                DatagramSocket ds = new DatagramSocket()) {
            System.out.println("Enter the number of pings: ");
            Scanner sc = new Scanner(System.in);
            int n = sc.nextInt(), seqNo = 0;
            for (int i = 0; i < n; i++) {
                String send = "PING ";
                seqNo++;
                send += seqNo;
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                send += " " + dateFormat.format(date) + "\r\n";
                InetAddress ip = InetAddress.getByName("127.0.0.1");
                DatagramPacket dp = new DatagramPacket(send.getBytes(), send.length(), ip, 3402);
                ds.send(dp);

                long startTime = System.currentTimeMillis();

                

                byte[] buf = new byte[100];
                ds.setSoTimeout(1000);
                DatagramPacket response = new DatagramPacket(buf, 100);
                try {
                    ds.receive(response);
                } catch(Exception e) {
                    System.out.println("Request timed out");
                    continue;
                }
                byte[] receivedData = response.getData();
                BufferedReader b = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(receivedData)));
                String str = b.readLine();
                if (str.length()>1) {
                    long endTime = System.currentTimeMillis();
                    System.out.println("PING received from " + dp.getAddress() + ": seq#=" + i + " time="+(endTime-startTime)+" ms ");
                }
            }
        }
    }

}
