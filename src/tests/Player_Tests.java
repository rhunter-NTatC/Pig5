package tests;

import org.junit.jupiter.api.*;
import org.opentest4j.AssertionFailedError;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.io.InputStream;
import main.*;

class Player_Tests {

	
	private static final PrintStream systemOut = System.out;
	private static final InputStream systemIn = System.in;
	private static boolean constructorPassed;
	private ByteArrayInputStream testIn;
	private int count;
	private int roundScore;
	private int score;
	private boolean gotPig;
	public String outputStream;
	private boolean didDetectPig;
	private boolean didNotBankRoundOnPig;
	private boolean didBankRoundOnNoPig;
	private boolean didContinueOnYes;
	private boolean didStopOnNo;
	private boolean didStopOnPig;
	
	@BeforeAll
	static void beforeAll() {
		constructorPassed = true;
	}
	
	@Test
	@DisplayName("Test #1 - Constructor:")
	void test1() {
		
		
		Player p1 = new Player("Fred");
		
		try {
			Assertions.assertEquals("Fred", p1.getName());
		}
		catch (AssertionError e) {
			constructorPassed = false;
			System.out.println("Error:\n"
					 + "Constructor did not set the\n"
					 + "name correctly.");
			Assertions.fail();
		}
		
		
	}

	@Test
	@DisplayName("Test #2 - Constructor:")
	void test2() {
		
		
		Player p1 = new Player("Fred");
		
		try {
			Assertions.assertEquals(0, p1.getScore());
		}
		catch (AssertionError e) {
			constructorPassed = false;
			System.out.println("Error:\n"
					 + "Constructor did not set the\n"
					 + "score correctly.");
			Assertions.fail();
		}
		
		
	}
	
	
	@Test
	@DisplayName("Test #3 - takeTurn:")
	void test3() {
		
		//if running entire class, check that constructor methods passed
		if (!constructorPassed) {
			System.out.println("takeTurn() test stopped.\nFix constructor first!");
			Assertions.fail();
		}
		
		didDetectPig = true;
		didNotBankRoundOnPig = true;
		didBankRoundOnNoPig = true;
		didContinueOnYes = true;
		didStopOnNo = true;
		didStopOnPig = true;
		roundScore = 0;
		score = 0;
		outputStream = "";
		gotPig = false;
		count = 1;
		int storedScore = 0;  //holds the current Player object's score for comparison later
		
		provideInput("y");
		overridePrintStatements();

		Player p1 = new Player("Fred");
		Die d1 = new Die();

		for (int i = 0; i < 75; i++) {
			count = 0;
			roundScore = 0;
			gotPig = false;
			storedScore = p1.getScore();
			p1.takeTurn(d1);

			//check if they got a pig 
			if (gotPig) {
				//check that player's score didn't change
				if (storedScore != p1.getScore()) {
					didNotBankRoundOnPig = false;
				}
				
			}
			else {
				//check that round was added to total score
				if (storedScore + roundScore != p1.getScore()) {
					didBankRoundOnNoPig = false;
				}
				
				//check they continued on yes
				if (count < 3) {
					didContinueOnYes = false;
				}
				
			}
			
		}
		
		
		//reset System.out
		System.setOut(systemOut);
		
		//Run Assertions
		try {
			Assertions.assertTrue(didDetectPig, "1");
			Assertions.assertTrue(didNotBankRoundOnPig, "2");
			Assertions.assertTrue(didBankRoundOnNoPig, "3");
			Assertions.assertTrue(didContinueOnYes, "4");
			Assertions.assertTrue(didStopOnNo, "5");
			Assertions.assertTrue(didStopOnPig, "6");
			
		}
		catch (AssertionFailedError e) {
			
			switch (e.getLocalizedMessage().charAt(0)) {
			case '1':
				System.out.println("Error:\n"
						 + "Did not detect a pig\n");
				break;
			case '2':
				System.out.println("Error:\n"
						 + "Did not bank the turn's total\n");
				break;
			case '3':
				System.out.println("Error:\n"
						+ "Did not bank score correctly when user stopped without pig\n");
				break;
			case '4':
				System.out.println("Error:\n"
						+ "Did not continue when user chose yes\n");
				break;
			case '5':
				System.out.println("Error:\n"
						+ "Did not stop when user chose no\n");
				break;
			case '6':
				System.out.println("Error:\n"
						 + "Continued rolling afting finding pig\n");
				break;
			}
			
			System.out.println("YOUR OUTPUT:");
			System.out.println(outputStream);
			
			Assertions.fail();
			
			
		}
		
		
		
		
	}
	
	//manages the string that would have been printed to System.out
	void printCalled(String str) {
		/*
		 * concatenate str to overall output stream
		 * printed for debugging if the test fails
		 */
		outputStream += "   " + str + "\n"; 
		
		String str1 = str.toLowerCase();

		int currentRoll = 0;
		boolean hasQ = false;
		if (str.contains("?")) {
			hasQ = true;
		}

		// look for "roll"
		if (str1.contains("roll") && !hasQ) {
			if (str1.contains("pig")) {
				currentRoll = 1;
			} else {
				char[] chars = str1.toCharArray();
				for (char letter : chars) {
					// look for roll's value
					if (Character.isDigit(letter)) {
						currentRoll = Character.getNumericValue(letter);
						break;
					}
				}
			}
		}
		
		//check if they rolled again after finding a pig
		if (!(currentRoll == 0 || currentRoll == 1) && gotPig) {
			didStopOnPig = false;
		}
		
		// add roll to score (zero it if a pig)
		if (currentRoll == 1) {
			gotPig = true;
			roundScore = 0;
		} else {
			roundScore += currentRoll;
		}

		if (hasQ && count < 3) {
			provideInput("y");
			count++;
		} else if (hasQ) {
			count++;
			score += roundScore;
			provideInput("n");
			if (count > 4) {
				didStopOnNo = false;
			}
		}
	}
	
	
	/*
	 * Diverts all calls to println, print or printf to printCalled
	 */
	void overridePrintStatements() {
		System.setOut(new PrintStream(System.out) {
			public void println(String str) {
				printCalled(str);
			}

			public void printf(String str) {
				printCalled(str);
			}

			public void print(String str) {
				printCalled(str);
			}
			// override some other methods?
		});

	}

	// this simulates the data as if it came from the system.in
	private void provideInput(String data) {
		testIn = new ByteArrayInputStream(data.getBytes());
		System.setIn(testIn);
	}
	
	@AfterAll
	static void afterAll() {
		System.setOut(systemOut);
		System.setIn(systemIn);
	}
	
}
