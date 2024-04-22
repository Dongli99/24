package test.recursive;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import javax.script.ScriptException;

import org.junit.jupiter.api.Test;

import main.recursive.Recursive24;

public class Recursive24Test {
    @Test
    public void exhaustTest() {
        Random rand = new Random();
        int i = 1;
        while (i < 10) {
            int a = rand.nextInt(13) + 1; // Generate random numbers between 1 and 13
            int b = rand.nextInt(13) + 1;
            int c = rand.nextInt(13) + 1;
            int d = rand.nextInt(13) + 1;
            String solution = Recursive24.play(a, b, c, d); // Play the game with the random numbers
            try {
                System.out.println(solution);
                int result = (int) ExpressionEvaluator.evaluate(solution);
                assertEquals(Recursive24.getTarget(), result);
            } catch (Exception e) {
                assertEquals(ScriptException.class, e.getClass());
            }
            i++;
        }
    }
}
