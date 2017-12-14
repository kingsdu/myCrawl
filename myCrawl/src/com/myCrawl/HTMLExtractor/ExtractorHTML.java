package com.myCrawl.HTMLExtractor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;


public class ExtractorHTML {

	static List<String> linkList = new ArrayList<>();
	static List<String> linkListEmbed = new ArrayList<>();
	
	
	
	static final String LINK = "link";
	
	
	private static final int MAX_ATTR_NAME_LENGTH =
			Integer.parseInt(System.getProperty(ExtractorHTML.class.getName() +
					".maxAttributeNameLength", "1024")); // 1K; 

	static final int MAX_ATTR_VAL_LENGTH = 
			Integer.parseInt(System.getProperty(ExtractorHTML.class.getName() +
					".maxAttributeValueLength", "16384")); // 16K; 

	// TODO: perhaps cut to near MAX_URI_LENGTH

	// this pattern extracts attributes from any open-tag innards
	// matched by the above. attributes known to be URIs of various
	// sorts are matched specially
	static final String EACH_ATTRIBUTE_EXTRACTOR =
			"(?is)\\b((href)|(action)|(on\\w*)" // 1, 2, 3, 4 
			+"|((?:src)|(?:lowsrc)|(?:background)|(?:cite)|(?:longdesc)" // ...
			+"|(?:usemap)|(?:profile)|(?:datasrc))" // 5
			+"|(codebase)|((?:classid)|(?:data))|(archive)|(code)" // 6, 7, 8, 9
			+"|(value)|(style)|(method)" // 10, 11, 12
			+"|([-\\w]{1,"+MAX_ATTR_NAME_LENGTH+"}))" // 13
			+"\\s*=\\s*"
			+"(?:(?:\"(.{0,"+MAX_ATTR_VAL_LENGTH+"}?)(?:\"|$))" // 14
			+"|(?:'(.{0,"+MAX_ATTR_VAL_LENGTH+"}?)(?:'|$))" // 15
			+"|(\\S{1,"+MAX_ATTR_VAL_LENGTH+"}))"; // 16
	
	private static final int MAX_ELEMENT_LENGTH =
			Integer.parseInt(System.getProperty(ExtractorHTML.class.getName() +
					".maxElementNameLength", "1024"));


	static final String RELEVANT_TAG_EXTRACTOR =
			"(?is)<(?:((script[^>]*+)>.*?</script)" + // 1, 2
					"|((style[^>]*+)>.*?</style)" + // 3, 4
					"|(((meta)|(?:\\w{1,"+MAX_ELEMENT_LENGTH+"}))\\s+[^>]*+)" + // 5, 6, 7
					"|(!--.*?--))>"; // 8 
	
	
	static final String RELEVANT_TAG_EXTRACTOR1 =
			"(((meta)|(?:\\w{1,"+MAX_ELEMENT_LENGTH+"}))\\s+[^>]*+)" +
					"|(!--.*?--))>"; // 8 

	
	public static void main(String[] args) {
		String path = "F:\\data\\test\\zhy_list.html";
		String curi = "http://www.sinopecnews.com/news/content/2017-11/10/content_1693067.htm";
		readHtml(path,curi);
		String path1 = "F:\\data\\test\\text.txt";
//		writeList(linkListEmbed,path1);
		writeList(linkList,path1);
	}


