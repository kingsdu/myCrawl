package com.myCrawl.io;


public class CharSubSequence implements CharSequence{
	CharSequence inner;
	int start;
	int end;
	
	public CharSubSequence(CharSequence inner, int start, int end) {
		// TODO bounds check 
		super();
		this.inner = inner;
		this.start = start;
		this.end = end;
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#length()
	 */
	public int length() {
		return end-start;
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#charAt(int)
	 */
	public char charAt(int index) {
		return inner.charAt(start+index);
	}

	/* (non-Javadoc)
	 * @see java.lang.CharSequence#subSequence(int, int)
	 */
	public CharSequence subSequence(int start, int end) {
		return new CharSubSequence(this,start,end);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer((int)length());
		for(int i=0; i<length(); i++) {
			sb.append(charAt(i));
		}
		return sb.toString();
	}
}
