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
import bn.core.Domain;
import bn.base.StringValue;
import bn.core.BayesianNetwork;
import bn.core.RandomVariable;
import bn.core.Value;
import bn.parser.BIFParser;
import bn.parser.XMLBIFParser;

public class RejectionSampling {

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
        Distribution dis = rejection_sampling(X,assign,bn, N);
        printResult(dis,X);

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
	public static Distribution rejection_sampling(RandomVariable X, bn.core.Assignment e, BayesianNetwork bn, int N) {
		
		Distribution qx = new Distribution(X);
		
		for (Value obj : X.getDomain()) {
			qx.put(obj, 0.0);
		}//a vector of counts for each value X, initially zero
		

		int sample = 0;
		int reject=0;
		String rej= "-";
		for (int j = 1; j <= N; j++) {
			HashMap<RandomVariable, Object> x = prior_sample(bn);
			
			if (consistent(x, e)) {//if consistent, put value in distribution
				sample++;
				Value obj = (Value) x.get(X);
				qx.put(obj, qx.get(obj) + 1);
				rej+=x;
			}
			if(!consistent(x,e)) {//if not consistent, output "---"
				rej+="---\n";
				reject++;
			}
		}
		System.out.println(rej);
		System.out.println("Number of consistent samples: " + sample);	
		System.out.println("Number of samples being rejected: "+ reject);
		qx.normalize();
		return qx;
	}
	
	//check if random variable generated is consistent with evidence
	private static boolean consistent(HashMap<RandomVariable, Object> x, Assignment e) {
		Iterator<Entry<RandomVariable, Value>> iterator = e.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Entry<RandomVariable, Value> temp_map = iterator.next();
			if (!x.get(temp_map.getKey()).equals(temp_map.getValue())) {
				return false;
			}
		}
		
		return true;
	}
	
	//returns an event sampled from the prior specified bn
	public static HashMap<RandomVariable, Object> prior_sample(BayesianNetwork bn) {
		
		Random random = new Random();
		
		HashMap<RandomVariable, Object> hashMap = new HashMap<RandomVariable, Object>();//an event with n elements
		
		Assignment assignment = new bn.base.Assignment();
		
		for (RandomVariable rv : (ArrayList<RandomVariable>) bn.getVariablesSortedTopologically()) {
			Domain domain = rv.getDomain();
			double random_float = random.nextFloat();
			
			double p = 0;
			for (Value val : domain) {
				Assignment temp_ass = assignment.copy();
				temp_ass.put(rv, val);
				double pi = bn.getProbability(rv, temp_ass);
				p += pi;
				if (random_float <= p) {
					hashMap.put(rv, val);
					assignment.put(rv, val);
					break;
				}
			}
		}
		return hashMap;
	}
}
