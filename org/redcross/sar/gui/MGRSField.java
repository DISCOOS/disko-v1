/**
 * 
 */
package org.redcross.sar.gui; 

import java.util.Arrays;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;

import java.awt.Insets;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComboBox;
//import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;
import javax.swing.JFormattedTextField;

import org.redcross.sar.app.IDiskoApplication;
import java.awt.GridLayout;
import javax.swing.BoxLayout;

/**
 * @author kennetgu
 *
 */
public class MGRSField extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel Right = null;
	private JLabel LatitudeLabel = null;
	private JFormattedTextField LatitudeText = null;
	private JLabel LongetudeLabel = null;
	private JPanel Left = null;
	private JFormattedTextField LongetudeText = null;
	private JLabel ZoneLabel = null;
	private JLabel SquareLabel = null;
	private IDiskoApplication m_app = null;
	
	private String[] zones = {"32V","33V","32W","33W","34W","35W","36W"};  //  @jve:decl-index=0:
	private String[] squares = {"KQ","KP","KN","KM","KL",
								"LQ","LP","LN","LM","LL","LK",
								"MR","MQ","MP","MN","MM","ML","MK",
								"NT","NS","NR","NQ","NP","NN","NM","NL","NK",
								"PA","PV","PU","PT","PS","PR","PQ","PP","PN","PM","PL",
								"UR","UQ","UP","UN","UM","UL","UK","UJ","UH","UG",
								"VS","VR","VQ","VP","VN","VM","VL",
								"WT","WS","WR","WQ","WP",
								"DD","DC","DB","DA",
								"ED","EC","EB",
								"FD","FC","FB",
								"LU","LT","LS",
								"MU","MT","MS",
								"NU","NT","NS",
								"PU","PT","PS",
								"UD","UC",
								"VD","VC"};  //  @jve:decl-index=0:
	private JComboBox ZoneCombo = null;
	private JComboBox SquareCombo = null;

	/**
	 * Constructor
	 */
	public MGRSField(IDiskoApplication app) {
		super();
		m_app = app;
		initialize();
	}

	/**
	 * Constructor
	 * 
	 * @param arg0
	 */
	public MGRSField(IDiskoApplication app, LayoutManager arg0) {
		super(arg0);
		m_app = app;
		initialize();
	}

	/**
	 * @param arg0
	 */
	public MGRSField(IDiskoApplication app, boolean arg0) {
		super(arg0);
		m_app = app;
		initialize();
	}

	/**
	 * Constructor
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public MGRSField(IDiskoApplication app, LayoutManager arg0, boolean arg1) {
		super(arg0, arg1);
		m_app = app;
		initialize();
	}

	/**
	 * This method set mgrs from text
	 * 
	 * @return void
	 */
	public void setText(String mgrs) {
		boolean bEmpty = false;
		// is not null?
		if (mgrs != null) {
			// is empty?
			if (mgrs.length() > 0) { 
				// get zone
				String zone = mgrs.subSequence(0, 3).toString();
				String square = mgrs.subSequence(3, 5).toString();
				String x = mgrs.subSequence(5, 10).toString();
				String y = mgrs.subSequence(10, 15).toString();
				getZoneCombo().setSelectedItem(zone);
				getSquareCombo().setSelectedItem(square);
				getLatitudeText().setText(x);
				getLongetudeText().setText(y);
			}
			else 
				bEmpty = true;
		}
		else
			bEmpty = true;
		
		if (bEmpty) {
			getZoneCombo().setSelectedIndex(-1);
			getSquareCombo().setSelectedIndex(-1);
			getLatitudeText().setText(null);
			getLongetudeText().setText(null);
		}			
	}
	
	/**
	 * This method get mgrs text
	 * 
	 * @return String
	 */
	public String getText() {
		String mgrs = getZoneCombo().getSelectedItem().toString() +
				getSquareCombo().getSelectedItem().toString() +
				getLatitudeText().getText() + getLongetudeText().getText();
		return mgrs;
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		// setup content pane
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.setSize(new Dimension(250, 45));
		this.setPreferredSize(new Dimension(250, 45));
		this.setMinimumSize(new Dimension(250, 45));
		this.setMaximumSize(new Dimension(250, 45));
		// add components
		this.add(getLeft(), null);
		this.add(getRight(), null);
	}

	/**
	 * This method initializes Left	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getLeft() {
		if (Left == null) {
			// create panel
			Left = new JPanel();
			Left.setPreferredSize(new Dimension(170, 45));
			Left.setLayout(new GridBagLayout());
			// create labels
			ZoneLabel = new JLabel();
			ZoneLabel.setText("Sone");
			SquareLabel = new JLabel();
			SquareLabel.setText("100-km rute");
			// create constraints
			GridBagConstraints c1 = new GridBagConstraints();
			c1.gridx = 0;
			c1.gridy = 0;
			c1.fill = GridBagConstraints.HORIZONTAL;
			c1.anchor = GridBagConstraints.NORTHWEST;
			c1.insets = new Insets(0, 0, 5, 5);
			GridBagConstraints c2 = new GridBagConstraints();
			c2.gridx = 1;
			c2.gridy = 0;
			c2.weightx = 0.5;
			c2.weighty = 0.5;
			c2.fill = GridBagConstraints.HORIZONTAL;
			c2.anchor = GridBagConstraints.NORTHWEST;
			c2.insets = new Insets(0, 0, 5, 0);
			GridBagConstraints c3 = new GridBagConstraints();
			c3.gridx = 0;
			c3.gridy = 1;
			c3.fill = GridBagConstraints.HORIZONTAL;
			c3.anchor = GridBagConstraints.NORTHWEST;
			c3.insets = new Insets(0, 0, 0, 5);
			GridBagConstraints c4 = new GridBagConstraints();
			c4.gridx = 1;
			c4.gridy = 1;
			c4.weightx = 0.5;
			c4.weighty = 0.5;
			c4.fill = GridBagConstraints.HORIZONTAL;
			c4.anchor = GridBagConstraints.NORTHWEST;
			c4.insets = new Insets(0, 0, 0, 0);
			Left.add(ZoneLabel, c1);
			Left.add(getZoneCombo(), c2);
			Left.add(SquareLabel, c3);
			Left.add(getSquareCombo(), c4);
		}
		return Left;
	}

	/**
	 * This method initializes Right	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getRight() {
		if (Right == null) {
			// create panel
			Right = new JPanel();
			Right.setPreferredSize(new Dimension(120, 45));
			Right.setLayout(new GridBagLayout());
			// create labels
			LatitudeLabel = new JLabel();
			LatitudeLabel.setText("X");
			LongetudeLabel = new JLabel();
			LongetudeLabel.setText("Y");
			// create constraints
			GridBagConstraints c1 = new GridBagConstraints();
			c1.gridx = 0;
			c1.gridy = 0;
			c1.fill = GridBagConstraints.HORIZONTAL;
			c1.anchor = GridBagConstraints.NORTHWEST;
			c1.weighty = 0.0;
			c1.weightx = 0.0;
			c1.insets = new Insets(0, 15, 5, 5);
			GridBagConstraints c2 = new GridBagConstraints();
			c2.gridx = 1;
			c2.gridy = 0;
			c2.weightx = 0.5;
			c2.weighty = 0.5;
			c2.fill = GridBagConstraints.HORIZONTAL;
			c2.anchor = GridBagConstraints.NORTHWEST;
			c2.insets = new Insets(0, 0, 5, 0);
			GridBagConstraints c3 = new GridBagConstraints();
			c3.gridx = 0;
			c3.gridy = 1;
			c3.anchor = GridBagConstraints.NORTHWEST;
			c3.fill = GridBagConstraints.NONE;
			c3.weightx = 0.0;
			c3.weighty = 0.0;
			c3.insets = new Insets(0, 15, 5, 5);
			GridBagConstraints c4 = new GridBagConstraints();
			c4.gridx = 1;
			c4.gridy = 1;
			c4.weightx = 0.5;
			c4.weighty = 0.5;
			c4.fill = GridBagConstraints.HORIZONTAL;
			c4.anchor = GridBagConstraints.NORTHWEST;
			c4.insets = new Insets(0, 0, 0, 0);
			Right.add(LatitudeLabel, c1);
			Right.add(getLatitudeText(), c2);
			Right.add(LongetudeLabel, c3);
			Right.add(getLongetudeText(), c4);
		}
		return Right;
	}	
	
	/**
	 * This method initializes LatitudeText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JFormattedTextField getLatitudeText() {
		if (LatitudeText == null) {
			MaskFormatter mf1 = null;
			try {
				mf1 = new MaskFormatter("#####");
				mf1.setPlaceholder("00000");
				mf1.setPlaceholderCharacter('0');
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			LatitudeText = new JFormattedTextField(mf1);
			LatitudeText.setPreferredSize(new Dimension(4, 20));
			LatitudeText.setText("");
			LatitudeText.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {					
					if (e.getClickCount() == 2){
						NumPadDialog numPadDialog = m_app.getUIFactory().getNumPadDialog();
						java.awt.Point p = LatitudeText.getLocationOnScreen();
						p.setLocation(p.x + (LatitudeText.getWidth()+7), p.y);
						numPadDialog.setLocation(p);					
						numPadDialog.setTextField(LatitudeText);
						numPadDialog.setVisible(true);	
					}
				}
			});	
		}
		return LatitudeText;
	}
	
	/**
	 * This method initializes LongetudeText	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JFormattedTextField getLongetudeText() {
		if (LongetudeText == null) {
			MaskFormatter mf1 = null;
			try {
				mf1 = new MaskFormatter("#####");
				mf1.setPlaceholderCharacter('0');
				mf1.setPlaceholder("00000");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			LongetudeText = new JFormattedTextField(mf1);
			LongetudeText.setPreferredSize(new Dimension(4, 20));
			LongetudeText.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {					
					if (e.getClickCount() == 2){
						NumPadDialog numPadDialog = m_app.getUIFactory().getNumPadDialog();
						java.awt.Point p = LongetudeText.getLocationOnScreen();
						p.setLocation(p.x + (LongetudeText.getWidth()+7), p.y);
						numPadDialog.setLocation(p);					
						numPadDialog.setTextField(LongetudeText);
						numPadDialog.setVisible(true);	
					}
				}
			});	
		}
		return LongetudeText;
	}

	/**
	 * This method initializes ZoneCombo	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getZoneCombo() {
		if (ZoneCombo == null) {
			Arrays.sort(zones);
			ZoneCombo = new JComboBox(zones);
			ZoneCombo.setPreferredSize(new Dimension(60, 20));
		}
		return ZoneCombo;
	}

	/**
	 * This method initializes SquareCombo	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getSquareCombo() {
		if (SquareCombo == null) {
			Arrays.sort(squares);
			SquareCombo = new JComboBox(squares);
			SquareCombo.setPreferredSize(new Dimension(60, 20));
		}
		return SquareCombo;
	}

}  //  @jve:decl-index=0:visual-constraint="7,7"
