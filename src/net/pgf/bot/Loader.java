package net.pgf.bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONObject;   


public class Loader {
	
	public String dir = System.getProperty("user.dir") + File.separator + "PGFbot";
	
	public File create(String path, String name) throws IOException
	{
		File file = new File(dir + File.separator + path);
		file.mkdirs();
		file = new File(dir + File.separator + path + File.separator + name + ".json");
		file.createNewFile();
		
		return file;
	}
	
	public void write(String path, String key, Object content) throws IOException // path can be "" (empty)
	{
		FileWriter fileWriter = new FileWriter(dir + File.separator + path);
		
		HashMap<String, Object> c = new HashMap<String, Object>();
		c.put(key, content);
		
		JSONObject jo = new JSONObject(c);
				
		fileWriter.write(jo.toString());
		fileWriter.close();
	}
	
	public void write(File file, String key, Object content) throws IOException // probably won't be used
	{
		FileWriter fileWriter = new FileWriter(file);
		
		HashMap<String, Object> c = new HashMap<String, Object>();
		c.put(key, content);
		
		JSONObject jo = new JSONObject(c);
				
		fileWriter.write(jo.toString());
		
		fileWriter.close();
	}
	
	public void read(String path)
	{
		File file = new File(dir + File.separator + path);
	}
	
	public void read(File file) throws FileNotFoundException // probably won't be used
	{
		FileReader fileReader = new FileReader(file);
		
		//fileReader.
	}

}
