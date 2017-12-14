package com.myCrawl.seven;

import org.w3c.dom.Node;

import junit.framework.TestCase;

public class testStyleNodeSet extends TestCase {
	private final static String textString = "abc";
	private static String str1 = "<tr><a href=\"www.daum.net\">china</a></tr>";
	private static String str2 = "<tr><a href=\"www.daum.net\">china</a><p>car</p></tr>";


	public void testEquals() {
		Node node = null;
		//                ElementNode root = ElementNode.getInstanceOf();
		ElementNode root = ElementNode.getInstanceOf((Node)StyleTree.parseBytes(textString.getBytes()));

		node = (Node)StyleTree.parseBytes(str1.getBytes());
		root.trainNode(node);

		node = (Node)StyleTree.parseBytes(str2.getBytes());
		root.trainNode(node);

		root.printContentImportance();
	}


	public void testImportant() {
		String fileName1 = "E:\\test\\test1.txt";
		String fileName2 = "E:\\test\\test2.txt";
		
		Node node = null;

		ElementNode root = ElementNode.getInstanceOf((Node)StyleTree.parseBytes(textString.getBytes()));

		node = (Node)StyleTree.parseBytes(StyleTree.readBytesFromFile(fileName1));
		root.trainNode(node);

		node = (Node)StyleTree.parseBytes(StyleTree.readBytesFromFile(fileName2));
		root.trainNode(node);

		root.printContentImportance();
	}
}
