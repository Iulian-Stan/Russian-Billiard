package Retea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import Billiard.Window;

public class SimpleServer extends Thread {

	private static Window w;
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	public PrintWriter out = null;
	private BufferedReader in = null;
	public String mes;

	public SimpleServer(int port, Window w) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port + ".");
			System.exit(1);
		}
		System.out.println("server wait for 1 client\n");
		try {
			clientSocket = serverSocket.accept();
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket
					.getInputStream()));
			System.out.println("Accept succes.");
		} catch (IOException e) {
			System.err.println("Accept failed.");
			System.exit(1);
		}
		SimpleServer.w = w;
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
			clientSocket.close();
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("error at close: " + e.getMessage());
		}
	}
}