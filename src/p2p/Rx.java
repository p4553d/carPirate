package p2p;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Rx extends Thread {

	private final Socket connection;
	private final BufferedReader bInput;
	private final BufferedWriter bOutput;

	public Rx(Socket connection) throws IOException {
		this.connection = connection;
		this.bInput = new BufferedReader(new InputStreamReader(connection
				.getInputStream()));
		this.bOutput = new BufferedWriter(new OutputStreamWriter(connection
				.getOutputStream()));

		System.out.println("Rx.Rx() connection established with " + connection);

	}

	public void run() {
		String line;
		try {
			while (true) {

				line = bInput.readLine();

				System.out.println("Rx.run() recived: " + line);

				// TODO: XML-Parser
				if (line.equals(".")) {
					break;
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			connection.close();
			System.out.println("Rx.run() connection closed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
