package bn.inference;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.util.Map.Entry;
import java.util.Random;
import bn.core.Assignment;
import bn.base.Distribution;
import bn.base.StringValue;
import bn.core.BayesianNetwork;
import bn.core.Domain;
import bn.core.RandomVariable;
import bn.core.Value;
import bn.parser.BIFParser;
import bn.parser.XMLBIFParser;
import bn.util.Pair;

public class LikelihoodWeighting {

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
		Distribution dis = Likelihoodweighting(X,assign,bn, N);
		printResult(dis,X);

	}

	//fixes the values for the evidence variables and samples only the nonevidence variables
	public static Distribution Likelihoodweighting(RandomVariable X, bn.core.Assignment e, BayesianNetwork bn, int N) {

		Distribution qx = new Distribution(X);

		for(Value val : X.getDomain()) {
			qx.put(val, 0.0);
		}//initally zero

		Random random = new Random();
		for(int j = 0; j < N; j++) {//sum up weight based on likelihood
			Pair xw = weighted_sample(bn, e,random);
			HashMap<RandomVariable, Object> hashMap = xw.getFirst();
			double w = xw.getSecond();
			Value val = (Value) hashMap.get(X);
			qx.put(val, qx.get(val)+w);
		}

		qx.normalize();
		return qx;
	}

	//a weight is accumulated based on the likelihood for each evidence variable
	//returns an hashmap of event and weight
	public static Pair weighted_sample(BayesianNetwork bn, Assignment e, Random random){

		double weight = 1.0;
		Assignment temp = e.copy();

		HashMap<RandomVariable, Object> x = new HashMap<RandomVariable,Object>();
		//an event with n elemetns intialized from e
		Iterator<RandomVariable> iterator = temp.variableSet().iterator();
		while(iterator.hasNext()) {
			RandomVariable rVariable = iterator.next();
			x.put(rVariable, temp.get(rVariable));
		}

		for(RandomVariable rv : (ArrayList<RandomVariable>)bn.getVariablesSortedTopologically()) {
			if(x.containsKey(rv)) {
				weight *= bn.getProbability(rv, temp);//multiply the probability of all the evidence
			}else {
				Domain domain = rv.getDomain();
				double ran = random.nextFloat();

				double p = 0;
				for (Value val : domain) {
					temp.put(rv, val);
					double pi = bn.getProbability(rv, temp);
					p += pi;
					if (ran <= p) {
						x.put(rv, val);
						break;
					}
				}
			}
		}
		return new Pair(x,weight);
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

}
