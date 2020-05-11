import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
		FileProcessing files = new FileProcessing();

		files.printOccurences();
		int opt = -1;
		do {
			
			System.out.println("Would you like to crack this file with the Brute Force method, "
					+ "or with letter frequency analysis?\n"
					+ "\nPress 1 for Brute Force"
					+ "\nPress 2 for Frequency Analysis");
			opt = sc.nextInt();
		} while (opt < 1 && opt > 2);
		
		String decrypted;
		
		if (opt == 1)
			 decrypted = files.affineDecrypt(true);
		else
			 decrypted = files.affineDecrypt(false);
		
		System.out.println("\nThe correct key values are A: " + files.getKeyA() + " B: " + files.getKeyB());
	
		System.out.println("\nTo view the decrypted text, type V and press enter");
		if ((new Scanner(System.in)).next().equalsIgnoreCase("V"))
			System.out.println(decrypted);
	}

}
