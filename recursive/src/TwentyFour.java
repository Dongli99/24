import java.util.ArrayList;
import java.util.List;
import java.util.function.BinaryOperator;

public class TwentyFour {

	private static class Operation {
		private BinaryOperator<Double> o;
		private char s;

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

		public Double calc(Double a, Double b) {
			return this.o.apply(a, b);
		}

		@Override
		public String toString() {
			return String.valueOf(this.s);
		}
	}

	private static class OpUnit {
		private Double a = null;
		private Double b = null;
		private Operation o = null;
		private Double r;
		private String s = null;

		public OpUnit(Double a) {
			this.a = a;
			r = a;
			s = Integer.toString(a.intValue());
		}

		public OpUnit(OpUnit au, OpUnit bu, Operation o) {
			this.a = au.r;
			this.b = bu.r;
			this.o = o;
			r = o.calc(this.a, this.b);
			s = "(" + au.s + o.toString() + bu.s + ")";
		}

		public Double r() {
			return r;
		}

		@Override
		public String toString() {
			return s;
		}
	}

	private static final Integer TARGET = 24;
	private static final Operation p = new Operation('+');
	private static final Operation m = new Operation('-');
	private static final Operation t = new Operation('*');
	private static final Operation d = new Operation('/');
	private static final Operation[] os = { p, m, t, d };

	public static void play(int a, int b, int c, int d) {
		String solution = makeNum(
				Double.valueOf(TARGET),
				new OpUnit(Double.valueOf(a)),
				new OpUnit(Double.valueOf(b)),
				new OpUnit(Double.valueOf(c)),
				new OpUnit(Double.valueOf(d)));
		solution = solution != null ? solution : "No answer.";
		System.out.println(solution);
	}

	private static String makeNum(Double target, OpUnit... us) {
		if (us.length == 1)
			return null;
		for (Operation o : os) {
			if (us.length == 2) {
				OpUnit u = new OpUnit(us[0], us[1], o);
				if (u.r().equals(target))
					return u.toString();
			} else {
				for (int i = 0; i < us.length; i++) {
					for (int j = 0; j < us.length; j++) {
						if (i == j)
							continue;
						OpUnit[] remain = combineUnits(us, i, j, o);
						String result = makeNum(target, remain);
						if (result != null) {
							return result;
						}
					}
				}
			}
		}
		return null;
	}

	private static OpUnit[] combineUnits(OpUnit[] us, int i, int j, Operation o) {
		OpUnit[] remain = new OpUnit[us.length - 1];
		int index = 0;
		remain[index] = new OpUnit(us[i], us[j], o);
		for (int k = 0; k < us.length; k++) {
			if (k == i || k == j)
				continue;
			remain[++index] = us[k];
		}
		return remain;
	}

	public static void main(String[] args) {
		play(5, 8, 7, 3);
	}
}
