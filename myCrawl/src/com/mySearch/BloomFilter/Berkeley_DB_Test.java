package com.mySearch.BloomFilter;

public class Berkeley_DB_Test {
	
	
	public static void main(String args[]){
		Berkeley_DB db = new Berkeley_DB();	
		db.openDatabase();
		db.writeToDatabase("a", "test01", false);
		db.writeToDatabase("b", "test02", false);
		db.writeToDatabase("c", "test03", false);
		db.writeToDatabase("d", "test04", false);
		db.writeToDatabase("e", "test05", false);
		db.writeToDatabase("f", "test06", false);
		
//		db.getEveryItem();
//		db.deleteFromDatabase("b");
		db.getEveryItem();
		db.closeDatabase();
	}
}
