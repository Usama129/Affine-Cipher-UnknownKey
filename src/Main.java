import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		FileProcessing files = new FileProcessing();

		files.printOccurences();
		files.findCipherKeys();
		System.out.println("\nThe correct key values are A: " + files.getKeyA() + " B: " + files.getKeyB());
	
		System.out.println("\nTo view the decrypted text, type V and press enter");
		if ((new Scanner(System.in)).next().equalsIgnoreCase("V"))
			System.out.println(files.getDecryptedText());
	}

}
