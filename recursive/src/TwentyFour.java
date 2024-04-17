import java.util.ArrayList;
import java.util.Arrays;

public class TwentyFour {
	private final double TARGET = 24;
	public static String play(double a, double b, double c, double d) {
		return null;
	}
	
	public static String makeNum(double a, double b, double c, double target) {
		if (makeNum(b, c, target - a) != null) {
			return makeNum(b, c, target - a);
		}
		if (makeNum(a, c, target - b) != null) {
			return makeNum(a, c, target - b);
		}
		if (makeNum(a, b, target - c) != null) {
			return makeNum(a, b, target - c);
		}
		return null;
	}
	
	public static String makeNum(double a, double b, double target) {
		if (a < b) {
			double tmp = a;
			a = b;
			b = tmp;
		}
		if (a + b == target)
			return a + " + " + b;
		if (a * b == target)
			return  a + " * " + b;
		if (a / b == target)
			return "a / b";
		if (a - b == target)
			return "a - b";
		return null;
	}
	
	public static ArrayList<Double> createCombs(double a, double b){
		ArrayList<Double> res = new ArrayList<Double>();
 		res.addAll( Arrays.asList(a + b, a * b, a / b, b / a, a - b, b - a));
 		return res;
	}
	public static void main(String[] args) {
		String res = makeNum(2, 12, 6, 24);
		System.out.print(res);
	}
}
