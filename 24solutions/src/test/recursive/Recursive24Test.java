package test.recursive;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

import main.recursive.Recursive24;
import main.utils.ExpressionEvaluator;

public class Recursive24Test {
    @Test
    public void recursiveExhaustTest() {
        Random rand = new Random();
        int i = 1;
        while (i < 10000) { // Conduct 10000 tests.
            int a = rand.nextInt(13) + 1; // Generate random numbers between 1 and 13
            int b = rand.nextInt(13) + 1;
            int c = rand.nextInt(13) + 1;
            int d = rand.nextInt(13) + 1;
            String solution = Recursive24.play(a, b, c, d); // Play the game with the random numbers
            int result = (int) ExpressionEvaluator.evaluate(solution);
            assertTrue(Recursive24.getTarget() == result || solution.charAt(0) == 'N');
            i++;
        }
    }
}
