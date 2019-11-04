
AustraliaCSP.java: Main class for the Australia Map Coloring CSP.

This code illustrates how to create an instance of a CSP, using Variables, Domains, and Constraints. It also provides an example of a main() method that creates an instance, creates a solver, and calls the solver to (try to) solve the instance. You don't have to do things exactly this way, but you need to do something similar, per the requirements.

This is only one part of my solution to the project, from the package "aus".

I also have a package "core", which contains the abstract representation of CSPs (interfaces and abstract classes).

The problem-specific package "aus" contains classes that implement or extend the abstract specifications, for this specific problem domain. Thus AustraliaCSP extends core.CSP, as you can see in the code.

The implementations of the CSP algorithms are in my package "solver", such as the BacktrackingSearchSolver used in this example.

Again, you don't have to do things exactly this way, but you need to do something similar.
