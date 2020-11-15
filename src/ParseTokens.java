import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ParseTokens {

	public void input() {
		String csvFile = "/temp/sirplsmytokens.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		ArrayList<Model> models = new ArrayList<Model>();
		HashMap<String, Double> map = new HashMap<String, Double>();

		// "Txhash","UnixTimestamp","DateTime","From","To","Value","ContractAddress","TokenName","TokenSymbol"
		try {

			br = new BufferedReader(new FileReader(csvFile));
			boolean timer = false;
			while ((line = br.readLine()) != null && !timer) {

				String[] info = line.split(cvsSplitBy);

				Model model = new Model();
				model.setTimestamp(info[1]);
				model.setFrom(info[3]);
				model.setTo(info[4]);
				model.setValue(Double.parseDouble(info[5]));
				model.setTokenName(info[7]);

				
				//CHANGE THIS FOR DIFFERENT BLOCK, THIS THE TIMESTAMP
				if (model.getTimestamp().equalsIgnoreCase("1603964907")) {
					timer = true;
				}

				models.add(model);

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		for (int i = 0; i < models.size(); i++) {
			Model model = models.get(i);
			if (!model.getFrom().equalsIgnoreCase("0x6621ca7b8edeb023ff32816b3996abd0c77579dc")
					&& !model.getFrom().equalsIgnoreCase("0x0000000000000000000000000000000000000000")
					&& model.getTokenName().equalsIgnoreCase("Uniswap V2")) {
				if (map.containsKey(model.getFrom())) {

					Double dub = map.get(model.getFrom());
					map.put(model.getFrom(), dub + model.getValue());

				} else {
					map.put(model.getFrom(), model.getValue());
				}
			}

			if (model.getFrom().equalsIgnoreCase("0x6621ca7b8edeb023ff32816b3996abd0c77579dc")
					&& !model.getTo().equalsIgnoreCase("0x6621ca7b8edeb023ff32816b3996abd0c77579dc")
					&& !model.getFrom().equalsIgnoreCase("0x0000000000000000000000000000000000000000")
					&& model.getTokenName().equalsIgnoreCase("Uniswap V2") && map.containsKey(model.getTo())) {

				Double dub = map.get(model.getTo());
				map.put(model.getTo(), dub - model.getValue());

			}

		}
		 map.entrySet().forEach(entry->{
			 	if(entry.getValue() > 0.1) {
				    System.out.println(entry.getKey() + " " + entry.getValue());  
			 		
			 	}
			 });
	}

}
