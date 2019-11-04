package csp.aus;

import java.util.Date;

import csp.base.CSP;
import csp.base.NamedVariable;
import csp.base.NotEqualConstraint;
import csp.core.Assignment;
import csp.core.Solver;
import csp.core.Variable;
import csp.solver.BacktrackingSearchSolver;
import csp.util.ArrayListSet;

/**
 * Constraint satisfaction problem for coloring the map of Australia,
 * from AIMA Section 6.1 and Fig 6.1.
 */
public class AustraliaMapCSP extends CSP {
	
	/**
	 * Construct and return a new instance of the CSP for coloring the map of
	 * Australia.
	 */
	public AustraliaMapCSP() {
		// Don't share domains if doing constraint propagation
		Variable WA = new NamedVariable("WA", ColorDomain.rgbDomain());
		Variable NT = new NamedVariable("NT", ColorDomain.rgbDomain());
		Variable Q = new NamedVariable("Q", ColorDomain.rgbDomain());
		Variable NSW = new NamedVariable("NSW", ColorDomain.rgbDomain());
		Variable V = new NamedVariable("V", ColorDomain.rgbDomain());
		Variable SA = new NamedVariable("SA", ColorDomain.rgbDomain());
		Variable T = new NamedVariable("T", ColorDomain.rgbDomain());
		this.variables = new ArrayListSet<Variable>();
		this.variables.add(WA);
		this.variables.add(NT);
		this.variables.add(Q);
		this.variables.add(NSW);
		this.variables.add(V);
		this.variables.add(SA);
		this.variables.add(T);
		this.constraints = new ArrayListSet<csp.core.Constraint>();
		this.constraints.add(new NotEqualConstraint(SA, WA));
		this.constraints.add(new NotEqualConstraint(SA, NT));
		this.constraints.add(new NotEqualConstraint(SA, Q));
		this.constraints.add(new NotEqualConstraint(SA, NSW));
		this.constraints.add(new NotEqualConstraint(SA, V));
		this.constraints.add(new NotEqualConstraint(WA, NT));
		this.constraints.add(new NotEqualConstraint(NT, Q));
		this.constraints.add(new NotEqualConstraint(Q, NSW));
		this.constraints.add(new NotEqualConstraint(NSW, V));
	}

	public static void main(String[] args) {
		System.out.println("Australia Map Coloring Problem (AIMA 6.1.1)");
		CSP csp = new AustraliaMapCSP();
		System.out.println(csp);
		System.out.println("Backtracking search solver");
		Solver solver = new BacktrackingSearchSolver();
		long start = new Date().getTime();
		Assignment result = solver.solve(csp);
		long end = new Date().getTime();
		System.out.format("time: %.3f secs\n", (end-start)/1000.0);
		System.out.println("result=" + result);
	}
}
