/**
 * RTSTreeNodeBuilder.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/02
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.corbanaming.ui;

import net.ysuga.corbanaming.RTNamingContext;

/**
 * <div lang="ja">
 *
 * </div>
 * <div lang="en">
 *
 * </div>
 * @author ysuga
 *
 */
public class RTSTreeNodeBuilder {

	/**
	 * <div lang="ja">
	 * �R���X�g���N�^
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	public RTSTreeNodeBuilder() {
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	
	/**
	 * 
	 * buildRTSTreeNode
	 * <div lang="ja">
	 * 
	 * @param namingContext
	 * @return
	 * </div>
	 * <div lang="en">
	 *
	 * @param namingContext
	 * @return
	 * </div>
	 */
	public static RTSTreeNode buildRTSTreeNode(RTNamingContext namingContext) {
		RTSTreeNode rootNode = new RTSTreeNode(namingContext);
		for(RTNamingContext nc : namingContext) {
			rootNode.add(buildRTSTreeNode(nc));
		}
		return rootNode;
		
	}
}
