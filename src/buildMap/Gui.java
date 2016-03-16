package buildMap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Set;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

public class Gui extends JFrame implements ActionListener {
	private static final long serialVersionUID = -7457350242559078527L;
	private CanvasMap cm;
	int width = 1700, height = 1000;
	JButton process, clusterbt, clusterCoefBt;
	JCheckBox originalButton, graphButton, backButton, showNodes,clusters,highTags;
	JTextField th1, th2, th3,thTagTF;
	JComboBox<String> nodes;
	JComboBox<String> tagList;
	double threshold1, threshold2, thTag=0;
	int cutNode;
	int seqNumber=0;
	boolean original = true, showMap = true, background = true,
			mapGenerated = false, nodesMode = false, selectNodeChanged = false, 
			showCluster = false, tagMode = false, selectTagChanged = false, thTagMode=false,
			zoneGenerated=false;
	
	int selectedNode = -1 ;
	String selectedTag = null;
	
	String dataSetPath="/home/jcarlos2289/Descargas/";
	
	BuildMap bm;
	Kmeans km;
	String name ;
	
	 JMenu jmOperations, jmShows;
	 JMenuItem jmiGetCluster, jmiGenCluster, jmiCapture, jmiGenMap, jmiGenAllMaps, jmiVidSq1, jmiGenZone, jmiGenHierCluster, jmiNodeComposition;
     JCheckBoxMenuItem originalCB, graphCB, backCB, showNodesCB, clustersCB, highTagsCB, thTagsCB,zoneCB;
     JMenu jMDataSet,jMSunny,jMCloudy, jMNight;
     JMenuItem jmiCl_1, jmiCl_2, jmiCl_3, jmiCl_4, jmiNi_1, jmiNi_2, jmiNi_3, jmiNi_4, jmiSu_1, jmiSu_2, jmiSu_3, jmiSu_4;
     JLabel statusLabel;
     
     JMenuItem jMiHybrid_Sq1;
     JMenuItem jMiHybrid_Sq2;
     JMenuItem jMiHybrid_Sq3;
     JMenuItem jMiHybrid_Sq4;
     JMenuItem jMiHybrid_Sq5;
     JMenuItem jMIImgAlex_Sq1;
     JMenuItem jMIImgAlex_Sq2;
     JMenuItem jMIImgAlex_Sq3;
     JMenuItem jMIImgAlex_Sq4;
     JMenuItem jMIImgAlex_Sq5;
     JMenuItem jMIImgCaff_Sq1;
     JMenuItem jMIImgCaff_Sq2;
     JMenuItem jMIImgCaff_Sq3;
     JMenuItem jMIImgCaff_Sq4;
     JMenuItem jMIImgCaff_Sq5;
     JMenuItem jMIImgGNet_Sq1;
     JMenuItem jMIImgGNet_Sq2;
     JMenuItem jMIImgGNet_Sq3;
     JMenuItem jMIImgGNet_Sq4;
     JMenuItem jMIImgGNet_Sq5;
     JMenuItem jMIImgVGG_Sq1;
     JMenuItem jMIImgVGG_Sq2;
     JMenuItem jMIImgVGG_Sq3;
     JMenuItem jMIImgVGG_Sq4;
     JMenuItem jMIImgVGG_Sq5;
     JMenuItem jMIPlaAlex_Sq1;
     JMenuItem jMIPlaAlex_Sq2;
     JMenuItem jMIPlaAlex_Sq3;
     JMenuItem jMIPlaAlex_Sq4;
     JMenuItem jMIPlaAlex_Sq5;
     JMenuItem jMIPlaGNet_Sq1;
     JMenuItem jMIPlaGNet_Sq2;
     JMenuItem jMIPlaGNet_Sq3;
     JMenuItem jMIPlaGNet_Sq4;
     JMenuItem jMIPlaGNet_Sq5;
     
     JMenuItem jMIImgMX_Sq1;
     JMenuItem jMIImgMX_Sq2;
     JMenuItem jMIImgMX_Sq3;
     JMenuItem jMIImgMX_Sq4;
     JMenuItem jMIImgMX_Sq5;
          
     JMenuItem jMIImgMX21K_Sq1;
     JMenuItem jMIImgMX21K_Sq2;
     JMenuItem jMIImgMX21K_Sq3;
     JMenuItem jMIImgMX21K_Sq4;
     JMenuItem jMIImgMX21K_Sq5;

     
     JMenuItem jMIImgAlex_Sq6;
     JMenuItem jMIImgCaff_Sq6;
     JMenuItem jMIImgGNet_Sq6;
     JMenuItem jMIImgMX_Sq6;
     JMenuItem jMIImgVGG_Sq6;
     JMenuItem jMIImgMX21K_Sq6;
     JMenu jMRelation;
     JPopupMenu.Separator jSeparator1;
     JMenuItem jMIGetNodesContent;
     
     
     JMenu jMVidrilo;
     JMenu jMSeq1,jMSeq2, jMSeq3, jMSeq4, jMSeq5;
     
    
	
