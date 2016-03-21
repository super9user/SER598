package edu.asupoly.ser422;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Utility {
	
	private static Utility instance = null;
	private Utility() {
	}
	
	public static Utility getInstance() {
		if(instance == null){
			instance = new Utility();
		}
		return instance;
	}
	
	public List<String> removeFromList(List<String> list, String element){
		for (Iterator<String> iter = list.listIterator(); iter.hasNext(); ) {
		    String a = iter.next();
		    if (a.equalsIgnoreCase(element)) {
		        iter.remove();
		    }
		}
		return list;
	}
	
	// will return (a - b)
	public List<String> difference(List<String> a, List<String> b){
		List<String> difference = new ArrayList<String>();
		for (String val : a) {
			if(!b.contains(val)){
				difference.add(val);
			}
		}
		return difference;
	}
	
	public List<String> split(String delimiter, String str){
		if(str==null){
			return new LinkedList<String>();
		}
			
		List<String> list = Arrays.asList(str.split(delimiter));
		if(list.size()==1 && list.get(0).equals("")){
			return new LinkedList<String>();
		}
		return new LinkedList<String>(list);
	}
	
	public List<String> split(String delimiter, String[] str){
		if(str==null){
			return new LinkedList<String>();
		}
		List<String> list = new LinkedList<String>();
		for (String string : str) {
			if(string!=null && !string.equals("")){
				list.add(string);
			}
		}
		return list;
	}

	public String join(String delimiter, List<String> list){
		String joined = "";
		for(int i=0; i<list.size(); i++){
			String str = list.get(i);
			joined += str;
			if(i < (list.size() - 1)){
				joined += delimiter;
			}
		}
		return joined;
	}
	
}
