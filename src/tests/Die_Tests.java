package tests;

import org.junit.jupiter.api.*;

import main.Die;

class Die_Tests {
	
	private static boolean constructorPassed;
	
	@BeforeAll
	static void beforeAll() {
		constructorPassed = true;
	}
	
	
	@Test
	@DisplayName("Test #1 - Default Constructor:")
	void test1() {
		
		Die d1 = new Die();
		
		try {
			Assertions.assertEquals(6, d1.getNumFaces());
		}
		catch (AssertionError e) {
			constructorPassed = false;
			System.out.println("Error:\n"
							 + "Default Constructor did not set the\n"
							 + "number of faces to 6.");
			Assertions.fail();
		}	
	}
	
	@Test
	@DisplayName("Test #2 - Second Constructor:")
	void test2() {
		
		Die d2 = new Die(25);
		
		try {
			Assertions.assertEquals(25, d2.getNumFaces());
		}
		catch (AssertionError e) {
			constructorPassed = false;
			System.out.println("Error:\n"
							 + "Second Constructor did not set the\n"
							 + "number of faces to the parameter.");
			Assertions.fail();
		}
	}
	
	
	@Test
	@DisplayName("Test #3 - Roll Method:")
	void test3() {
		
		//if running entire class, check that constructor methods passed
		if (!constructorPassed) {
			System.out.println("Roll test stopped.\nFix constructors first!");
			Assertions.fail();
		}
		
		
		
		//base variables
		int min = 100, max = 0, roll = 0;
		boolean rollWorks = true;  //flag to see if it fails
		
		Die d1 = new Die();  //try it with a default value
		
		//loop 100 times and get max and min values
		for (int i = 0; i < 100; i++) {  
			roll = d1.roll();
			min = Math.min(min, roll);
			max = Math.max(max, roll);
		}
		//check if roll is not from 1 to 6
		if (min != 1 || max != 6) {
			rollWorks = false;
		}
		
		//reset max and min
		min = 100;
		max = 0;
		
		d1 = new Die(18); //try it with custom numFaces
		
		// loop 100 times and get max and min values
		for (int i = 0; i < 100; i++) {
			roll = d1.roll();
			min = Math.min(min, roll);
			max = Math.max(max, roll);
		}
		// check if is not from 1 to 18
		if (min != 1 && max != 18) {
			rollWorks = false;
		}

		try {
			Assertions.assertTrue(rollWorks);
		}
		catch (AssertionError e) {
			System.out.println("Error:\n"
					 		 + "Roll method does not return a number from\n"
							 + "1 to the number of faces.\n"
							 + "For a die with 18 faces, these were your results.\n"
							 + "  lowest roll  = " + min + "\n"
							 + "  highest roll = " + max);
			Assertions.fail();
		}
	}
	

}
