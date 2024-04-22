package main.recursive;

/**
 * 
 * A class for solving the 24 game with recursive function.
 * 
 * The 24 Game is a mathematical card game where the objective is to use 
 * four given numbers and the basic arithmetic operations (+, -, *, /) to 
 * arrive at the number 24. Players must use all four numbers exactly once 
 * and can only use each number once. The game can be played competitively 
 * or as a solo puzzle. 
 * Example: given numbers 5, 12, 2, 10, the solution can be: (5+12)*2-10=24
 * 
 * @author Dongli Liu
 */

import java.util.Random;
import java.util.Scanner;
import java.util.function.BinaryOperator;

import main.utils.ExpressionEvaluator;
import main.utils.ExpressionNormalizer;

public class Recursive24 {
	/**
	 * A class representing mathematical operations.
	 */
	private static class Operation {
		private BinaryOperator<Double> o;// BinaryOperator for the operation
		private char s;// Symbol of the operation

		/**
		 * Constructs an Operation object.
		 * 
		 * @param operator The character representing the operation.
		 */
		public Operation(char operator) {
			switch (operator) {
				case '+':
					this.o = (a, b) -> a + b;
					break;
				case '-':
					this.o = (a, b) -> a - b;
					break;
				case '*':
					this.o = (a, b) -> a * b;
					break;
				case '/':
					this.o = (a, b) -> a / b;
					break;
				default:
					throw new IllegalArgumentException("Invalid operator: " + operator);
			}
			this.s = operator;
		}

		/**
		 * Calculates the result of the operation.
		 * 
		 * @param a The first operand.
		 * @param b The second operand.
		 * @return The result of the operation.
		 */
		public Double calc(Double a, Double b) {
			return this.o.apply(a, b);
		}

		/**
		 * Returns the symbol of the operation as a string.
		 * 
		 * @return The symbol of the operation.
		 */
		@Override
		public String toString() {
			return String.valueOf(this.s);
		}
	}

	/**
	 * A class representing an operand or an operation unit.
	 */
	private static class OpUnit {
		private Double a = null; // First operand
		private Double b = null; // Second operand
		private Operation o = null; // Operation
		private Double r; // Result of the operation
		private String s = null; // String representation of the unit

		/**
		 * Constructs an OpUnit object with a single operand.
		 * 
		 * @param a The operand value.
		 */
		public OpUnit(Double a) {
			this.a = a;
			r = a;
			s = Integer.toString(a.intValue());// String representation is the integer value
		}

		/**
		 * Constructs an OpUnit object with two operand units and an operation.
		 * 
		 * @param au The first operand unit.
		 * @param bu The second operand unit.
		 * @param o  The operation.
		 */
		public OpUnit(OpUnit au, OpUnit bu, Operation o) {
			this.a = au.r; // First operand is the result of the first operand unit
			this.b = bu.r; // Second operand is the result of the second operand unit
			this.o = o;
			r = o.calc(this.a, this.b); // Calculate the result of the operation
			s = "(" + au.s + o.toString() + bu.s + ")"; // String representation keeps s of previous units
		}

		/**
		 * Returns the result of the operation unit.
		 * 
		 * @return The result of the operation unit.
		 */
		public Double r() {
			return r;
		}

		/**
		 * Overrides the toString method to return the string representation of the
		 * unit.
		 * 
		 * @return The string representation of the unit.
		 */
		@Override
		public String toString() {
			return s;
		}

		/**
		 * Performs the specified operation between two OpUnits and combines the result
		 * with the remaining units in the array.
		 * 
		 * @param us The array of OpUnits.
		 * @param i  The index of the first unit.
		 * @param j  The index of the second unit.
		 * @param o  The operation to be performed.
		 * @return The new array of OpUnits after performing the operation.
		 */
		private static OpUnit[] performOperationAndCombine(OpUnit[] us, int i, int j, Operation o) {
			OpUnit[] remain = new OpUnit[us.length - 1]; // New array with one less element
			int index = 0;
			remain[index] = new OpUnit(us[i], us[j], o); // Perform operation between the units and add the result
			for (int k = 0; k < us.length; k++) {
				if (k == i || k == j)
					continue;
				remain[++index] = us[k]; // Copy the remaining units
			}
			return remain;
		}

	}

	// Constants for the operations
	private static int target = 24;
	private static final Operation P = new Operation('+');
	private static final Operation M = new Operation('-');
	private static final Operation T = new Operation('*');
	private static final Operation D = new Operation('/');
	private static final Operation[] OS = { P, M, T, D };

	/**
	 * Plays the 24 game with the given numbers.
	 * 
	 * @param a The first number.
	 * @param b The second number.
	 * @param c The third number.
	 * @param d The fourth number.
	 */
	public static String play(int a, int b, int c, int d) {
		String solution = makeNum(
				Double.valueOf(target),
				new OpUnit(Double.valueOf(a)),
				new OpUnit(Double.valueOf(b)),
				new OpUnit(Double.valueOf(c)),
				new OpUnit(Double.valueOf(d)));
		// build the solution expression if found, otherwise build no answer message
		solution = solution != null
				? ExpressionNormalizer.normalizeExpression(solution, solution.length() - 1)
				: String.format("No answer for (%d, %d, %d, %d)", a, b, c, d);
		return solution;
	}

	/**
	 * Recursively generates combinations of operations and numbers to reach the
	 * target.
	 * 
	 * @param target The target number.
	 * @param us     The array of OpUnits.
	 * @return The solution as a string, or null if no solution is found.
	 */
	private static String makeNum(Double target, OpUnit... us) {
		if (us.length == 1) // If only one OpUnit, return null
			return null;
		for (Operation o : OS) { // Iterate over operations
			if (us.length == 2) { // If only two OpUnits, check if they form the target
				OpUnit u = new OpUnit(us[0], us[1], o);
				if (u.r().equals(target))
					return u.toString();
			} else { // If more than two OpUnits, combine 2 of them, recursively call makeNum
				for (int i = 0; i < us.length; i++) {
					for (int j = 0; j < us.length; j++) {
						if (i == j) // if they are the same OpUnits, do nothing
							continue;
						OpUnit[] remain = OpUnit.performOperationAndCombine(us, i, j, o);
						String result = makeNum(target, remain); // Call recursively
						if (result != null) // Return a string if got a meaningful answer
							return result;
					}
				}
			}
		}
		return null;
	}

	/**
	 * The main method for running the program.
	 * 
	 * @param newTarget The new target integer to replace original 24.
	 */
	public static void setTarget(int newTarget) {
		target = newTarget;
	}

	public static int getTarget() {
		return target;
	}

	public static void main(String[] args) throws Exception {
		int[] numbers = new int[4];
		Scanner sc = new Scanner(System.in);
		for (int i = 0; i < 4; i++) {
			System.out.print("Enter an integer between 1 and 13: ");
			numbers[i] = sc.nextInt();
		}
		sc.close();
		System.out.println(play(numbers[0], numbers[1], numbers[2], numbers[3]));
	}
}
