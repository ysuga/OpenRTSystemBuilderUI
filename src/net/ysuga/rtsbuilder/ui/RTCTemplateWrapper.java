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

	public static void generate(File workingDir, String language,
			String moduleName, String moduleDesc, String moduleVendor,
			String moduleVersion, String moduleCompType, String moduleActType,
			String moduleCategory, String moduleMaxInstance,
			Map<String, String> inportMap, Map<String, String> outportMap)
			throws Exception {
		if (!workingDir.exists()) {
			throw new Exception();
		}

		List<String> arg = new ArrayList<String>();
		arg.add("C:/Python26/python");
		arg.add("\"C:/Program Files (x86)/OpenRTM-aist/1.0/utils/rtc-template/rtc-template.py\"");
		arg.add("-b" + language);
		arg.add("--module-name=" + moduleName);
		arg.add("--module-desc=\"" + moduleDesc + "\"");
		arg.add("--module-vendor=\"" + moduleVendor + "\"");
		arg.add("--module-version=" + moduleVersion);
		arg.add("--module-comp-type=" + moduleCompType);
		arg.add("--module-act-type=" + moduleActType);
		arg.add("--module-category=" + moduleCategory);
		arg.add("--module-max-inst=" + moduleMaxInstance);
		//arg.add("--module-lang=" + language);
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
		
		ProcessBuilder pb = new ProcessBuilder(arg);
		pb.directory(workingDir);
		Process p = pb.start();
		p.waitFor();
		p.destroy();

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
	}

}
