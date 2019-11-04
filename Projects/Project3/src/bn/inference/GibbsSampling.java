package bn.inference;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import bn.core.*;
import org.xml.sax.SAXException;

import java.util.Map.Entry;
import java.util.Random;

import bn.base.Distribution;
import bn.base.StringValue;
import bn.parser.BIFParser;
import bn.parser.XMLBIFParser;
import bn.util.ArraySet;

public class GibbsSampling implements Inferencer {

	public bn.core.Distribution query(RandomVariable X, bn.core.Assignment e, BayesianNetwork network) {

		return null;

	};
	
	
	public static Distribution GibbsAsk(RandomVariable X, bn.core.Assignment e, BayesianNetwork bn, int N) {

		Random random = new Random();
		Distribution dis = new Distribution(X);
		for(Value val : X.getDomain()) {
			dis.put(val, 0.0);
		}

		ArrayList<RandomVariable> variables = (ArrayList<RandomVariable>) bn.getVariablesSortedTopologically();
		ArrayList<RandomVariable> Z = new ArrayList<RandomVariable>(); //nonevidence varaibles in bn
		for(RandomVariable rVariable : variables) {
			if(!e.variableSet().contains(rVariable)) {
				Z.add(rVariable);
			}
		}

		Assignment x = e.copy();//current state

		for(RandomVariable rv : Z) {//set x randomly 
			Domain domain = rv.getDomain();
			for (Value val : domain) {
				x.put(rv, val);

			}
		}
		for(int i = 0; i < N; i++) {
			for(RandomVariable Zi : Z) {
				//get P(Zi|mb(Zi))
				Distribution tempd = new Distribution(X);
				for(Value val : Zi.getDomain()) {
					x.put(Zi, val);
					Set<RandomVariable> parents = getParents(Zi, bn);
					Assignment tempa = getAssignment(parents, x);
					double pi = bn.getProbability(Zi, tempa);
					for(RandomVariable children : bn.getChildren(Zi)) {
						Set<RandomVariable> parents1 = getParents(children, bn);
						Assignment tempaa = getAssignment(parents1, x);
						pi *= bn.getProbability(children, tempaa);
					}
					tempd.put(val, pi);
				}
				tempd.normalize();

				// set the value of Zi in x by sampling from P(Zi|mb(Zi))
				double random_double = random.nextDouble();
				double p = 0;
				for(Value val : Zi.getDomain()) {
					double temp_p = tempd.get(val);
					p += temp_p;
					if(random_double <= p) {
						x.put(Zi, val);
						break;
					}
				}
				// N(x) = N(x) + 1 where x is the value of X in x
				Value val = x.get(X);
				dis.put(val, dis.get(val) + 1);
			}
		}

		dis.normalize();
		return dis;
	}

	public static Set<RandomVariable> getParents(RandomVariable X,BayesianNetwork bn){
		Set<RandomVariable> parents = new ArraySet<RandomVariable>();
		for(RandomVariable rv : bn.getVariables()) {
			if(bn.getChildren(rv).contains(X)) {
				parents.add(rv);
			}
		}
		parents.add(X);
		return parents;
	}

	public static Assignment getAssignment(Set<RandomVariable> parents, Assignment e) {
		Assignment assign = new bn.base.Assignment();
		for(Entry<RandomVariable, Value> entry : e.entrySet()) {
			for(RandomVariable rv : parents) {
				if(entry.getKey().equals(rv)) {
					assign.put(rv, entry.getValue());
				}
			}
		}
		return assign;
	}


	public static void printResult(Distribution d, RandomVariable ran) {
		System.out.println("Query Variable: " + ran.toString());

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
		//args[0] sample size
		int N = Integer.parseInt(args[0]);

		//arg[1] ==> filename
		String filename = args[1];
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

		//arg[2] ==> query variable
		RandomVariable X = bn.getVariableByName(args[2]);
		System.out.println("--------------------------");
		String condition = "\nCondition: ";
		bn.core.Assignment assign = new bn.base.Assignment();


		//args[3+} ==> evidence key value pairs [J true]
		for(int i = 3; i < args.length; i = i+2){
			String evidenceVariable = args[i];
			RandomVariable ran = bn.getVariableByName(evidenceVariable);
			String evidenceVariableValue = args[i+1];
			Value val = new StringValue(evidenceVariableValue);
			assign.put(ran, val);
			condition += evidenceVariable + "=" + val + " ";
		}


		System.out.println(condition);
		Distribution dis = GibbsAsk(X,assign,bn, N);
		printResult(dis,X);

	}

}
