package com.dxc.common_tools;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Write_Xml {
	public void whenWriteStringUsingBufferedWritter_thenCorrect(String Directory_Name,String fileName,String content) throws IOException {			    
		File directory = new File(Directory_Name);
		if(!directory.exists()) {
			directory.mkdirs();
		}
		
		OutputStreamWriter writer =  new OutputStreamWriter(new FileOutputStream(Directory_Name + "/" + fileName), StandardCharsets.UTF_8);
			    						
			    writer.write(content);
			    writer.close();
			}

}
