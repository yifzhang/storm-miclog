package com.mic.log.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
/**
 * 这是一个与日志读写有关的类，定义了一些通用的方法
 * @author ChenZhao
 *
 */
public class LogsWriteUtils implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5363343784637032351L;
	/**
     * 
     * @param filePath      文件路径的字符串表示形式
     * @param KeyWords      查找包含某个关键字的信息：非null为带关键字查询；null为全文显示
     * @return      当文件存在时，返回字符串；当文件不存在时，返回null
     */
    public static String readFromFile(String filePath, String KeyWords){
        StringBuffer stringBuffer = null;
        File file = new File(filePath);
        if(file.exists()){
            stringBuffer = new StringBuffer();
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            String temp = "";
            try {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                while((temp = bufferedReader.readLine()) != null){
                    if(KeyWords ==null){
                        stringBuffer.append(temp + "\n");
                    }else{
                        if(temp.contains(KeyWords)){
                            stringBuffer.append(temp + "\n");
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }finally{
                try {
                    fileReader.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
        if(stringBuffer == null){
            return null;
        }else{
            return stringBuffer.toString();
        }
         
         
    }
     
    /**
     * 将指定字符串写入文件。如果给定的文件路径不存在，将新建文件后写入。
     * @param log       要写入文件的字符串
     * @param filePath      文件路径的字符串表示形式，目录的层次分隔可以是“/”也可以是“\\”
     * @param isAppend      true：追加到文件的末尾；false：以覆盖原文件的方式写入
     */        
      
    public static boolean writeIntoFile(String log, String filePath, boolean isAppend){
        boolean isSuccess = true;
        //如有则将"\\"转为"/",没有则不产生任何变化
        String filePathTurn = filePath.replaceAll("\\\\", "/");
        //先过滤掉文件名
        int index = filePath.lastIndexOf("/");
        String dir = filePath.substring(0, index);
        //创建除文件的路径
        File fileDir = new File(dir);
        fileDir.mkdirs();
        //再创建路径下的文件
        File file = null;
        try {
            file = new File(filePath);
            file.createNewFile();
        } catch (IOException e) {
            isSuccess = false;
            //e.printStackTrace();
        }
        //将logs写入文件
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, isAppend);
            fileWriter.write(log);
            fileWriter.flush();
        } catch (IOException e) {
            isSuccess = false;
            //e.printStackTrace();
        } finally{
            try {
                fileWriter.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
         
        return isSuccess;
    }
    /**
     * 创建文件，如果该文件已存在将不再创建（即不起任何作用）
     * @param filePath       要创建文件的路径的字符串表示形式，目录的层次分隔可以是“/”也可以是“\\”
     * @return      创建成功将返回true；创建不成功则返回false
     */
    public static boolean createNewFile(String filePath){
        boolean isSuccess = true;
        //如有则将"\\"转为"/",没有则不产生任何变化
        String filePathTurn = filePath.replaceAll("\\\\", "/");
        //先过滤掉文件名
        int index = filePathTurn.lastIndexOf("/");
        String dir = filePathTurn.substring(0, index);
        //再创建文件夹
        File fileDir = new File(dir);
        isSuccess = fileDir.mkdirs();
        //创建文件
        File file = new File(filePathTurn);
        try {
            isSuccess = file.createNewFile();
        } catch (IOException e) {
            isSuccess = false;
            //e.printStackTrace();
        }
 
        return isSuccess;
    }
}
