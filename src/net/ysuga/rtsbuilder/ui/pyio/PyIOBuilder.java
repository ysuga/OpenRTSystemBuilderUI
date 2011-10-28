/**
 * PyIOBuilder.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/09
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui.pyio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Set;

import net.ysuga.rtsbuilder.ui.RTCTemplateWrapper;
import net.ysuga.rtsystem.profile.DataPort;
import net.ysuga.rtsystem.profile.PyIOComponent;
import net.ysuga.rtsystem.profile.PythonRTCLauncher;

/**
 *
 * @author ysuga
 *
 */
public class PyIOBuilder {

	/**
	 * Constructor
	 */
	public PyIOBuilder() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	/**
	 * 
	 * generateAndExecute
	 *
	 * @param directory
	 * @param pyio
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static PythonRTCLauncher generateAndExecute(String directory, PyIOComponent pyio) throws IOException, InterruptedException {
		String fileName = generateCode(directory, pyio);
		File pythonFile = new File(fileName);
		File workingDir = new File(pythonFile.getParentFile().getAbsolutePath());
		if(!workingDir.exists()) {
			workingDir.mkdir();
		}
		if(pythonFile.exists()) {
			PythonRTCLauncher launcher = new PythonRTCLauncher(workingDir, pythonFile);
			launcher.execute();
			return launcher;
		}
		return null;
	}
	
	public static String generateCode(String directory, PyIOComponent pyio) {
		String language = "python";
		String moduleName = pyio.getModuleName();
		String moduleDesc = "";
		String moduleVendor = pyio.getVendor();
		String moduleVersion= pyio.getVersion();
		String moduleCompType = "DataFlowComponent";
		String moduleActType = "PERIODIC";
		String moduleCategory = pyio.getCategory();
		String moduleMaxInstance = "16";
		
		String periodicRate = pyio.getPeriodicRate();
		String namingFormats = pyio.getNamingContext();
		String nameServers = pyio.getNameServers();
		
		HashMap<String, String> inportMap = new HashMap<String, String>();
		HashMap<String, String> outportMap = new HashMap<String, String>();
		HashMap<String, String> servicePortMap = new HashMap<String, String>();
		for(DataPort dataPort: (Set<DataPort>)pyio.dataPortSet) {
			if(dataPort.getDirection() == DataPort.DIRECTION_IN) {
				inportMap.put(dataPort.getPlainName(), dataPort.getDataType());
			} else if(dataPort.getDirection() == DataPort.DIRECTION_OUT) {
				outportMap.put(dataPort.getPlainName(), dataPort.getDataType());
			} else {
				//inportMap.put(dataPort.getDataType(), dataPort.getPlainName());
			}
		}
		
		if(!directory.endsWith("/")) {
			directory = directory + "/";
		}
		File rtcDir = new File(directory + moduleName);
		int i = 2;
		while(rtcDir.exists()) {
			rtcDir = new File(directory  + moduleName + "(" + i + ")");	
			i++;
		}
		rtcDir.mkdirs();
		
		try {
			File componentFile = RTCTemplateWrapper.generate(rtcDir, language, moduleName, moduleDesc,
					moduleVendor, moduleVersion, moduleCompType, moduleActType, moduleCategory, moduleMaxInstance,
					namingFormats, nameServers, periodicRate, inportMap, outportMap);
			String componentFileName = componentFile.getAbsolutePath();
			File immediate = new File(componentFile.getAbsolutePath() + ".bak");
			componentFile.renameTo(immediate);
			File newComponentFile = new File(componentFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(immediate)));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newComponentFile)));
			
			String line;
			while((line = br.readLine()) != null) {
				String lineBuffer = line.trim();
				if(lineBuffer.startsWith("#def onExecute")) {
					line = br.readLine(); // Waste one line (this must be # return RTC_OK;
					
					BufferedReader sr = new BufferedReader(new StringReader(pyio.getOnExecuteCode()));
					
					while((line = sr.readLine()) != null) {
						bw.write("\t" + line + "\n");
					}
					sr.close();
				} else {
					bw.write(line + "\n");
				}
			}
			br.close();
			bw.close();
			
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return rtcDir.getAbsolutePath() + "/" + moduleName + ".py";
	}

}
