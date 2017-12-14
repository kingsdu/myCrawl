package com.myCrawl.seven;

import junit.framework.TestCase;

import org.w3c.dom.Node;

public class testStyleNode extends TestCase {
        private ElementNode styleNode;
        private final static String ahrefString = "<a href=\"www.daum.net\">abc</a>";
        private final static String textString = "abc";
        private final static String t1 = "<div><a href=\"www.daum.net\">abc</a></div>";
        protected void setUp() {
        }
        
        protected void tearDown() {
        }
        
        public void testRootToString() {
                this.styleNode = ElementNode.getInstanceOf();
//                assertEquals(StyleNode.nodeToString(this.styleNode.getNode()), "root");
        }
        
        public void testAhrefToString() {
                this.styleNode = ElementNode.getInstanceOf((Node)StyleTree.parseBytes(ahrefString.getBytes()));
//                assert(ahrefString.equalsIgnoreCase(StyleNode.nodeToString(styleNode.getNode())));
        }
        
        public void testTextToString() {
                this.styleNode = ElementNode.getInstanceOf((Node)StyleTree.parseBytes(textString.getBytes()));
                //assert(textString.equalsIgnoreCase(StyleNode.nodeToString(styleNode.getNode())));
        }
        
        public void testAddSameStyleSet() {
                Node node = null;
//                this.styleNode = ElementNode.getInstanceOf();
                //先初始化一个根节点root
                this.styleNode = ElementNode.getInstanceOf((Node)StyleTree.parseBytes(t1.getBytes()));
                //在root下加上一个新节点
                node = (Node)StyleTree.parseBytes(textString.getBytes());;
                this.styleNode.trainNode(node);
                assertEquals(1, this.styleNode.getChildren().size());
                
                node = (Node)StyleTree.parseBytes(textString.getBytes());
                this.styleNode.trainNode(node);
                assertEquals(1, this.styleNode.getChildren().size());
        }
        
        public void testAddDifferentStyleSet() {
                Node node = null;
//                this.styleNode = ElementNode.getInstanceOf();
                this.styleNode = ElementNode.getInstanceOf((Node)StyleTree.parseBytes(textString.getBytes()));
                
                node = (Node)StyleTree.parseBytes(ahrefString.getBytes());;
                this.styleNode.trainNode(node);
                assertEquals(1, this.styleNode.getChildren().size());
                
                node = (Node)StyleTree.parseBytes(textString.getBytes());
                this.styleNode.trainNode(node);
                assertEquals(2, this.styleNode.getChildren().size());
                
                node = (Node)StyleTree.parseBytes(t1.getBytes());
                this.styleNode.trainNode(node);
                assertEquals(3, this.styleNode.getChildren().size());
        }
        
        
        
        /***************************************/
//        public void test_01(){
//        		DOMFragmentParser parser = new DOMFragmentParser();
//        		HTMLDocumentImpl doc = new HTMLDocumentImpl();
//        		try {
//        			parser.setFeature(
//        					"http://news.cnpc.com.cn", false);
//        			parser.setProperty(
//        					"http://news.cnpc.com.cn",
//        					"utf-8");
//        			parser
//        					.setFeature(
//        							"http://news.cnpc.com.cn/toutiao",
//        							true);
//        			parser
//        					.setFeature(
//        							"http://news.cnpc.com.cn/zgsyb",
//        							false);
//        			parser
//        					.setFeature(
//        							"http://news.cnpc.com.cn/gsld",
//        							true);
//        			parser.setFeature(
//        					"http://news.cnpc.com.cn/zhuanti", false);
//        		} catch (SAXException e) {
//        		}
//
//        		doc.setErrorChecking(false);
//        		DocumentFragment res = doc.createDocumentFragment();
//        		DocumentFragment frag = doc.createDocumentFragment();
//        		parser.parse(input, frag);
//        		res.appendChild(frag);
//
//        		try {
//        			while (true) {
//        				frag = doc.createDocumentFragment();
//        				parser.parse(input, frag);
//        				if (!frag.hasChildNodes())
//        					break;
//        				System.out.println(" - new frag, "
//        						+ frag.getChildNodes().getLength() + " nodes.");
//        				res.appendChild(frag);
//        			}
//        		} catch (Exception e) {
//        			e.printStackTrace();
//        		}
//        		return res;
//        	}
//
//        }
//        
        
}
