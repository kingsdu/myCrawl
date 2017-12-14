package com.mySearch.BloomFilter;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DatabaseNotFoundException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.OperationStatus;

public class BdbUriUniqFilter {

	protected boolean createdEnvironment = false;
	protected transient Database alreadySeen = null;
	static protected DatabaseEntry ZERO_LENGTH_ENTRY = 
			new DatabaseEntry(new byte[0]);
	private static final String DB_NAME = "alreadySeenUrl";
	protected long count = 0;

	private static final String COLON_SLASH_SLASH = "://";


	/**
	 * Constructor.
	 * @param environment A bdb environment ready-configured.
	 * @throws IOException
	 */
	public BdbUriUniqFilter(Environment environment)
			throws IOException {
		try {
			initialize(environment);
		} catch (DatabaseException e) {
			throw new IOException(e.getMessage());
		}
	}



	/**
	 * Constructor.
	 * @param bdbEnv The directory that holds the bdb environment. Will
	 * make a database under here if doesn't already exit.  Otherwise
	 * reopens any existing dbs.
	 * @param cacheSizePercentage Percentage of JVM bdb allocates as
	 * its cache.  Pass -1 to get default cache size.
	 * @throws IOException
	 */
	public BdbUriUniqFilter(File bdbEnv, final int cacheSizePercentage)
			throws IOException {
		if (!bdbEnv.exists()) {
			bdbEnv.mkdirs();
		}
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		if (cacheSizePercentage > 0 && cacheSizePercentage < 100) {
			envConfig.setCachePercent(cacheSizePercentage);
		}
		try {
			createdEnvironment = true;
			initialize(new Environment(bdbEnv, envConfig));
		} catch (DatabaseException e) {
			throw new IOException(e.getMessage());
		}
	}




	/**
	 * Method shared by constructors.
	 * @param env Environment to use.
	 * @throws DatabaseException
	 */
	protected void initialize(Environment env) throws DatabaseException {
		DatabaseConfig dbConfig = getDatabaseConfig();
		dbConfig.setAllowCreate(true);
		try {
			//先清空已有数据库
			env.truncateDatabase(null, DB_NAME, false);
		} catch (DatabaseNotFoundException e) {
			// Ignored
		}
		//打开新数据库
		open(env, dbConfig);
	}




	/**
	 * @return DatabaseConfig to use
	 */
	protected DatabaseConfig getDatabaseConfig() {
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setDeferredWrite(true);
		return dbConfig;
	}



	/**
	 * Call after deserializing an instance of this class.  Will open the
	 * already seen in passed environment.
	 * @param env DB Environment to use.
	 * @throws DatabaseException
	 */
	public void reopen(final Environment env)
			throws DatabaseException {
		DatabaseConfig dbConfig = getDatabaseConfig();
		open(env, dbConfig);
	}

	protected void open(final Environment env, final DatabaseConfig dbConfig)
			throws DatabaseException {
		this.alreadySeen = env.openDatabase(null, DB_NAME, dbConfig);
	}

	public synchronized void close() {
		Environment env = null;
		if (this.alreadySeen != null) {
			try {
				env = this.alreadySeen.getEnvironment();
				this.alreadySeen.sync();
				this.alreadySeen.close();
			} catch (DatabaseException e) {
			}
			this.alreadySeen = null;
		}
		if (env != null && createdEnvironment) {
			try {
				// This sync flushes whats in RAM.  Its expensive operation.
				// Without, data can be lost.  Not for transactional operation.
				env.sync();
				env.close();
			} catch (DatabaseException e) {
			}
		}
	}


	/**
	 * Create fingerprint.
	 * Pubic access so test code can access createKey.
	 * @param uri URI to fingerprint.
	 * @return Fingerprint of passed <code>url</code>.
	 */
	public static long createKey(CharSequence uri) {
		String url = uri.toString();
		int index = url.indexOf(COLON_SLASH_SLASH);
		if (index > 0) {
			index = url.indexOf('/', index + COLON_SLASH_SLASH.length());
		}
		CharSequence hostPlusScheme = (index == -1)? url: url.subSequence(0, index);
		System.out.println("1"+hostPlusScheme);
		long tmp = FPGenerator.std24.fp(hostPlusScheme);
		return tmp | (FPGenerator.std40.fp(url) >>> 24);
	}




	/**
	 * @Description: 写入BD数据库
	 * @return:
	 * @date: 2017-9-24  
	 */
	protected boolean setAdd(CharSequence uri) {
		DatabaseEntry key = new DatabaseEntry();
		LongBinding.longToEntry(createKey(uri), key);

		OperationStatus status = null;
		try {
			//将压缩后得到的long型值作为key,ZERO_LENGTH_ENTRY(统一用一个byte长度的值来表示value)作为value
			status = alreadySeen.putNoOverwrite(null, key, ZERO_LENGTH_ENTRY);//写入
		} catch (DatabaseException e) {

		}
		if (status == OperationStatus.SUCCESS) {
			count++;
		}
		if(status == OperationStatus.KEYEXIST) {
			return false; // not added
		} else {
			return true;
		}
	}

	protected long setCount() {
		return count;
	}

	protected boolean setRemove(CharSequence uri) {
		DatabaseEntry key = new DatabaseEntry();
		LongBinding.longToEntry(createKey(uri), key);
		OperationStatus status = null;
		try {
			status = alreadySeen.delete(null, key);
		} catch (DatabaseException e) {
		}
		if (status == OperationStatus.SUCCESS) {
			count--;
			return true; // removed
		} else {
			return false; // not present
		}
	}

	public long flush() {
		// We always write but this might be place to do the sync
		// when checkpointing?  TODO.
		return 0;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		// sync deferred-write database
		try {
			alreadySeen.sync();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
		oos.defaultWriteObject();
	}
	
	
	public static void main(String args[]){
		CharSequence uri = "http://news.sina.com.cn/s/wh/2017-09-22/doc-ifymenmt6218226.shtml";
		System.out.println("2"+createKey(uri));
	}
}
