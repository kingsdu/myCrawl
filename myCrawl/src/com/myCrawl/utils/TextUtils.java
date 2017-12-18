//package com.myCrawl.utils;
//
//import java.util.EmptyStackException;
//import java.util.Hashtable;
//import java.util.Stack;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class TextUtils {
//
//	final static Hashtable patternMatchers = new Hashtable(50); // Resuable match objects
//	
//	/**
//	 * Get a matcher object for a precompiled regex pattern.
//	 * This method tries to reuse Matcher objects for efficiency.
//	 * 
//	 * @param p the precompiled Pattern
//	 * @param input the character sequence the matcher should be using
//	 * @return a matcher object loaded with the submitted character sequence
//	 */
//	public static Matcher getMatcher(Pattern p, CharSequence input) {
//		Stack matchers;
//		if((matchers = (Stack) patternMatchers.get(p)) == null) {
//			matchers = new Stack();
//			patternMatchers.put(p, matchers);
//		}
//		Matcher matcher;
//		try {
//			matcher = (Matcher) matchers.pop();
//			matcher.reset(input);
//		} catch(EmptyStackException e) {
//			matcher = p.matcher(input);
//		}
//		return matcher;
//	}
//	
//	
//	/**
//	 * Use this method to indicate that you are finnished with a Matcher object so
//	 * that it can be recycled. It is up to the user to make sure that this object really isn't
//	 * used anymore. If used after it is marked as freed behaviour is unknown because
//	 * the Matcher object is not thread safe.
//	 * 
//	 * @param m the Matcher object that is no longer needed.
//	 */
//	public static void freeMatcher(Matcher m) {
//		Stack matchers;
//		if((matchers = (Stack) patternMatchers.get(m.pattern())) == null) {
//			// This matcher wasn't created by any pattern in the map, throw it away
//			return;
//		}
//		matchers.push(m);
//	}
//	
//	
//	/**
//	 * Utility method using a precompiled pattern instead of using the replaceAll method of
//	 * the String class. This method will also be reusing Matcher objects.
//	 * 
//	 * @see java.util.regex.Pattern
//	 * @param p precompiled Pattern to match against
//	 * @param input the character sequence to check
//	 * @param replacement the String to substitute every match with
//	 * @return the String with all the matches substituted
//	 */
//	public static String replaceAll(Pattern p, CharSequence input, String replacement) {
//		Stack matchers;
//		if((matchers = (Stack) patternMatchers.get(p)) == null) {
//			matchers = new Stack();
//			patternMatchers.put(p, matchers);
//		}
//		Matcher matcher;
//		try {
//			matcher = (Matcher) matchers.pop();
//			matcher.reset(input);
//		} catch(EmptyStackException e) {
//			matcher = p.matcher(input);
//		}
//		String res = matcher.replaceAll(replacement);
//		matchers.push(matcher);
//		return res;
//	}
//	
//	
//	
//	/**
//	 * Utility method using a precompiled pattern instead of using the split method of
//	 * the String class.
//	 * 
//	 * @see java.util.regex.Pattern
//	 * @param p precompiled Pattern to split by
//	 * @param input the character sequence to split
//	 * @return array of Strings split by pattern
//	 */
//	public static String[] split(Pattern p, CharSequence input) {
//		return p.split(input);
//	}
//	
//	
//	/**
//	 * Utility method using a precompiled pattern instead of using the matches method of
//	 * the String class. This method will also be reusing Matcher objects.
//	 * 
//	 * @see java.util.regex.Pattern
//	 * @param p precompiled Pattern to match against
//	 * @param input the character sequence to check
//	 * @return true if character sequence matches
//	 */
//	public static boolean matches(Pattern p, CharSequence input) {
//		Stack matchers;
//		if((matchers = (Stack) patternMatchers.get(p)) == null) {
//			matchers = new Stack();
//			patternMatchers.put(p, matchers);
//		}
//		Matcher matcher;
//		try {
//			matcher = (Matcher) matchers.pop();
//			matcher.reset(input);
//		} catch(EmptyStackException e) {
//			matcher = p.matcher(input);
//		}
//		boolean res = matcher.matches();
//		matchers.push(matcher);
//		return res;
//	}
//
//
//	
//}
