package bn.inference;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import javax.xml.parsers.ParserConfigurationException;

import bn.core.Inferencer;
import bn.core.Assignment;
import org.xml.sax.SAXException;
import bn.core.BayesianNetwork;
import bn.core.RandomVariable;
import bn.core.Value;
import bn.base.Distribution;
import bn.base.StringValue;
import bn.parser.BIFParser;
import bn.parser.XMLBIFParser;

public class ExactInference implements Inferencer{

	public bn.core.Distribution query(RandomVariable X, bn.core.Assignment e, BayesianNetwork network) {

		return enumerationAsk(X, e, network);

	};


	//THE ALGORITHM
	//evaluates trees using depth-first recursion
	public static Distribution enumerationAsk(RandomVariable X, bn.core.Assignment e, BayesianNetwork bn) {
		Distribution qx = new Distribution(X); //initially empty

		if (e.variableSet().contains(X)) {
			for (Value xi : X.getDomain()) {
				if (xi.equals(e.get(X))) {
					qx.put(xi, 1.0);//if empty
				} else {
					qx.put(xi, 0.0);
				}
			}
			return qx;
		}
		for (Value xi : X.getDomain()) {	//put random variable and value into assignment
			bn.core.Assignment temp = e.copy();
			temp.put(X, xi);
			qx.put(xi, enumerateAll((ArrayList<RandomVariable>)bn.getVariablesSortedTopologically(),temp, bn));
		}		
		qx.normalize();
		return qx;
	}

	//variable enumeration algorithm
	private static double enumerateAll(ArrayList<RandomVariable> vars, bn.core.Assignment e, BayesianNetwork bn) {
		if (vars.isEmpty()) {
			return 1.0;
		}
		RandomVariable Y = vars.get(0);

		ArrayList<RandomVariable> rest = (ArrayList<RandomVariable>) vars.clone();
		rest.remove(0);

		if (e.variableSet().contains(Y)) {//if Y has value y in e
			return bn.getProbability(Y, e.copy()) * enumerateAll(rest, e.copy(), bn);
		} else {
			double p = 0;
			for (Value yi : Y.getDomain()) {
				bn.core.Assignment temp_e = e.copy();
				temp_e.put(Y, yi);

				double pi = bn.getProbability(Y, temp_e);
				p += pi * enumerateAll(rest, temp_e, bn);

			}
			return p;
		}
	}

	public static void printResult(Distribution d, RandomVariable rv) {//print the result output
		System.out.println("Result Output: ");
		System.out.println("Query Variable: " + rv.toString());

		Set<Entry<Value, Double>> set = d.entrySet();
		Iterator<Entry<Value, Double>> iterator = set.iterator();

		String s = "{ ";

		while (iterator.hasNext()) {
			Entry<Value, Double> temp = iterator.next();
			Value key = temp.getKey();
			String val = String.format("%.3f", temp.getValue());
			s += key + ":" + val + " ";
		}

		s += "}";

		System.out.println(s);
	}

	//MAIN
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		//arg[0] ==> filename
		String filename = args[0];
		String [] temp = filename.split("\\.");
		BayesianNetwork bn;

		if(temp[temp.length-1].endsWith(".bif")) {
			FileInputStream p = new FileInputStream("src/bn/examples/"+filename);
			BIFParser parse = new BIFParser(p); //parse the bif file
			bn = parse.parseNetwork();
		}else {
			XMLBIFParser xml = new XMLBIFParser();
			bn = xml.readNetworkFromFile("src/bn/examples/" + filename);//parse the xml file
		}
		System.out.println(bn.toString());

		//arg[1] ==> query variable
		RandomVariable X = bn.getVariableByName(args[1]);
		System.out.println("--------------------------");
		String condition = "\nCondition: ";
		Assignment assign = new bn.base.Assignment();


		//args[2+} ==> evidence key value pairs [J true]
		for(int i = 2; i < args.length; i = i+2){
			String evidenceVariable = args[i];
			RandomVariable ran = bn.getVariableByName(evidenceVariable);
			String evidenceVariableValue = args[i+1];
			Value val = new StringValue(evidenceVariableValue);
			assign.put(ran, val);
			condition += evidenceVariable + "=" + val + " ";
		}


		System.out.println(condition);
		Distribution dis = enumerationAsk(X,assign,bn);
		printResult(dis,X);

	}

}