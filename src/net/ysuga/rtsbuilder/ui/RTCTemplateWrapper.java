/**
 * RTCTemplateWrapper.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/10/09
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.rtsbuilder.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author ysuga
 * 
 */
public class RTCTemplateWrapper {

	public static File generate(File workingDir, String language,
			String moduleName, String moduleDesc, String moduleVendor,
			String moduleVersion, String moduleCompType, String moduleActType,
			String moduleCategory, String moduleMaxInstance,
			String namingFormats, String nameServers, String periodicRate,
			Map<String, String> inportMap, Map<String, String> outportMap)
			throws Exception {
		if (!workingDir.exists()) {
			throw new Exception();
		}

		List<String> arg = new ArrayList<String>();
		arg.add("C:/Python26/python");
		arg.add("\"C:/Program Files (x86)/OpenRTM-aist/1.1/utils/rtc-template/rtc-template.py\"");
		arg.add("-b" + language);
		arg.add("--module-name=" + moduleName);
		arg.add("--module-desc=\"" + moduleDesc + "\"");
		arg.add("--module-vendor=\"" + moduleVendor + "\"");
		arg.add("--module-version=" + moduleVersion);
		arg.add("--module-comp-type=" + moduleCompType);
		arg.add("--module-act-type=" + moduleActType);
		arg.add("--module-category=" + moduleCategory);
		arg.add("--module-max-inst=" + moduleMaxInstance);
		if(inportMap != null) {
			for(String name : inportMap.keySet()) {
				String type = inportMap.get(name);
				arg.add("--inport=" + name + ":" + type);
			}
		}
		if(outportMap != null) {
			for(String name : outportMap.keySet()) {
				String type = outportMap.get(name);
				arg.add("--outport=" + name + ":" + type);
			}
		}
		
		// Executing rtc-template.py
		ProcessBuilder pb = new ProcessBuilder(arg);
		pb.directory(workingDir);
		Process p = pb.start();
		p.waitFor();
		p.destroy();

		// Generating rtc.conf
		//String rtc_conf_str = "naming.formats:%n.rtc\ncorba.nameservers:localhost:2809";
		File rtcConfFile = new File(workingDir.getAbsolutePath() + "/" + "rtc.conf");
		BufferedWriter cbw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rtcConfFile)));
		cbw.write("naming.formats:"+namingFormats+"\n");
		cbw.write("corba.nameservers:"+nameServers+"\n");
		cbw.write("exec_cxt.periodic.rate:"+periodicRate+"\n");
		cbw.close();
		
		// Changing wrong import method in the beginning seciton of generated *.py file.
		File backupFile = new File(workingDir.getAbsolutePath() + "/"
				+ moduleName + ".bak" + ".py");
		File componentFile = new File(workingDir.getAbsolutePath() + "/"
				+ moduleName + ".py");
		componentFile.renameTo(backupFile);
		File newComponentFile = new File(workingDir.getAbsolutePath() + "/"
				+ moduleName + ".py");

		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(backupFile)));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(newComponentFile)));

		String str;
		while ((str = br.readLine()) != null) {
			if (str.equals("import OpenRTM")) {
				bw.write("import OpenRTM_aist as OpenRTM\n");
			} else {
				bw.write(str + "\n");
			}
		}
		bw.flush();
		br.close();
		bw.close();
		
		if (!backupFile.delete()) {
			throw new Exception("Failed to delete.");
		}
		return newComponentFile;
		
	}

}