	public static void readHtml(String path,String curi){
		List<String> Link1 = new ArrayList<>();
		List<String> Link3 = new ArrayList<>();
		List<String> Link5 = new ArrayList<>();
		List<String> Link6 = new ArrayList<>();
		List<String> Link7 = new ArrayList<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
			String line = "";
			String result = "";
			while((line = br.readLine())!=null){
				result+=line;
			}
			Matcher tags = Pattern.compile(RELEVANT_TAG_EXTRACTOR).matcher((CharSequence) result);
			char[] charArray = result.toCharArray();
			String str = "";
			while(tags.find()) {
				if (tags.start(8) > 0) {
					// comment match
					// for now do nothing
				} else if (tags.start(7) > 0) {
					// <meta> match
					int start = tags.start(5);
					int end = tags.end(5);
					str = "";
					for(int i=start;i<end;i++){				
						str+=charArray[i];				
					}
					Link7.add(str);
//					System.out.println();
//					System.out.println("start    "+start+"end    "+end);
					//					if (processMeta(curi,
					//							cs.subSequence(start, end))) {
					//						// meta tag included NOFOLLOW; abort processing
					//						break;
					//					}
				} else if (tags.start(5) > 0) {
					// generic <whatever> match
					int start5 = tags.start(5);
					int end5 = tags.end(5);
					int start6 = tags.start(6);
					int end6 = tags.end(6);
					str = "";
					for(int i = start5;i<end5;i++){					
						str+=charArray[i];				
					}
					Link5.add(str);
//					System.out.println();
//					System.out.println("start5    "+start5+"end5    "+end5);
					str = "";
					for(int i = start6;i<end6;i++){			
						str+=charArray[i];				
					}
					Link6.add(str);
//					System.out.println();
//					System.out.println("start6    "+start6+"end6    "+end6);
					//					processGeneralTag(curi,
					//							cs.subSequence(start6, end6),
					//							cs.subSequence(start5, end5));
					processGeneralTag(curi,
							result.subSequence(start6, end6),
							result.subSequence(start5, end5));
				} else if (tags.start(1) > 0) {
					// <script> match
					int start = tags.start(1);
					int end = tags.end(1);
					str = "";
					for(int i = start;i<end;i++){				
						str+=charArray[i];	
					}
					Link1.add(str);
					System.out.println();
					System.out.println("start    "+start+"end    "+end);
					//					assert start >= 0: "Start is: " + start + ", " + curi;
					//					assert end >= 0: "End is :" + end + ", " + curi;
					//					assert tags.end(2) >= 0: "Tags.end(2) illegal " + tags.end(2) +
					//							", " + curi;
					//					processScript(curi, cs.subSequence(start, end),
					//							tags.end(2) - start);

				} else if (tags.start(3) > 0){
					// <style... match
					int start = tags.start(3);
					int end = tags.end(3);
					str = "";
					for(int i = start;i<end;i++){				
						str+=charArray[i];
					}
					Link3.add(str);
					System.out.println();
					System.out.println("start    "+start+"end    "+end);
					//					assert start >= 0: "Start is: " + start + ", " + curi;
					//					assert end >= 0: "End is :" + end + ", " + curi;
					//					assert tags.end(4) >= 0: "Tags.end(4) illegal " + tags.end(4) +
					//							", " + curi;
					//					processStyle(curi, cs.subSequence(start, end),
					//							tags.end(4) - start);
				}
			}
//			String path1 = "F:\\data\\test\\listOut1.txt";
//			writeList(Link1,path1);
//			writeList(Link3,path1);
//			writeList(Link5,path1);
//			writeList(Link6,path1);
//			writeList(Link7,path1);
		} catch (Exception e) {
		}
	}
	
	/**
	 * @Description: TODO
	 * @param:element link6(链接)；cs 5(标签)
	 * @return:
	 * @date: 2017-11-10  
	 */
	protected static void processGeneralTag(String curi, CharSequence element,
			CharSequence cs) {
		Matcher attr = Pattern.compile(EACH_ATTRIBUTE_EXTRACTOR).matcher((CharSequence) cs);
		
		final String elementStr = element.toString();
		while (attr.find()) {
			int valueGroup =
					(attr.start(14) > -1) ? 14 : (attr.start(15) > -1) ? 15 : 16;
			int start = attr.start(valueGroup);
			int end = attr.end(valueGroup);
			CharSequence value = cs.subSequence(start, end);
			CharSequence attrName = cs.subSequence(attr.start(1),attr.end(1));
			value = unescapeHtml(value);
			if (attr.start(2) > -1) {
				// HREF
				CharSequence context =
						elementContext(element, attr.group(2));
				if(elementStr.equalsIgnoreCase(LINK)) {
					//属于link
					linkListEmbed.add(value.toString());
				} else {
					linkList.add(value.toString());
				}	
			}
		}
	}
	
	
	
	/**
     * Create a suitable XPath-like context from an element name and optional
     * attribute name. 
     * 
     * @param element
     * @param attribute
     * @return CharSequence context
     */
    public static CharSequence elementContext(CharSequence element, CharSequence attribute) {
        return attribute == null? "": element + "/@" + attribute;
    }
	
	/**
     * Replaces HTML Entity Encodings.
     * @param cs The CharSequence to remove html codes from
     * @return the same CharSequence or an escaped String.
     */
    public static CharSequence unescapeHtml(final CharSequence cs) {
        if (cs == null) {
            return cs;
        }
        
        return StringEscapeUtils.unescapeHtml(cs.toString());
    }
	
	public static void writeList(List<String> aList,String path){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path),true)));
			bw.newLine();
			bw.write("+++++++++++++++++++++++++++++++++++++++++++");
			bw.newLine();
			for(int i=0;i<aList.size();i++){				
				bw.write(aList.get(i));
				bw.newLine();
				bw.flush();
			}
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
}
