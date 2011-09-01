package net.ysuga.rtsbuilder.ui;

import javax.swing.tree.DefaultMutableTreeNode;

import jp.go.aist.rtm.RTC.CorbaNaming;
import jp.go.aist.rtm.RTC.util.ORBUtil;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

public class RTSTreeNode extends DefaultMutableTreeNode {

	private NamingContext namingContext;
	private Binding binding;
	private String name;
	
	private RTSTreeNode(NamingContext namingContext, Binding binding) throws NotFound, CannotProceed, InvalidName {
		super();
		this.namingContext = namingContext;
		this.binding = binding;
		this.name = binding.binding_name[0].id + "|" + binding.binding_name[0].kind;
		if(binding.binding_type == BindingType.ncontext) {
			NamingContext nextNamingContext = (NamingContext)namingContext.resolve(binding.binding_name);
			BindingListHolder bl = new BindingListHolder();
			BindingIteratorHolder bi = new BindingIteratorHolder();
			nextNamingContext.list(30, bl, bi);
			for(Binding nextBinding : bl.value ) {
				add(new RTSTreeNode(nextNamingContext, nextBinding));
			}
		} else {
			
		}
	}
	
	private RTSTreeNode(String name) {
		super();
		this.name = name;
	}
	
	static public RTSTreeNode create(String hostAddress) throws Exception {
		String[] arg = {"main"};
		ORB orb = ORBUtil.getOrb(arg);
		CorbaNaming naming = new CorbaNaming(orb, hostAddress);
		RTSTreeNode rootNode = new RTSTreeNode(hostAddress);
		NamingContext rootContext = naming.getRootContext();
		BindingListHolder bl = new BindingListHolder();
		BindingIteratorHolder bi = new BindingIteratorHolder();
		rootContext.list(30, bl, bi);
		for(Binding binding : bl.value) {
			rootNode.add(new RTSTreeNode(rootContext, binding));
		}
		
		return rootNode;
	}
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#isLeaf()
	 */
	@Override
	public boolean isLeaf() {
		if(binding != null) {
			return !(binding.binding_type == BindingType.ncontext);
		}else {
			// Root Node;
			return false;
		}
		
	}
	/* (non-Javadoc)
	 * @see javax.swing.tree.DefaultMutableTreeNode#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
	
	
	
}
