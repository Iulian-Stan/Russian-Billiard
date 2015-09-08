package Retea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import Billiard.Window;

public class SimpleClient extends Thread {

	private static Window w;
	private Socket echoSocket = null;
	public PrintWriter out = null;
	private BufferedReader in = null;
	public String mes;
	
	public SimpleClient(int port, String adress, Window w) {
		try {
			echoSocket = new Socket(adress, port);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket
					.getInputStream()));
		} catch (UnknownHostException e) {
			System.err.println("Could not connect to server");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for server conection.");
			System.exit(1);
		}
		SimpleClient.w = w;
	}

	public void run() {
		try {
			while ((mes = in.readLine()) != null) {
				w.panel.readInfo();
			}
		} catch (IOException e) {
			System.out.println("connection lost: " + e.getMessage());
		}
		System.out.println("connection closed");
		out.close();
		try {
			in.close();
			echoSocket.close();
		} catch (IOException e) {
			System.out.println("error at close: " + e.getMessage());
		}
	}
}