	public Gui() {
		threshold1 = 0.001;
		threshold2 = 0.05;
		cutNode = 1;
		bm = new BuildMap(threshold1, threshold2, cutNode);
	
		
		getContentPane().setLayout(new BorderLayout());
		setSize(width, height);
		setTitle(name+" TriangleZone");
		cm = new CanvasMap(this);
		//setTitle("Topological Mapping");
		getContentPane().add(cm, BorderLayout.CENTER);
		getContentPane().add(getToolBar(), BorderLayout.NORTH);
		getContentPane().add(getStatusBar(), BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		
		
	}
	
	
	public JPanel getStatusBar(){
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		//frame.add(statusPanel, BorderLayout.SOUTH);
		
		//statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel("SemanticMapping 1");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		
		
		return statusPanel;
	}

	public JPanel getToolBar() {
		
		    jmOperations = new JMenu();
	        //jmOperations.addActionListener(this);
	        jmOperations.setText("Operations");

	        jmiGenMap = new JMenuItem("Gen Map");
	        jmiGenMap.addActionListener(this);
	        jmiGenMap.setEnabled(false);
	        jmOperations.add(jmiGenMap);
	        
	        jmiGenZone = new JMenuItem("Gen Zones");
	        jmiGenZone.addActionListener(this);
	        jmiGenZone.setEnabled(false);
	        jmOperations.add(jmiGenZone);
	           
	        jmiGenHierCluster = new JMenuItem("Gen Hierarchical Clustering");
	        jmiGenHierCluster.addActionListener(this);
	        jmiGenHierCluster.setEnabled(false);
	        jmOperations.add(jmiGenHierCluster);

	        jmiGetCluster = new JMenuItem();
	        jmiGetCluster.setText("Get K Cluster");
	        jmiGetCluster.addActionListener(this);
	        jmOperations.add(jmiGetCluster);

	        jmiGenCluster = new JMenuItem();
	        jmiGenCluster.setText("Gen Cluster");
	        jmiGenCluster.addActionListener(this);
	        jmOperations.add(jmiGenCluster);

	        jmiCapture = new JMenuItem("Capture Screen");
	        jmiCapture.addActionListener(this);
	        jmiCapture.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
	        jmOperations.add(jmiCapture);
	        
	        jmiGenAllMaps = new JMenuItem("Gen 12 Idol Maps");
	        jmiGenAllMaps.addActionListener(this);
	        jmOperations.add(jmiGenAllMaps);
	        
	        jSeparator1 = new JPopupMenu.Separator();
	        jMRelation = new JMenu();
	        jMIImgMX_Sq6 = new JMenuItem();
	        jMIImgAlex_Sq6 = new JMenuItem();
	        jMIImgCaff_Sq6 = new JMenuItem();
	        jMIImgGNet_Sq6 = new JMenuItem();
	        jMIImgVGG_Sq6 = new JMenuItem();
	        jMIImgMX21K_Sq6= new JMenuItem();
	        
	        jMIGetNodesContent = new JMenuItem();
	        
	        jMIImgMX_Sq6.addActionListener(this);
	        jMIImgAlex_Sq6.addActionListener(this);
	        jMIImgCaff_Sq6.addActionListener(this);
	        jMIImgGNet_Sq6.addActionListener(this);
	        jMIImgVGG_Sq6.addActionListener(this);
	        jMIImgMX21K_Sq6.addActionListener(this);
	        
	        
	        
	        jmOperations.add(jSeparator1);

	        jMRelation.setText("Get Relationship with");
	        jMRelation.setEnabled(false);

	        jMIImgMX_Sq6.setText("ImageNet_MXNet");
	        jMRelation.add(jMIImgMX_Sq6);
	        
	        jMIImgMX21K_Sq6.setText("ImageNet_MXNet21K");
	        jMRelation.add(jMIImgMX21K_Sq6);

	        jMIImgAlex_Sq6.setText("ImageNet_AlexNet");
	        jMRelation.add(jMIImgAlex_Sq6);

	        jMIImgCaff_Sq6.setText("ImageNetCaffeNet");
	        jMRelation.add(jMIImgCaff_Sq6);

	        jMIImgGNet_Sq6.setText("ImageNet_GoogLeNet");
	        jMRelation.add(jMIImgGNet_Sq6);

	        jMIImgVGG_Sq6.setText("ImageNet_VGG");
	        jMRelation.add(jMIImgVGG_Sq6);
	        
	        
	        jmOperations.add(jMRelation);
	        
	        jmiNodeComposition = new JMenuItem("Node Composition");
	        jmiNodeComposition.addActionListener(this);
	        jmiNodeComposition.setEnabled(false);
	        jmOperations.add(jmiNodeComposition);
	        
	        jMIGetNodesContent.setText("Export Nodes Information");
	        jMIGetNodesContent.addActionListener(this);
	        jMIGetNodesContent.setEnabled(false);
	        jmOperations.add(jMIGetNodesContent);
	       
	        jmShows = new JMenu("View");
	        
	        originalCB= new javax.swing.JCheckBoxMenuItem("OriginalMap");
	        originalCB.setSelected(true);
	        originalCB.addActionListener(this);
	                
	        graphCB = new javax.swing.JCheckBoxMenuItem("Graph");
	        graphCB.addActionListener(this);
	        graphCB.setSelected(true);
	        graphCB.setEnabled(mapGenerated);
	        
	        
	        backCB = new javax.swing.JCheckBoxMenuItem("Background Image");
	        backCB.setSelected(true);
	        backCB.addActionListener(this);
	        
	           
	        showNodesCB = new javax.swing.JCheckBoxMenuItem("Show Nodes");
	        showNodesCB.setSelected(false);
	        showNodesCB.setEnabled(false);
	        showNodesCB.addActionListener(this);
	        
	        
	        clustersCB =new JCheckBoxMenuItem("Clusters");
	        clustersCB.addActionListener(this);
	        clustersCB.setSelected(false);
			clustersCB.setEnabled(false);
			
			highTagsCB = new JCheckBoxMenuItem("Show High Tag");
			highTagsCB.addActionListener(this);
			highTagsCB.setSelected(false);
			highTagsCB.setToolTipText("A termic color scale for the whole map");
			highTagsCB.setEnabled(false);
			
			
			thTagsCB = new JCheckBoxMenuItem("Show High Threshold Tags");
			thTagsCB.addActionListener(this);
			thTagsCB.setSelected(false);
			thTagsCB.setToolTipText("A termic color scale for every node in the map.");
			thTagsCB.setEnabled(false);
			
			zoneCB = new JCheckBoxMenuItem("Show Zones");
			zoneCB.addActionListener(this);
			zoneCB.setSelected(false);
			zoneCB.setEnabled(false);
					
	                
	        jmShows.add(originalCB);
	        jmShows.add(graphCB);
	        jmShows.add(backCB);
	        jmShows.add(showNodesCB);
	        jmShows.add(clustersCB);
	        jmShows.add(highTagsCB);
	        jmShows.add(thTagsCB);
	        jmShows.add(zoneCB);
	        
	        //JPanel jp2 = new JPanel();
	        JMenuBar jMenuBar1 = new JMenuBar();
	        jMenuBar1.add(jmOperations);
	        jMenuBar1.add(jmShows);
	        
	          
	        jMDataSet = new javax.swing.JMenu();
	        
	        
	        jMDataSet.setText("Select Data");
     
	        
	        jMSeq1 = new JMenu();
	        jMiHybrid_Sq1 = new JMenuItem();
	        jMIImgAlex_Sq1 = new JMenuItem();
	        jMIImgCaff_Sq1 = new JMenuItem();
	        jMIImgGNet_Sq1 = new JMenuItem();
	        jMIImgVGG_Sq1 = new JMenuItem();
	        jMIPlaAlex_Sq1 = new JMenuItem();
	        jMIPlaGNet_Sq1 = new JMenuItem();
	        jMIImgMX_Sq1 = new JMenuItem();
	        jMSeq2 = new JMenu();
	        jMiHybrid_Sq2 = new JMenuItem();
	        jMIImgAlex_Sq2 = new JMenuItem();
	        jMIImgCaff_Sq2 = new JMenuItem();
	        jMIImgGNet_Sq2 = new JMenuItem();
	        jMIImgVGG_Sq2 = new JMenuItem();
	        jMIPlaAlex_Sq2 = new JMenuItem();
	        jMIPlaGNet_Sq2 = new JMenuItem();
	        jMSeq3 = new JMenu();
	        jMiHybrid_Sq3 = new JMenuItem();
	        jMIImgAlex_Sq3 = new JMenuItem();
	        jMIImgCaff_Sq3 = new JMenuItem();
	        jMIImgGNet_Sq3 = new JMenuItem();
	        jMIImgVGG_Sq3 = new JMenuItem();
	        jMIPlaAlex_Sq3 = new JMenuItem();
	        jMIPlaGNet_Sq3 = new JMenuItem();
	        jMSeq4 = new JMenu();
	        jMiHybrid_Sq4 = new JMenuItem();
	        jMIImgAlex_Sq4 = new JMenuItem();
	        jMIImgCaff_Sq4 = new JMenuItem();
	        jMIImgGNet_Sq4 = new JMenuItem();
	        jMIImgVGG_Sq4 = new JMenuItem();
	        jMIPlaAlex_Sq4 = new JMenuItem();
	        jMIPlaGNet_Sq4 = new JMenuItem();
	        jMSeq5 = new JMenu();
	        jMiHybrid_Sq5 = new JMenuItem();
	        jMIImgAlex_Sq5 = new JMenuItem();
	        jMIImgCaff_Sq5 = new JMenuItem();
	        jMIImgGNet_Sq5 = new JMenuItem();
	        jMIImgVGG_Sq5 = new JMenuItem();
	        jMIPlaAlex_Sq5 = new JMenuItem();
	        jMIPlaGNet_Sq5 = new JMenuItem();
	        jMVidrilo = new JMenu();
	        
	        jMIImgMX_Sq1= new JMenuItem();
	        jMIImgMX_Sq2= new JMenuItem();
	        jMIImgMX_Sq3= new JMenuItem();
	        jMIImgMX_Sq4= new JMenuItem();
	        jMIImgMX_Sq5= new JMenuItem();
	             
	        jMIImgMX21K_Sq1= new JMenuItem();
	        jMIImgMX21K_Sq2= new JMenuItem();
	        jMIImgMX21K_Sq3= new JMenuItem();
	        jMIImgMX21K_Sq4= new JMenuItem();
	        jMIImgMX21K_Sq5= new JMenuItem();
	       
	        
	        
	        
	        jMVidrilo.setText("Vidrilo");

	        jMSeq1.setText("Sequence1");
	        
	        jMiHybrid_Sq1.addActionListener(this);
	        jMIImgAlex_Sq1.addActionListener(this);
	        jMIImgCaff_Sq1.addActionListener(this);
	        jMIImgGNet_Sq1.addActionListener(this);
	        jMIImgVGG_Sq1.addActionListener(this);
	        jMIPlaAlex_Sq1.addActionListener(this);
	        jMIPlaGNet_Sq1.addActionListener(this);
	        jMIImgMX_Sq1.addActionListener(this);
	        jMIImgMX21K_Sq1.addActionListener(this);
	        
	        jMiHybrid_Sq2.addActionListener(this);
	        jMIImgAlex_Sq2.addActionListener(this);
	        jMIImgCaff_Sq2.addActionListener(this);
	        jMIImgGNet_Sq2.addActionListener(this);
	        jMIImgVGG_Sq2.addActionListener(this);
	        jMIPlaAlex_Sq2.addActionListener(this);
	        jMIPlaGNet_Sq2.addActionListener(this);
	        jMIImgMX_Sq2.addActionListener(this);
	        jMIImgMX21K_Sq2.addActionListener(this);
	        
	        jMiHybrid_Sq3.addActionListener(this);
	        jMIImgAlex_Sq3.addActionListener(this);
	        jMIImgCaff_Sq3.addActionListener(this);
	        jMIImgGNet_Sq3.addActionListener(this);
	        jMIImgVGG_Sq3.addActionListener(this);
	        jMIPlaAlex_Sq3.addActionListener(this);
	        jMIPlaGNet_Sq3.addActionListener(this);
	        jMIImgMX_Sq3.addActionListener(this);
	        jMIImgMX21K_Sq3.addActionListener(this);
	        
	        jMiHybrid_Sq4.addActionListener(this);
	        jMIImgAlex_Sq4.addActionListener(this);
	        jMIImgCaff_Sq4.addActionListener(this);
	        jMIImgGNet_Sq4.addActionListener(this);
	        jMIImgVGG_Sq4.addActionListener(this);
	        jMIPlaAlex_Sq4.addActionListener(this);
	        jMIPlaGNet_Sq4.addActionListener(this);
	        jMIImgMX_Sq4.addActionListener(this);
	        jMIImgMX21K_Sq4.addActionListener(this);
	        
	        jMiHybrid_Sq5.addActionListener(this);
	        jMIImgAlex_Sq5.addActionListener(this);
	        jMIImgCaff_Sq5.addActionListener(this);
	        jMIImgGNet_Sq5.addActionListener(this);
	        jMIImgVGG_Sq5.addActionListener(this);
	        jMIPlaAlex_Sq5.addActionListener(this);
	        jMIPlaGNet_Sq5.addActionListener(this);
	        jMIImgMX_Sq5.addActionListener(this);
	        jMIImgMX21K_Sq5.addActionListener(this);
	        

	        jMiHybrid_Sq1.setText("Hybrid");
	        jMSeq1.add(jMiHybrid_Sq1);
	        
	        jMIImgAlex_Sq1.setText("ImageNet_AlexNet");
	        jMSeq1.add(jMIImgAlex_Sq1);

	        jMIImgCaff_Sq1.setText("ImageNetCaffeNet");
	        jMSeq1.add(jMIImgCaff_Sq1);

	        jMIImgGNet_Sq1.setText("ImageNet_GoogLeNet");
	        jMSeq1.add(jMIImgGNet_Sq1);

	        jMIImgVGG_Sq1.setText("ImageNet_VGG");
	        jMSeq1.add(jMIImgVGG_Sq1);

	        jMIPlaAlex_Sq1.setText("Places_AlexNet");
	        jMSeq1.add(jMIPlaAlex_Sq1);

	        jMIPlaGNet_Sq1.setText("Places_GoogLeNet");
	        jMSeq1.add(jMIPlaGNet_Sq1);
	        
	        jMIImgMX_Sq1.setText("ImageNet_MXNet");
	        jMSeq1.add(jMIImgMX_Sq1);
	        
	        jMIImgMX21K_Sq1.setText("ImageNet_MXNet21K");
	        jMSeq1.add(jMIImgMX21K_Sq1);

	        jMVidrilo.add(jMSeq1);

	        jMSeq2.setText("Sequence2");

	        jMiHybrid_Sq2.setText("Hybrid");
	        jMSeq2.add(jMiHybrid_Sq2);

	        jMIImgAlex_Sq2.setText("ImageNet_AlexNet");
	        jMSeq2.add(jMIImgAlex_Sq2);

	        jMIImgCaff_Sq2.setText("ImageNetCaffeNet");
	        jMSeq2.add(jMIImgCaff_Sq2);

	        jMIImgGNet_Sq2.setText("ImageNet_GoogLeNet");
	        jMSeq2.add(jMIImgGNet_Sq2);

	        jMIImgVGG_Sq2.setText("ImageNet_VGG");
	        jMSeq2.add(jMIImgVGG_Sq2);

	        jMIPlaAlex_Sq2.setText("Places_AlexNet");
	        jMSeq2.add(jMIPlaAlex_Sq2);

	        jMIPlaGNet_Sq2.setText("Places_GoogLeNet");
	        jMSeq2.add(jMIPlaGNet_Sq2);
	        
	        jMIImgMX_Sq2.setText("ImageNet_MXNet");
	        jMSeq2.add(jMIImgMX_Sq2);
	        
	        jMIImgMX21K_Sq2.setText("ImageNet_MXNet21K");
	        jMSeq2.add(jMIImgMX21K_Sq2);

	        jMVidrilo.add(jMSeq2);

	        jMSeq3.setText("Sequence3");

	        jMiHybrid_Sq3.setText("Hybrid");
	        jMSeq3.add(jMiHybrid_Sq3);

	        jMIImgAlex_Sq3.setText("ImageNet_AlexNet");
	        jMSeq3.add(jMIImgAlex_Sq3);

	        jMIImgCaff_Sq3.setText("ImageNetCaffeNet");
	        jMSeq3.add(jMIImgCaff_Sq3);

	        jMIImgGNet_Sq3.setText("ImageNet_GoogLeNet");
	        jMSeq3.add(jMIImgGNet_Sq3);

	        jMIImgVGG_Sq3.setText("ImageNet_VGG");
	        jMSeq3.add(jMIImgVGG_Sq3);

	        jMIPlaAlex_Sq3.setText("Places_AlexNet");
	        jMSeq3.add(jMIPlaAlex_Sq3);

	        jMIPlaGNet_Sq3.setText("Places_GoogLeNet");
	        jMSeq3.add(jMIPlaGNet_Sq3);
	        
	        jMIImgMX_Sq3.setText("ImageNet_MXNet");
	        jMSeq3.add(jMIImgMX_Sq3);
	        
	        jMIImgMX21K_Sq3.setText("ImageNet_MXNet21K");
	        jMSeq3.add(jMIImgMX21K_Sq3);

	        jMVidrilo.add(jMSeq3);

	        jMSeq4.setText("Sequence4");

	        jMiHybrid_Sq4.setText("Hybrid");
	        jMSeq4.add(jMiHybrid_Sq4);

	        jMIImgAlex_Sq4.setText("ImageNet_AlexNet");
	        jMSeq4.add(jMIImgAlex_Sq4);

	        jMIImgCaff_Sq4.setText("ImageNetCaffeNet");
	        jMSeq4.add(jMIImgCaff_Sq4);

	        jMIImgGNet_Sq4.setText("ImageNet_GoogLeNet");
	        jMSeq4.add(jMIImgGNet_Sq4);

	        jMIImgVGG_Sq4.setText("ImageNet_VGG");
	        jMSeq4.add(jMIImgVGG_Sq4);

	        jMIPlaAlex_Sq4.setText("Places_AlexNet");
	        jMSeq4.add(jMIPlaAlex_Sq4);

	        jMIPlaGNet_Sq4.setText("Places_GoogLeNet");
	        jMSeq4.add(jMIPlaGNet_Sq4);
	        
	        jMIImgMX_Sq4.setText("ImageNet_MXNet");
	        jMSeq4.add(jMIImgMX_Sq4);
	        
	        jMIImgMX21K_Sq4.setText("ImageNet_MXNet21K");
	        jMSeq4.add(jMIImgMX21K_Sq4);
	        

	        jMVidrilo.add(jMSeq4);

	        jMSeq5.setText("Sequence5");

	        jMiHybrid_Sq5.setText("Hybrid");
	        jMSeq5.add(jMiHybrid_Sq5);

	        jMIImgAlex_Sq5.setText("ImageNet_AlexNet");
	        jMSeq5.add(jMIImgAlex_Sq5);

	        jMIImgCaff_Sq5.setText("ImageNetCaffeNet");
	        jMSeq5.add(jMIImgCaff_Sq5);

	        jMIImgGNet_Sq5.setText("ImageNet_GoogLeNet");
	        jMSeq5.add(jMIImgGNet_Sq5);

	        jMIImgVGG_Sq5.setText("ImageNet_VGG");
	        jMSeq5.add(jMIImgVGG_Sq5);

	        jMIPlaAlex_Sq5.setText("Places_AlexNet");
	        jMSeq5.add(jMIPlaAlex_Sq5);

	        jMIPlaGNet_Sq5.setText("Places_GoogLeNet");
	        jMSeq5.add(jMIPlaGNet_Sq5);
	        
	        jMIImgMX_Sq5.setText("ImageNet_MXNet");
	        jMSeq5.add(jMIImgMX_Sq5);
	        
	        jMIImgMX21K_Sq5.setText("ImageNet_MXNet21K");
	        jMSeq5.add(jMIImgMX21K_Sq5);

	        jMVidrilo.add(jMSeq5);

	        jMDataSet.add(jMVidrilo);

	        jMenuBar1.add(jMDataSet);

	        this.setJMenuBar(jMenuBar1);
		
		
		
		JPanel jp = new JPanel();
		jp.setSize(width, 100);
		jp.setLayout(new BoxLayout(jp, BoxLayout.LINE_AXIS));
		jp.setAlignmentX(LEFT_ALIGNMENT);
		
		
		
		JLabel lab1 = new JLabel("Threshold1");
		jp.add(lab1);
		th1 = new JTextField(String.valueOf(threshold1));
		th1.addActionListener(this);
		jp.add(th1);
		JLabel lab2 = new JLabel("Threshold2");
		jp.add(lab2);
		th2 = new JTextField(String.valueOf(threshold2));
		th2.addActionListener(this);
		jp.add(th2);
		JLabel lab3 = new JLabel("CutNode");
		jp.add(lab3);
		th3 = new JTextField(String.valueOf(cutNode));
		th3.addActionListener(this);
		jp.add(th3);
		
		//th1.setEnabled(false);
		//th2.setEnabled(false);
		//th3.setEnabled(false);
		
		
		
		JLabel lab4 = new JLabel("TagThreshold");
		jp.add(lab4);
		thTagTF=new JTextField(String.valueOf(thTag));
		thTagTF.addActionListener(this);
		thTagTF.setEnabled(false);
		
	
		jp.add(thTagTF);
		
	
		
		String[] aux = {"Select Node"};
		nodes = new JComboBox<String>(aux);
		nodes.addActionListener(this);
		nodes.setEnabled(nodesMode);
		jp.add(nodes);
		
		
		String[] aux2 = {"Select Tag"};
		tagList = new JComboBox<String>(aux2);
		tagList.addActionListener(this);
		tagList.setEnabled(tagMode); 
		jp.add(tagList);
		
		
		
		return jp;
	}

	public void genComboNodes() {
		int size = bm.map.nodes.size();
		String[] aux = new String[size + 1];
		aux[0] = "Select Node";
		for (int i = 1; i < size + 1; i++) {
			aux[i] = String.valueOf(i - 1) + " "
					+ bm.map.getNode(i - 1).getSize();
		}
		nodes.setModel(new DefaultComboBoxModel<String>(aux));
		nodes.setSelectedIndex(0);
		//Imprimir lista de nodos
		/*String lst = "";
		for (int i = 0; i < aux.length; i++) {
			lst += aux[i] + "\n";
		}*/

		// FileMethods.saveFile(lst, "NodeList", false);
	}
	
	public void genComboTag(){
		int size = bm.dimension;
		String[] aux = new String[size + 1];
		aux[0] = "Select Tag";
		
		ImageTags auxImage;
		
		auxImage = bm.imgTags.get(0);
		Set<String> keyset = auxImage.tags.keySet();
		
		ArrayList<String> keys =new ArrayList<String>();
		
		for (String object : keyset) {
			keys.add(object);
		}
		
		Collections.sort(keys);
		
		for (int i = 1; i < size + 1; i++) {
			aux[i] =keys.get(i-1);
		}
		
	
		tagList.setModel(new DefaultComboBoxModel<String>(aux));
		tagList.setSelectedIndex(0);
		
	}
	

	public static void main(String[] args) {
		Gui g = new Gui();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		g.setLocationRelativeTo(null);
		 g.setVisible(true);
	     g.toFront();
	     
	     
	    
	     
		
		// Each tag contains a name and probability assigned to it by the recognition engine.
        //System.out.println(String.format("  %s (%.4f)", tag.getName(), tag.getProbability()));
		
		/*
			
		double val=0;
        if (args.length ==0||args.length < 1){
            System.out.println("Ingresa el Th2");
            return;
        }else
            val = Double.parseDouble(args[0]);
        
        g.name+="_"+args[0];
       
        g.setTitle(g.name);
        g.setVisible(true);
        g.toFront();

		/*
		String DATARESUME ;
        float incremento = (float) 0.001;
        float th1 = (float) 0.001;
        float th2 = (float) val;
        int vuelta=1;
        //DecimalFormatSymbols simbolos;
        DecimalFormatSymbols simbol = new DecimalFormatSymbols();
        simbol.setDecimalSeparator('.');
        DecimalFormat formateador = new DecimalFormat("####.######", simbol);
       
        for (int i = 0; i <1; i++) {
            for (int j = 0; j <30; j++) {
                g.bm.setThreshold1(th1);
                g.bm.setThreshold2(th2);
                g.bm.buildMap();
                System.out.printf("i= %d\tj= %d\t Ciclo= %d/30\t th1= %.4f\t th2= %.4f\n", i,j,vuelta, th1, th2);
                th1+=incremento;
               
                        //String.format("%,6f",g.bm.threshold1)   
                float metric = g.bm.map.getMapMetric(g.cm.MaxDistance());
                DATARESUME=formateador.format(g.bm.threshold1)+ ";"
               
                        + formateador.format(g.bm.threshold2)+ ";"
                        +g.bm.cutNode+ ";"+g.bm.map.nodes.size()+ ";"
                        +g.bm.map.edges.size()+ ";"   
                        +g.bm.map.coefA+ ";"   
                        +g.bm.map.coefB+ ";"   
                        +g.bm.map.coefC+ ";"   
                        +g.bm.map.coefD+ ";"   
                        +g.bm.map.coefE+ ";"   
                        +metric+"\n";
                FileMethods.saveFile(DATARESUME, g.name+"_MetricsData", true);
               
                ++vuelta;
                }
            th2+=0.001;
            th1=(float)  0.001;
        }
        //---------------------------Segunda Vuelta--------------------------
       /*
        incremento = (float) 0.001;
        th1 = (float) 0.001;
        th2 = (float) 0.038;
        //vuelta=1;
        //DecimalFormatSymbols simbolos;
               
        for (int i = 0; i <12; i++) {
            for (int j = 0; j <30; j++) {
                g.bm.setThreshold1(th1);
                g.bm.setThreshold2(th2);
                g.bm.buildMap();
                System.out.printf("i= %d\tj= %d\t Ciclo= %d\t th1= %.4f\t th2= %.4f\n", i,j,vuelta, th1, th2);
                th1+=incremento;
               
                        //String.format("%,6f",g.bm.threshold1)   
                float metric = g.bm.map.getMapMetric(g.cm.MaxDistance());
                DATARESUME=formateador.format(g.bm.threshold1)+ ";"
               
                        + formateador.format(g.bm.threshold2)+ ";"
                        +g.bm.cutNode+ ";"+g.bm.map.nodes.size()+ ";"
                        +g.bm.map.edges.size()+ ";"   
                        +g.bm.map.coefA+ ";"   
                        +g.bm.map.coefB+ ";"   
                        +g.bm.map.coefC+ ";"   
                        +g.bm.map.coefD+ ";"   
                        +g.bm.map.coefE+ ";"   
                        +metric+"\n";
                FileMethods.saveFile(DATARESUME, g.name+"_MetricsData", true);
               
                ++vuelta;
                }
            th2+=0.001;
            th1=(float)  0.001;
        }
               
       */
       // g.setVisible(false);
      //  g.dispose();
	
		/*
		System.out.println("Width-X= "+g.cm.getMaxWidth());
		System.out.println("Heigth-Y= "+g.cm.getMaxHeight());
		System.out.println("DMAX= " +g.cm.MaxDistance());
		
		g.dispose();
		
		*/
		
	}

	public void actionPerformed(ActionEvent e) {

		
		if (e.getSource() == th1) {
			threshold1 = Double.parseDouble(th1.getText());
			bm.setThreshold1(threshold1);
			cm.repaint();
			return;
		}
		if (e.getSource() == th2) {
			threshold2 = Double.parseDouble(th2.getText());
			bm.setThreshold2(threshold2);
			cm.repaint();
			return;
		}
		if (e.getSource() == th3) {
			System.out.println(th3.getText());
			bm.setCutNode(Integer.parseInt(th3.getText()));
			if (bm.map != null)
				bm.map.setWeights(Integer.parseInt(th3.getText()));
			cm.repaint();
			return;
		}
		
		if (e.getSource()== thTagTF){
			thTag=Double.parseDouble(thTagTF.getText());
			return;
		}
			
		if (e.getSource() == tagList) {
			
			
			if(tagMode){
			if (((String) tagList.getSelectedItem()).equals("Select Tag")) {
				selectedTag = null;
			} else {
				selectedTag = (String) tagList.getSelectedItem();
				selectTagChanged = true;
				//tagMode =true;
				nodesMode=false;
				cm.repaint();
			}}
			
			
			if(thTagMode){
				if(!thTagTF.getText().isEmpty()){
					thTag = Double.parseDouble(thTagTF.getText());
					selectedTag = (String) tagList.getSelectedItem();
					selectTagChanged = true;
					nodesMode=false;
					cm.repaint();
				}
			}
			
			return;
		}
		//-*----------------------------------------------
		
		
		   if (e.getSource() == originalCB) {
	            original = originalCB.isSelected();
	            cm.repaint();
	            return;
	        }
	        if (e.getSource() == graphCB) {
	            showMap = graphCB.isSelected();
	            cm.repaint();
	            return;
	        }
	        if (e.getSource() == backCB) {
	            background = backCB.isSelected();
	            cm.repaint();
	            return;
	        }
	        
	         if (e.getSource() == showNodesCB) {
	            nodesMode = showNodesCB.isSelected();
	            selectedNode = -1;
	            nodes.setEnabled(true);
	            cm.repaint();
	            return;
	        }
	         
	         if(e.getSource()== highTagsCB){
	             tagMode=highTagsCB.isSelected();
	             //System.out.println("Evento tag");
	        	 if(tagMode){
	        		 //System.out.println("Tag mode en True");
	        		 thTagMode= false;
	        		 thTagsCB.setSelected(false);
	        		 thTagTF.setEnabled(false);
	             if (!((String) tagList.getSelectedItem()).equals("Select Tag")){
	            	 selectTagChanged = true;
	            	 //tagMode=true;
	                 selectedTag=(String) tagList.getSelectedItem();}
	             }
	             cm.repaint();
	        	 return;
	         }
	         
	         if(e.getSource()== thTagsCB){
	             thTagMode=thTagsCB.isSelected();
	             //System.out.println("Evento tag");
	        	 if(thTagMode){
	        		 thTagTF.setEnabled(true);
	        		 tagMode=false;
	        		 highTagsCB.setSelected(false);
	        		 thTag = Double.parseDouble(thTagTF.getText());
	        		 //System.out.println("Tag mode en True");
	             if (!((String) tagList.getSelectedItem()).equals("Select Tag")){
	            	 selectTagChanged = true;
	            	 //tagMode=true;
	                 selectedTag=(String) tagList.getSelectedItem();}
	             }
	        	 else
	        		 thTagTF.setEnabled(false);
	             cm.repaint();
	        	 return;
	         }
	                 
	         
	     	if (e.getSource() == clustersCB) {
	     		zoneGenerated = true;
				cm.repaint();
				return;
			}
	     	
	     	if (e.getSource() == zoneCB) {
				zoneGenerated = zoneCB.isSelected();
				cm.repaint();
				return;
			}
	     	
	     	         
	          if (e.getSource() == jmiGenMap) {
	        	  //original = true;
	        		showMap = true;
	        		//background = true;
	        		mapGenerated = false; 
	        		nodesMode = false; 
	        		selectNodeChanged = false; 
	        		showCluster = false; 
	        		tagMode = false; 
	        		selectTagChanged = false; 
	        		thTagMode=false;
	        		selectedNode = -1 ;
	        		selectedTag = null;      
	        	  
	        	       	  
	        	graphCB.setEnabled(false);
	  			showNodesCB.setEnabled(false);
	  			bm.buildMap();
	  			graphCB.setEnabled(true);
	  			showNodesCB.setEnabled(true);
	  			//highTagsCB.setEnabled(true);
	  			genComboNodes();
	  			genComboTag();
	  			tagList.setEnabled(true);
	  			highTagsCB.setEnabled(true);
	  			highTagsCB.setSelected(true);
	  			thTagsCB.setEnabled(true);
	  			thTagsCB.setSelected(false);
	  			tagMode =true;
	  			mapGenerated = true;
	  			cm.repaint();
	  			Date date = new Date();
	        	DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss");
	        	//  String imgName = name+"_"+bm.threshold1+"_"+bm.threshold2+"_"+hourdateFormat.format(date);
	  			statusLabel.setText("Map for : "+name+" generated at: "+hourdateFormat.format(date));
	  			//cm.showNodeDetails();
	  			
	  			//cm.showMapInfo();
	  			
	  			 jMIGetNodesContent.setEnabled(true);
	  			 jMRelation.setEnabled(true);
	  			 jmiGenZone.setEnabled(true);
	  			jmiGenHierCluster.setEnabled(true);
	  			jmiNodeComposition.setEnabled(true);
	  			 
	            return;
	        }
	          
	          if (e.getSource()==jmiGenAllMaps){
	        	  statusLabel.setText("Generating all sequences maps in process...");
	        	  generate12ImagesMaps();
	        	 statusLabel.setText("Generation of all Sequence Maps Ended.");
	        	  return;
	          }
	          
	          if (e.getSource() == jmiCapture) {
	        	  Date date = new Date();
	        	  DateFormat hourdateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	        	  String imgName = name+"_"+bm.threshold1+"_"+bm.threshold2+"_"+bm.cutNode+"_"+hourdateFormat.format(date);
		  			cm.createImage(imgName);
		  			statusLabel.setText(imgName +" saved.");
		  			return;
		  		}
	          
	          if (e.getSource() == jmiGenCluster) {
	  		    // Kmeans
	  			int k =Integer.parseInt( JOptionPane.showInputDialog("How many clusters?","4"));
	  			km= new Kmeans(k, bm.dimension, bm.imgTags);
	  			km.findMeans();
	  			//mapGenerated = false;
	  			showCluster=true;
	  			clustersCB.setEnabled(true);
	  			clustersCB.setSelected(true);
	  			cm.repaint();
	  			return;
	  		}
	  			  				
	  		if (e.getSource() == jmiGetCluster) {
	  			Kmeans km2;
	  			km2 = new Kmeans(1,bm.dimension, bm.imgTags);
	  			ArrayList<Float> coef = new ArrayList<Float>();
	  			int k=1; 
	  			
	  			if(k==1)
	  				FileMethods.saveFile("K;s2\n", "K_Variances_"+name, false);
	  			
	  			
	  			do {
	  				km2.setK(k);
	  				Float coefValue =km2.findMeansCoef();
	  				coef.add(coefValue); 
	  				FileMethods.saveFile(String.valueOf(k)+";"+String.valueOf(coefValue)+"\n", "K_Variances_"+name, true);
	  				++k;
	  				if ((k%10)==0) System.out.println("K="+k);
	  			}
	  			while(k<=100);
	  			
	  			statusLabel.setText("K Clusters for : "+name+" calculated.");
	  			
	  			return;
	  		}
	  		
            
             //Sequence1 Vidrilo
            if(e.getSource() == jMiHybrid_Sq1){
            	bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_Hybrid/sequence1visual", -0.00000001, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Sequence1.txt",1183, 10, 2000000000, "TrianglesPoints.txt");
	  			name= "VidriloSeq1_HybridAlexNet";
	  			setTitle(name);      
            	cm.repaint();
            	statusLabel.setText("Sequence : "+name+" selected.");
            	seqNumber=1;
            	jmiGenMap.setEnabled(true);
                return;
            }
            
            if(e.getSource() == jMIImgAlex_Sq1){
            	bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_ImageNetAlexNet/sequence1visual", -0.00000001, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Sequence1.txt",1000, 10, 2000000000,"TrianglesPoints.txt");
	  			name= "VidriloSeq1_ImageNetAlexNet";
	  			setTitle(name);      
            	cm.repaint();
            	statusLabel.setText("Sequence : "+name+" selected.");
            	seqNumber=1;
            	jmiGenMap.setEnabled(true);
                return;
            }
            
