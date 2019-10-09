package me.trow.tgclan.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TopKDR implements Comparator<String>{
	
	public static HashMap<String, Double> a = new HashMap<>();
	public static ArrayList<String> ab = new ArrayList<>();
	public static TopKDR m = new TopKDR(a);
	public static Map<String, Double> base;
	public static TreeMap<String, Double> b = new TreeMap<String,Double>(m);
	
	public TopKDR(Map<String, Double> base){
		this.base = base;
	}
	
	public int compare(String a, String b){
		if(base.get(a) >= base.get(b)){
			return -1;
		}else{
			return 1;
		}
	}
	
	// Cabra
	
	public static List<String> getTops(int totalTop){
		List<String> l = new ArrayList<>();
		b.clear();
		a.clear();
		for(String t:base.keySet()){
			a.put(t, base.get(t));
		}
		b.putAll(a);
		ab.clear();
		for(String c : b.keySet()){
			ab.add(c);
		}
		if(totalTop>a.size()){
			totalTop=a.size();
		}
		for(int i=0; i < totalTop;i++){
			//print(ab+" - "+i);
			if(ab.get(i)!=null){
				l.add(ab.get(i));
			}
		}
		return l;
	}
	
	public static String getTopGlad(int max,int pos){
		if(max>3){
			max=3;
		}
		String t1="";
		String t2="";
		String t3="";
		String s="";
		b.clear();
		a.clear();
		for(String t:base.keySet()){
			a.put(t, base.get(t));
		}
		b.putAll(a);
		ab.clear();
		for(String c:b.keySet()){
			ab.add(c);
		}
		if(max>a.size()){
			max=a.size();
		}
		/*a.put("Trow_Espadas", 11);
		a.put("Trow_Pesca", 9);
		a.put("Tato_Herbalismo", 7);
		b.putAll(a);
		for(String c : b.keySet()){
			ab.add(c);
		}*/
		List<String> abc=new ArrayList<>();
		for(int i = 0; i < max;i++){
			abc.add(ab.get(i));
		}
		for(String a:abc){
			if(t1.equalsIgnoreCase("")){
				t1=a;continue;
			}
			if(t2.equalsIgnoreCase("")){
				t2=a;continue;
			}
			if(t3.equalsIgnoreCase("")){
				t3=a;
			}
		}
		if(pos==1){
			s=t1;
		}else if(pos==2){
			s=t2;
		}else if(pos==3){
			s=t3;
		}
		return s;
	}

	public static void main(String[] args) {
		a.put("Trow_Espadas", 11.0);
		a.put("Trow_Pesca", 9.0);
		a.put("Tato_Herbalismo", 7.0);
		b.putAll(a);
		for(String c : b.keySet()){
			ab.add(c);
		}
		for(int i = 0; i < 2;i++){
			print("AB: "+ab.get(i));
		}
		print("OBS: "+getTopGlad(2, 1));
	}
	
	public static void print(String a){
		System.out.println(a);
	}

}
