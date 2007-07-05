package org.redcross.sar.wp.tactics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IOperationAreaIf;

import com.esri.arcgis.interop.AutomationException;

public class TextAreaDialog extends DiskoDialog implements IDiskoMapEventListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private JScrollPane textAreaScrollPane = null;
	private JTextArea textArea = null;
	private JLabel headerLabel = null;
	
	public TextAreaDialog(DiskoWpTacticsImpl wp) {
		super(wp.getApplication().getFrame());
		//listener
		try {
			IMsoFeatureLayer msoLayer = wp.getApplication().getDiskoMapManager().
				getMsoLayer(IMsoFeatureLayer.LayerCode.OPERATION_AREA_LAYER);
			IMsoFeatureClass msoFC = (IMsoFeatureClass)msoLayer.getFeatureClass();
			msoFC.addDiskoMapEventListener(this);
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialize();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
            this.setPreferredSize(new Dimension(600, 125));
            this.setContentPane(getContentPanel());
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	public String getText() {
		return getTextArea().getText();
	}
	
	public void setText(String text) {
		getTextArea().setText(text);
	}
	
	public void setHeaderText(String text) {
		System.out.println(headerLabel.getFont());
		headerLabel.setText(text);
	}

	/**
	 * This method initializes contentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			try {
				headerLabel = new JLabel();
				contentPanel = new JPanel();
				contentPanel.setLayout(new BorderLayout());
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				contentPanel.add(getTextAreaScrollPane(), BorderLayout.CENTER);
				contentPanel.add(headerLabel, BorderLayout.NORTH);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
	}

	/**
	 * This method initializes textAreaScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTextAreaScrollPane() {
		if (textAreaScrollPane == null) {
			try {
				textAreaScrollPane = new JScrollPane();
				textAreaScrollPane.setViewportView(getTextArea());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return textAreaScrollPane;
	}

	/**
	 * This method initializes inputTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTextArea() {
		if (textArea == null) {
			try {
				textArea = new JTextArea();
				textArea.setLineWrap(true);
				textArea.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyTyped(java.awt.event.KeyEvent e) {
						fireDialogStateChanged();
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return textArea;
	}

	public void editLayerChanged(DiskoMapEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void onAfterScreenDraw(DiskoMapEvent e) throws IOException, AutomationException {
		// TODO Auto-generated method stub
	}

	public void onExtentUpdated(DiskoMapEvent e) throws IOException, AutomationException {
		// TODO Auto-generated method stub
	}

	public void onMapReplaced(DiskoMapEvent e) throws IOException, AutomationException {
		// TODO Auto-generated method stub
	}

	public void onSelectionChanged(DiskoMapEvent e) throws IOException, AutomationException {
		IMsoFeatureClass msoFC = (IMsoFeatureClass)e.getSource();
		List selection = msoFC.getSelected();
		if (selection != null && selection.size() > 0) {
			IMsoFeature msoFeature = (IMsoFeature)selection.get(0);
			IMsoObjectIf msoObject = msoFeature.getMsoObject();
			if (msoObject instanceof IAreaIf) {
				IAreaIf area = (IAreaIf)msoObject;
				IAssignmentIf assignment = area.getOwningAssignment();
				if (assignment != null) {
					setText(assignment.getRemarks());
					return;
				}
			}
			else if (msoObject instanceof IOperationAreaIf) {
				IOperationAreaIf opArea = (IOperationAreaIf)msoObject;
				setText(opArea.getRemarks());
				return;
			}
		}
		setText(null);
	}
}