            if(e.getSource() == jMIImgCaff_Sq1){
            	bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_ImageNetCaffeNet/sequence1visual", -0.00000001, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Sequence1.txt",1000, 10, 2000000000,"TrianglesPoints.txt");
	  			name= "VidriloSeq1_ImageNetCaffeNet";
	  			setTitle(name);      
            	cm.repaint();
            	statusLabel.setText("Sequence : "+name+" selected.");
            	seqNumber=1;
            	jmiGenMap.setEnabled(true);
                return;
            }
            
            if(e.getSource() == jMIImgGNet_Sq1){
            	bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_ImageNetGoogLeNet/sequence1visual", -0.00000001, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Sequence1.txt",1000, 10, 2000000000,"TrianglesPoints.txt");
	  			name= "VidriloSeq1_ImageNetGoogLeNet";
	  			setTitle(name);      
            	cm.repaint();
            	statusLabel.setText("Sequence : "+name+" selected.");
            	jmiGenMap.setEnabled(true);
                return;
            }
            
            if(e.getSource() == jMIImgVGG_Sq1){
            	bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_ImageNetVGG/sequence1visual", -0.00000001, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Sequence1.txt",1000, 10, 2000000000,"TrianglesPoints.txt");
	  			name= "VidriloSeq1_ImageNetVGG";
	  			setTitle(name);      
            	cm.repaint();
            	statusLabel.setText("Sequence : "+name+" selected.");
            	seqNumber=1;
            	jmiGenMap.setEnabled(true);
                return;
            }
            
            
            if(e.getSource() == jMIPlaAlex_Sq1){
            	bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_PlacesAlexNet/sequence1visual", -0.00000001, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Sequence1.txt",205, 10, 2000000000,"TrianglesPoints.txt");
	  			name= "VidriloSeq1_PlacesAlexNet";
	  			setTitle(name);      
            	cm.repaint();
            	statusLabel.setText("Sequence : "+name+" selected.");
            	seqNumber=1;
            	jmiGenMap.setEnabled(true);
                return;
            }
            
            if(e.getSource() == jMIPlaGNet_Sq1){
            	bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_PlacesGoogLeNet/sequence1visual", -0.00000001, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Sequence1.txt",205, 10, 2000000000,"TrianglesPoints.txt");
	  			name= "VidriloSeq1_PlacesGoogLeNet";
	  			setTitle(name);      
            	cm.repaint();
            	statusLabel.setText("Sequence : "+name+" selected.");
            	seqNumber=1;
            	jmiGenMap.setEnabled(true);
                return;
            }
            
            if(e.getSource() == jMIImgMX_Sq1){
            	bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_ImageNetMXNet/sequence1visual", -0.00000001, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Sequence1.txt",1000, 10, 2000000000,"TrianglesPoints.txt");
	  			name= "VidriloSeq1_ImageNetMXNet";
	  			setTitle(name);      
            	cm.repaint();
            	statusLabel.setText("Sequence : "+name+" selected.");
            	seqNumber=1;
            	jmiGenMap.setEnabled(true);
                return;
            }
            
            
            if(e.getSource() == jMIImgMX21K_Sq1){
            	bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_ImageNetMXNet21K/sequence1visual", 0.0000000, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Sequence1.txt",21841, 10, 2000000000,"TrianglesPoints.txt");
	  			name= "VidriloSeq1_ImageNetMXNet21K";
	  			setTitle(name);      
            	cm.repaint();
            	statusLabel.setText("Sequence : "+name+" selected.");
            	seqNumber=1;
            	jmiGenMap.setEnabled(true);
                return;
            }
            
            
            //----------------------------------Sequence 2
            
            if(e.getSource() == jMIPlaAlex_Sq2){
            	bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence2/Vidrilo_Sequence2_PlacesAlexNet/sequence2visual", -0.00000001, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence2/Sequence2.txt",205, 10, 2000000000,"TrianglesPoints.txt");
	  			name= "VidriloSeq2_PlacesAlexNet";
	  			setTitle(name);      
            	cm.repaint();
            	statusLabel.setText("Sequence : "+name+" selected.");
            	seqNumber=2;
            	jmiGenMap.setEnabled(true);
                return;
            }
            
            
            
            
            ///Para generacion de relacion entre modelos 
            if(e.getSource() == jMIImgAlex_Sq6){
            	//bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_ImageNetAlexNet/sequence1visual", -0.00000001, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Sequence1.txt",1000, 10, 2000000000);
            	cm.showNodeRelation("/home/jcarlos2289/Documentos/VidriloTags/Sequence"+String.valueOf(seqNumber)+"/Vidrilo_Sequence"+String.valueOf(seqNumber)+"_ImageNetAlexNet/");
            	return;
            }
            
            if(e.getSource() == jMIImgCaff_Sq6){
            	//bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_ImageNetCaffeNet/sequence1visual", -0.00000001, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Sequence1.txt",1000, 10, 2000000000);
            	cm.showNodeRelation("/home/jcarlos2289/Documentos/VidriloTags/Sequence"+String.valueOf(seqNumber)+"/Vidrilo_Sequence"+String.valueOf(seqNumber)+"_ImageNetCaffeNet/");
            	return;
            }
            
            if(e.getSource() == jMIImgGNet_Sq6){
            	//bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_ImageNetGoogLeNet/sequence1visual", -0.00000001, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Sequence1.txt",1000, 10, 2000000000);
            	cm.showNodeRelation("/home/jcarlos2289/Documentos/VidriloTags/Sequence"+String.valueOf(seqNumber)+"/Vidrilo_Sequence"+String.valueOf(seqNumber)+"_ImageNetGoogLeNet/");
            	return;
            }
            
            if(e.getSource() == jMIImgVGG_Sq6){
            	//bm.readTags("/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_ImageNetVGG/sequence1visual", -0.00000001, 2389, "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Sequence1.txt",1000, 10, 2000000000);
            	cm.showNodeRelation("/home/jcarlos2289/Documentos/VidriloTags/Sequence"+String.valueOf(seqNumber)+"/Vidrilo_Sequence"+String.valueOf(seqNumber)+"_ImageNetVGG/");
            	return;
            }
            
            if(e.getSource() == jMIImgMX_Sq6){
            	cm.showNodeRelation("/home/jcarlos2289/Documentos/VidriloTags/Sequence"+String.valueOf(seqNumber)+"/Vidrilo_Sequence"+String.valueOf(seqNumber)+"_ImageNetMXNet/");
            	return;
            }
            
            if(e.getSource() == jMIImgMX21K_Sq6){
            	cm.showNodeRelation("/home/jcarlos2289/Documentos/VidriloTags/Sequence"+String.valueOf(seqNumber)+"/Vidrilo_Sequence"+String.valueOf(seqNumber)+"_ImageNetMXNet21K/");
            	return;
            }
            
            //--------------------------Nodes Content
            
            if(e.getSource()== jMIGetNodesContent){
            	int o = 1;
            	for (Node nd : bm.map.nodes) {
            		FileMethods.saveFile(nd.getNodesContentRows(), "Node_" + String.valueOf(o), false);
            		FileMethods.saveFile(nd.getNodeMeanData(), "NodeMean_" + String.valueOf(o), false);
            		//FileMethods.saveFile(nd.getNodeMeanData(), "NodeMean", true);
					o++;
				}
            }
            
            if(e.getSource()== jmiGenZone){
            	cm.showZoneInfomation();
            	//bm.map.generateZones();
            	zoneGenerated = true;
            	zoneCB.setSelected(true);
            	zoneCB.setEnabled(true);
            	statusLabel.setText("Zones have been generated.");
            	cm.repaint();
            }
            
            if(e.getSource()==jmiGenHierCluster){
            	cm.showHierCluster();
            }
            
            
            if(e.getSource()==jmiNodeComposition){
            	cm.showNodeComposition();
            	//cm.showNodeDetails(); //top10
            }
            
		
	}

	private void generate12ImagesMaps() {
		// TODO Auto-generated method stub
		
		    Date date = new Date();
	        DateFormat hourdateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
	        String imgName;
	        float thr1 = (float) 0.001;
	        float thr2 = (float) 0.015;
	        int cn =1;
	        
	        thr1 = Float.parseFloat(th1.getText());
	        thr2 = Float.parseFloat(th2.getText());
	        cn = Integer.parseInt(th3.getText());
	       
	        bm.setThreshold1(thr1);
	        bm.setThreshold2(thr2);
	        bm.setCutNode(cn);
	        DecimalFormatSymbols simbol = new DecimalFormatSymbols();
	        simbol.setDecimalSeparator('.');
	        DecimalFormat formateador = new DecimalFormat("####.####",simbol);
	       
	        //Solo lo deje como ejemplo

	        //Cloudy
	        //1
	        //bm.readTags(dataSetPath + "KTH_IDOL/KTH_Minnie/min_cloudy1/min_cloudy1_HybridAlexNet/IDOL_MINNIE_Cl1_", -0.00000001, 915, dataSetPath + "KTH_IDOL/KTH_Minnie/min_cloudy1/IDOL_MINNIE_Cl1.txt",1183, 5, 2000000000);
	        name= "MinnieCloudy1_HybridAlexNet";
	        bm.buildMap();
	        mapGenerated = true;
	        original=false;
	        cm.repaint();
	        date = new Date();
	        imgName = name+"_"+formateador.format(bm.threshold1)+"_"+formateador.format(bm.threshold2)+"_"+hourdateFormat.format(date);
	        cm.createImage(imgName);
		
		
	}

}
