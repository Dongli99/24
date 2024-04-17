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
			s = a.toString();
		}

		public OpUnit(Double a, Double b, Operation o) {
			this.a = a;
			this.b = b;
			this.o = o;
			r = o.calc(this.a, this.b);
			if (b != null)
				s = a + o.toString() + b;
			else
				s = a.toString();
		}

		public OpUnit(OpUnit au, OpUnit bu, Operation o) {
			this(au.r, bu.r, o);
			s = au.s + o.toString() + bu.s;
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
		if (us.length == 0)
			return null;
		List<List<OpUnit>> orders = new ArrayList<>();
		List<OpUnit> origin = new ArrayList<>();
		for (OpUnit u : us) {
			origin.add(u);
		}
		genOrders(origin, 0, orders);
		for (Operation o : os) {
			for (List<OpUnit> od : orders) {
				if (us.length == 2) {
					OpUnit u = new OpUnit(od.get(0), od.get(1), o);
					if (u.r().equals(target))
						return u.toString();
				} else {
					OpUnit u = new OpUnit(od.get(0), od.get(1), o);
					OpUnit[] remain = new OpUnit[us.length - 1];
					remain[0] = u;
					System.arraycopy(us, 2, remain, 1, us.length - 2);
					String result = makeNum(target, remain);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}

	private static void genOrders(List<OpUnit> origin, int index, List<List<OpUnit>> orders) {
		if (index == origin.size()) {
			orders.add(new ArrayList<>(origin));
		} else {
			for (int i = index; i < origin.size(); i++) {
				swap(origin, index, i);
				genOrders(origin, index + 1, orders);
				swap(origin, index, i); // Backtrack
			}
		}
	}

	private static void swap(List<OpUnit> origin, int i, int j) {
		OpUnit temp = origin.get(i);
		origin.set(i, origin.get(j));
		origin.set(j, temp);
	}

	public static void main(String[] args) {
		play(1, 2, 3, 4);
	}
}
