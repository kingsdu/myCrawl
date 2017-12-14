package com.myCrawl.recovergz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecoverGZ {

	
    public static void main(String[] args) {  
    	String sourceFilePath = "G:\\data\\e430\\hetritrix\\output\\cnooc\\3\\recoverALL.txt";
    	String sourceFileEncoding = "utf-8";
    	String recoverGzDir = "G:\\data\\e430\\hetritrix\\output\\cnooc\\3";
    	String recoverGzFileName = "";
//    	readRecoverGZ(recoverGzDir,recoverGzFileName);
    	urlToRecoverUtilByFile(sourceFilePath,sourceFileEncoding,recoverGzDir,recoverGzFileName);
    } 
	
    
    
    
    /**
     * @Description: 读取recoverGZ
     * @return:
     * @date: 2017-9-26  
     */
    public static void readRecoverGZ(String recoverGzDir,String recoverGzFileName){
    	
//    	RecoveryJournal recover = null;
//    	
//    	//recover.gz文件为空则采用默认名字  
//		if(recoverGzFileName==null||recoverGzFileName.equals("")){  
//			recoverGzFileName="recover.gz";  
//		} 
//		recover = new RecoveryJournal(recoverGzDir,recoverGzFileName);//构造recover.gz对象  
//		recover.getBufferedReader(source);
    	InputStream is=null;  
		InputStreamReader isr=null;  
		BufferedReader br=null;  
		File sourceFile=null; 
		String sourceFileEncoding = "utf-8";
    	try {
    		sourceFile = new File(recoverGzDir);
    		
    		is=new FileInputStream(sourceFile);  
			isr=new InputStreamReader(is,sourceFileEncoding);  
			br=new BufferedReader(isr); 
			
			String line = null;
			
			while((line=br.readLine())!=null){
				System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    } 
    
    
    
    
    
    
    
	 /** 
     * 从文件中导入URl到recover.gz以便URL不再被抓取 
     *  
     * @param sourceFilePath        URL来源文件 
     * @param sourceFileEncoding    URL来源文件的编码  
     * @param recoverGzDir         要写到的recover.gz文件目录 
     * @param recoverGzFileName     recover.gz文件名,可以为空 
     * @return 
     */  
	public static boolean urlToRecoverUtilByFile(String sourceFilePath,String sourceFileEncoding,String recoverGzDir,String recoverGzFileName){
		boolean result=false;  

		InputStream is=null;  
		InputStreamReader isr=null;  
		BufferedReader br=null;  
		File sourceFile=null;  

		String line=null;  
		RecoveryJournal recover = null;  

		try {  
			sourceFile=new File(sourceFilePath);  

			//recover.gz文件为空则采用默认名字  
			if(recoverGzFileName==null||recoverGzFileName.equals("")){  
				recoverGzFileName="recover.gz";  
			}  
			recover = new RecoveryJournal(recoverGzDir,recoverGzFileName);//构造recover.gz对象  

			//读取文件内容  
			is=new FileInputStream(sourceFile);  
			isr=new InputStreamReader(is,sourceFileEncoding);  
			br=new BufferedReader(isr);  

			//一行一行写入recover.gz文件  
			while((line=br.readLine())!=null){  
				if(!line.equals("")){  
					recover.writeLine(line);  
				}  
			}  	
			result=true;  
		} catch (FileNotFoundException e) {  
			e.printStackTrace();  
		} catch (UnsupportedEncodingException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}finally{  
			try {  
				if(recover!=null){  
					recover.close();  
				}  
				if(br!=null){  
					br.close();  
				}  
				if(isr!=null){  
					isr.close();  
				}  
				if(is!=null){  
					is.close();  
				}  
			} catch (Exception e) {  
				e.printStackTrace();  
			}  
		}  
		return result;  
	}
	
	
	
	
	 /** 
     * 从ResultSet结果集中获取URL导入到recover.gz以便URl不再被抓取 
     *  
     * @param rs                ResultSet结果集 
     * @param filedName         ResultSet结果集中要获取URL对应的字段名        
     * @param recoverGzDir      要写到的recover.gz文件目录 
     * @param recoverGzFileName recover.gz文件名,可以为空 
     * @return 
     */  
    public static boolean urlToRecoverUtilByResultSet(ResultSet rs,String filedName,String recoverGzDir,String recoverGzFileName){  
        boolean result=false;  
          
        String line=null;  
        RecoveryJournal recover = null;  
          
        try {  
            if(recoverGzFileName==null||recoverGzFileName.equals("")){  
                recoverGzFileName="recover.gz";  
            }  
            recover=new RecoveryJournal(recoverGzDir,recoverGzFileName);  
            if(rs!=null){  
                while(rs.next()){  
                    line=rs.getString(filedName).trim();  
                    if(!line.equals("")){  
                        recover.writeLine(RecoveryJournal.F_SUCCESS, line);  
                    }  
                      
                }  
                result=true;  
            }  
        } catch (SQLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{  
            try {  
                if(rs!=null){  
                    rs.close();  
                }  
                if(recover!=null){  
                    recover.close();  
                }  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
        }     
          
        return result;  
    } 
    

    
    
 
  
}
