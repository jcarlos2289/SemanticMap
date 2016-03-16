package buildMap;

import java.awt.Color;
import java.awt.Point;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.apporiented.algorithm.clustering.Cluster;
import com.apporiented.algorithm.clustering.ClusteringAlgorithm;
import com.apporiented.algorithm.clustering.CompleteLinkageStrategy;
import com.apporiented.algorithm.clustering.DefaultClusteringAlgorithm;
import com.apporiented.algorithm.clustering.visualization.DendrogramPanel;

import java.util.Map.Entry;

import javax.swing.JScrollPane;




public class Map {
	ArrayList<Node> nodes;
	ArrayList<Edge> edges;
	ArrayList<Zone> zones;
	ArrayList<String> catID;
	ArrayList<String> nodesByCat;
	ArrayList<String> catTag;
	HashMap<String,String> catIdentifiedTags2;
	double[][] distanceMatrix =null;
	
	boolean useHisto=true;
	float[] weights;
	float coefA, coefB, coefC, coefD, coefE;
	
	public Map () {
		nodes=new ArrayList<Node>();
		edges=new ArrayList<Edge>();
		zones=new ArrayList<Zone>();
		catIdentifiedTags2 = new HashMap<String,String>();
		
		catID = new ArrayList<String>();
		nodesByCat  = new ArrayList<String>();
		catTag  = new ArrayList<String>();
		
		coefA=-1;
		coefB=-1;
		coefC=-1;
		coefD=-1;
		coefE=-1;
	}
	
	public void setWeights (int size) {
		weights= new float[size];
		for (int i=0; i< size; i++) {
			//weights[i]=(i/(float) size);
			//System.err.println(weights[i]);
			weights[i]=1;
		}
	}
	
	public Node createNode (ImageTags i) {
		Node n=new Node(useHisto, weights);
		n.add(i);
		nodes.add(n);
		return n;
	}
	
	
	public Zone createZone(Node n){
		Zone z = new Zone(n);
		zones.add(z);
		
		return z;
	}
	
	public Node createNode (String name){
		Node n=new Node(useHisto, weights);
		n.nodeName = name;
		nodes.add(n);
		return n;
	}
	
	public Node createNode (String name, double[]coords){
		Node n=new Node(useHisto, weights);
		n.nodeName = name;
		n.limits = new AreaCoords(coords);
		nodes.add(n);
		return n;
	}
	
	public Node getNodeByName(String name){
		Node y = new Node(true, weights);
		for(Node x: nodes){
			if(x.nodeName.equals(name)){
				y = x;
				break;
			}
				
		}
			
		return y;
	}
	
	public void printMap () {
		boolean [][]adj;
		adj=new boolean[nodes.size()][nodes.size()];
		
		for (int i=0; i<nodes.size(); i++) {
			nodes.get(i).printCoords();
		}
		for (int i=0; i<edges.size(); i++) 
			adj[nodes.indexOf(edges.get(i).getA())][nodes.indexOf(edges.get(i).getB())]=true;

		FileWriter fichero = null;
        PrintWriter pw = null;
        
        try {
        	fichero = new FileWriter("adj");
        	pw = new PrintWriter(fichero);

    		for (int i=0; i<nodes.size(); i++) {
    			for (int j=0; j<nodes.size(); j++) 
    				if (adj[i][j]) pw.print("1 ");
    				else pw.print("0 ");
    			pw.println("");
    		}
    	} catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
        		if (fichero != null)
        			fichero.close();
        	} catch (Exception e2) {
        		e2.printStackTrace();
        	}
        }
	}
	
	public int getMapSize () {
		return nodes.size();
	}
	
	public void addNode (Node n) {
		nodes.add(n);
	}
	
	public Node getNode (int i) {
		if (i>nodes.size()) 
			return null;
		return nodes.get(i);
	}
	
	
	public Zone getZone (int i) {
		if (i>zones.size()) 
			return null;
		return zones.get(i);
	}
	
	public void createEdge (Node a, Node b) {
		for (int i=0; i<edges.size(); i++) {
			if ((edges.get(i).a==a && edges.get(i).b==b) ||
				(edges.get(i).a==b && edges.get(i).b==a))
				return;
		}
		Edge e = new Edge(a, b);
		edges.add(e);
	}
	
	public float getMapDev_E(){
		float E=0;
		
		
		float xAcum=0, yAcum=0, xDev, yDev;
		for (Node node : nodes) {
			
			xAcum+=node.representative.xcoord;
			yAcum+=node.representative.ycoord;
		}
			
	
		xAcum =0;
		yAcum = 0;
		
		for(Node node:nodes){
			xAcum +=node.representative.xcoord/nodes.size();
			yAcum  +=node.representative.ycoord/nodes.size();
		}
		
		xDev= (float) Math.sqrt(xAcum);
		yDev= (float) Math.sqrt(yAcum);
		E =  (float) Math.sqrt(Math.pow(xDev, 2)+ Math.pow(yDev, 2));
				
		return E;
	}
	
	
	public float getAvgEdgeDist_C(){
		float C=0;
		float distAcum=0;
		
		for (Edge e : edges) {
			distAcum+= e.getDistance()/edges.size();
			C= distAcum;
			}
				
		return C;
	}
	
	
	
	public ArrayList<NodeCoef> getNodeCoeficientsByNode(float dmax){
		ArrayList<NodeCoef> NodeMetricCoeficients = new ArrayList<NodeCoef>();
		
		for (Node node : nodes) {
			NodeMetricCoeficients.add(new NodeCoef(node.getQCat_A(), node.getQConnAvg_B(edges), node.getQImgDev_D()));
			}
		
		return NodeMetricCoeficients;
	}
	
	public NodeCoef AvgCoeficients(ArrayList<NodeCoef> NodeMetricCoeficients, float dmax){
		NodeCoef AvgCoef = new NodeCoef(0,0,0);
		
		float acumA = 0, acumB =0, acumD=0;
		//NodeCoef normValue;
		for (NodeCoef nodeCoef : NodeMetricCoeficients) {
			//normValue = normalize(nodeCoef, dmax);
			acumA += nodeCoef.getA()/nodes.size();
			acumB += nodeCoef.getB()/nodes.size();
			acumD += nodeCoef.getD()/nodes.size();
		}
		//Averaged Coeficients in the nodes of the map
		AvgCoef = new NodeCoef(acumA, acumB, acumD);
		
		return AvgCoef;
	}
	
		
	public NodeCoef normalize(NodeCoef nodeCoef, float dmax){
		NodeCoef dat = new NodeCoef(0, 0, 0);
		//gets the avg values for the noeficients by node to normalize 		
		dat.setA((1-nodeCoef.getA()));
		dat.setB(   (nodes.size()-1-nodeCoef.getB())/(nodes.size()-2)    );
		dat.setD(	(dmax/2 - nodeCoef.getD()) /(dmax/2) );
				
		return dat;
	}
	
	
	public String printMetricTable(float dmax){
		String table ="<html><h2>Metrics Coeficients</h2><br> "
				+ "<table border=\"1\"   style=\"font-size:10px\" >"
				+ "<tr><th>Node</th><th>A</th><th>B</th><th>D</th></tr>";
				//+ "<tr><th>Node</th><th>A</th><th>B</th><th>D</th><th>A<sub>Norm</sub></th><th>B<sub>Norm</sub></th><th>D<sub>Norm</sub></th></tr>";
		
		//Get the coeficients by node in an arraylist
		ArrayList<NodeCoef> NodeMetricCoeficients = getNodeCoeficientsByNode(dmax);
		//ArrayList<NodeCoef> NodeMetricCoeficientsNorm = DetailNomrCoeficientsByNode(NodeMetricCoeficients,dmax);
		
		//Get the AVG Coeficients
		NodeCoef AvgCoef = AvgCoeficients(NodeMetricCoeficients, dmax);
		//Get the AVG Coeficients Normalized
		NodeCoef AvgCoefNorm = normalize(AvgCoef, dmax);
		
		int i =0;
		for (Node node : nodes) {
			table += "<tr>";
			table += node.printNodeMet(i, edges);
			//table += "<td>"+(NodeMetricCoeficientsNorm.get(i).getA()) +"</td><td>"+NodeMetricCoeficientsNorm.get(i).getB() +"</td><td>"+NodeMetricCoeficientsNorm.get(i).getD() +"</td>";
			table += "</tr>";
			++i;
		}
		
		table+="</table><br>";
		
				
		
		float E =0, C =0, En=0, Cn=0;
		
		E = getMapDev_E();
		C = getAvgEdgeDist_C();
		En = E/(dmax/2);
		Cn = (dmax-C)/dmax;
			
		table +="<h2>Dmax=  "+dmax+"</h2>";
		table +="<h2>Nodes=  "+nodes.size()+"</h2>";
		table +="<h2>Edges=  "+edges.size()+"</h2>";
		table +="<h2>A= "+ AvgCoef.getA()+"  A<sub>Norm</sub>=  "+AvgCoefNorm.getA()+"</h2>";
		table +="<h2>B= "+ AvgCoef.getB()+"  B<sub>Norm</sub>=  "+AvgCoefNorm.getB()+"</h2>";
		table +="<h2>D= "+ AvgCoef.getD()+"  D<sub>Norm</sub>=  "+AvgCoefNorm.getD()+"</h2>";
		table +="<h2>C= "+ C+ "  C<sub>Norm</sub>=  "+Cn+"</h2>";
		table +="<h2>E= "+ E+ "  E<sub>Norm</sub>=  "+En+"</h2>";
			
				
		
		float metric = getMapMetric(dmax);
			
		table +="<h1>Metric=  "+metric+"</h1><br>";
		table+= "</html>";
		return table;
				
		
	}
	
	public float getMapMetric(float dmax){
		float metric=0;
		//Get the coeficients by node in an arraylist
		ArrayList<NodeCoef> NodeMetricCoeficients = getNodeCoeficientsByNode(dmax);
		
		//Get the AVG Coeficients
		NodeCoef AvgCoef = AvgCoeficients(NodeMetricCoeficients, dmax);
			
		
		float E=0, C=0;
	
		float wA=20, wB=20, wC=20, wD=20, wE =20;
		
		//Normalized Values 
		NodeCoef AvgCoefNorm = normalize(AvgCoef, dmax);	
		E = getMapDev_E()/(dmax/2);
		C = (dmax-getAvgEdgeDist_C())/dmax;
	
		metric = AvgCoefNorm.getA()*wA 
				+AvgCoefNorm.getB()*wB
				+AvgCoefNorm.getD()*wD
				+C*wC
				+E*wE;
		
		
		coefA=AvgCoefNorm.getA();
		coefB=AvgCoefNorm.getB();
		coefC=C;
		coefD=AvgCoefNorm.getD();
		coefE=E;
				
		return metric;
	}
	
	public String getMapInfo(String name, String th1, String th2, String cutNode){
		
				ArrayList<Integer> classCount = new ArrayList<Integer>();
				String cats ="Node Categories\n";
				String text = "<html>\n";
				text +="<h1> Sequence: "+ name+"</h1><br>";
				text += "<p>Th1 = "+ th1 + "</p><p>Th2 = " +th2 + "</p><p>CN = "+ cutNode+"</p><p> Nodes: "+this.getMapSize()+"</p><br>";
							
				text += "<table border=\"1\"   style=\"font-size:8px\"    >";
				text += "<tr><th>Node</th>";
				for(int i = 0;i < 10; ++i )
					text+="<th>Tag "+ String.valueOf(i+1)+ "</th>";
				text +="</tr>\n";
				
				
				int h = 0;
				for (Iterator<Node> iterator = nodes.iterator(); iterator.hasNext();) {
					
					Node node =  iterator.next();
					classCount.add(node.getCategoryAmount());
					ArrayList<String> nodetags = node.getTop10Nodes();
					text +="<tr>";
					text +="<td>" + ++h +"</td>";
					for (String tag : nodetags) {
						text +="<td>"+tag +"</td>";
					}
					text +="</tr>\n";
					
					cats+="<br>Node "+(h-1)+"\n";
					cats += node.countCategory();
					cats+= "\n\n\n";
					
				
					
				}
				
				Collections.sort(classCount);
				String catPercentage="<h2>Categories Count Percentage</h2><br><p>Cat_Amout &nbsp;&nbsp; %</p><br>";
				Set<Integer> data = new HashSet<>(classCount);
		    
				for(int dat: data){
					int  val = Collections.frequency(classCount, dat);
					catPercentage+="<p>"+dat+"&nbsp;&nbsp;"+(((float)val)*100/nodes.size())+"%</p>";
				}
				
				catPercentage+="<br>";
						
				text += "</table>";
				text +="\n\n\n<br> <p>" +catPercentage+"</p><br>";
				text +="\n\n\n<br> <p>" +cats+"</p>";
				text += "\n</html>";
		
		
		return text;
		
	}
	public String getNodeTagsRelation (String name, String modelPath){
		// Incluir el Nombre de los Modelos que uso en  los tituos de las tablas
		
		String [] ram = modelPath.split("/");
		String text = "<html>\n";
		text +="<h1> Sequence: "+ name+"</h1><br>";
		text +="<h1> Relation with Sequence: "+ ram[ram.length-1]+"</h1><br>";
		text += "<p> Nodes: "+this.getMapSize()+"</p><br>";
		text += "<table border=\"1\"   style=\"font-size:8px\"    >";
		text += "<tr><th> </th> <th colspan=\"3\">Top 3 Places Model </th><th>æææææ</th> <th colspan=\"5\">Top 5 ImageNet Model</th> </tr> "	;
		text += "<tr><th>Node</th>";
		
		for(int i = 0;i < 3; ++i )
			text+="<th>Tag "+ String.valueOf(i+1)+ "</th>";
		text+="<th>æææææ</th>";
		for(int i = 0;i < 5; ++i )
			text+="<th>Tag "+ String.valueOf(i+1)+ "</th>";
		text +="</tr>\n";
		
		
		int h = 0;
		//String modelo2Path = "/home/jcarlos2289/Documentos/VidriloTags/Sequence1/Vidrilo_Sequence1_ImageNetAlexNet/";
		 String modelo2Path = modelPath;
		int topModelo2 = 5;
		ArrayList<Node> virtualMap = new ArrayList<Node>();
		
		for (Iterator<Node> iterator = nodes.iterator(); iterator.hasNext();) {
			
			Node node =  iterator.next();
			
			ArrayList<String> imageTop3Tag = node.getTopXNodes(3);

			text +="<tr>";
			text +="<td>" + ++h +"</td>";
			for (String tag : imageTop3Tag) {
				text +="<td>"+tag +"</td>";
			}
			text+="<td>ææ"+ h +"æææ</td>";
			//-----------------------------------------
			ArrayList<String> imageTop10 = new ArrayList<String>();
			
			
			for (ImageTags img : node.images ) {
				imageTop10.add(img.imageName);
			}
			
			
			float [] we = new float[weights.length];
			for (int i = 0; i < we.length; i++) {
				we[i] =1;
			}
			Node AuxNode = new Node(useHisto, we);
			List<String> topsTags = new ArrayList<>();
			ArrayList<String> top5Modelo2 = new ArrayList<String>();
			if(!imageTop10.isEmpty()){
			
				for (String lin : imageTop10) {
					//System.out.println(lin);
					
			        int lastIndex = 0;

			        lastIndex = lin.lastIndexOf("/");
			        lastIndex++; // Si es -1 se vuelve 0 y es un valor numerico le aumento 1 para que no se incluya en la cadena nueva

			        String seqName = lin.substring(lastIndex, lin.length());
			       // System.out.println(seqName);
			     
			        List<String> imgtagList = new ArrayList<>();
			        imgtagList =FileMethods.processFile(modelo2Path+seqName);
			        
			        ImageTags imgData = new ImageTags(seqName);
			        for (String line : imgtagList) {
						imgData.addTag(line);
						}
			         AuxNode.add(imgData);    
			      	topsTags.addAll(ListMethods.getTopXNodes(topModelo2, imgData.tags));
			      	
			        }
				}
			
			top5Modelo2 = AuxNode.getTopXNodes(5);
			virtualMap.add(AuxNode);
			//-------------------------------------------
			for (String tag : top5Modelo2) {
				text +="<td>"+tag +"</td>";
			}
			
			
			text +="</tr>\n";
			}//End of the for of the Nodes
			
		text += "</table>";
		text += "\n</html>\n\n";

		
		return text;
	}
	
	public String nodeComposition(String name){
		String text="";
		//node.images.get(0).imageName;
		// Incluir el Nombre de los Modelos que uso en  los tituos de las tablas
		
	    text = "<html>\n";
		text +="<h1> Sequence: "+ name+"</h1><br>";
		
		
		
		text += "<p> Nodes: "+this.getMapSize()+"</p><br>";
					
		text += "<table border=\"1\"   style=\"font-size:12px\"    >";
			
		
		text += "<tr><th> </th> <th colspan=\"3\">Top 3 Places Model </th>  </tr> "	;
		
		text += "<tr><th>Node</th>";
		for(int i = 0;i < 3; ++i )
			text+="<th>Tag "+ String.valueOf(i+1)+ "</th>";
		
		text +="</tr>\n";
			
		for (Iterator<Node> iterator = nodes.iterator(); iterator.hasNext();) {
			
			Node node =  iterator.next();
			
			ArrayList<String> imageTop3Tag = node.getTopXNodes(3);

			text +="<tr>";
			text +="<td>" + node.nodeName +"</td>";
			for (String tag : imageTop3Tag) {
				text +="<td>"+tag +"</td>";
			}
			
			
			text +="</tr>\n";
			}//End of the for of the Nodes
			
		text += "</table>";
		text += "\n</html>\n\n";
		return text;
		
	}
	public void generateDistanceMatrix(){
		
		distanceMatrix = new double[nodes.size()][nodes.size()];
		for (int i = 0; i < nodes.size(); i++) {
			for (int j = 0; j < nodes.size(); j++) {
				distanceMatrix[i][j] = -1.0f;
			}
		}
		
		for (int i = 0; i < nodes.size(); i++) {
			distanceMatrix[i][i]=0;
		}
		
				
		int n1=0;
		//Para Usar los Nodos del Mapa que se esta vizualizando
		for (Node fs : nodes) {
		
	
				for(int n2 = n1+1; n2 <nodes.size();n2++){
			
					distanceMatrix[n1][n2] = distanceOfNodes_KS(fs, nodes.get(n2));
					distanceMatrix[n2][n1] = distanceMatrix[n1][n2];
				}
	
			n1++;
		}
		
	}
	
	
	public JScrollPane getHierCluster(){
		Cluster cluster = generateHierCluster();
		
		
		DendrogramPanel dp = new DendrogramPanel();
		dp.setBackground(new Color(255,255,255));
		dp.setSize(500, 3000);
		dp.setModel(cluster);
		JScrollPane pane = new JScrollPane(dp);
		
		
		return pane;
		
		
	}
	
	public Cluster generateHierCluster(){
		//JScrollPane pane = new JScrollPane();
		
		//matrix de distancias
	//arreglo con los nombres
		
		String[] names = new String[nodes.size()];
		for (int i = 0; i < nodes.size(); i++) {
			names[i] = nodes.get(i).nodeName;//"Node_" +String.valueOf(i+1);
		}
			
		if(distanceMatrix==null)
			generateDistanceMatrix();
		
		ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
		Cluster cluster = alg.performClustering(distanceMatrix, names, new CompleteLinkageStrategy());  //SingleLinkageStrategy() CompleteLinkageStrategy()
				
		//cluster.toConsole(1);
		//System.out.println(printCluster(cluster));
		return cluster;
	}
	
	
	public String printCluster(Cluster cs){
		String result="";
		if(cs.countLeafs()>2){
			for(Cluster c : cs.getChildren()){
				result += printCluster(c);
				//if(c.isLeaf())
					//result+="\n-------------";
					}
		}
		else{
			result += "\n|"+cs.getName()+"| ";//Dist: "+ String.valueOf(cs.getDistance())+"\n";
			if(cs.getDistance()!=null)
				if(cs.getDistance()> 0.1 && cs.getDistance()<0.18)//if(!cs.isLeaf())
				result+="\n-------------";
		}
		return result;
	}
	
	
	public ArrayList<String> getClusterArray(Cluster cs){
		ArrayList<String> result= new ArrayList<String>();
		if(cs.countLeafs()>2){
			for(Cluster c : cs.getChildren()){
				result.addAll(getClusterArray(c));
				//if(c.isLeaf())
					//result+="\n--55-----------";
					}
		}
		else{
			result.add(cs.getName());//Dist: "+ String.valueOf(cs.getDistance())+"\n";
			if(!cs.isLeaf())//if(cs.getDistance()!=null)
				//if(cs.getDistance()> 0.12 && cs.getDistance()<0.15)//if(!cs.isLeaf())
				result.add("#");
			}
		return result;
	}
	
	
		
	public float distanceOfNodes_KS(Node node1, Node node2){

			Set<String> hs1;
			Iterator<String> iterator;
				
			hs1 = node1.histoMean.keySet();
			iterator = hs1.iterator();
	
			
			ArrayList<String> tagList = new ArrayList<String>();
			while(iterator.hasNext()){
				tagList.add(iterator.next());
			}
			
			Collections.sort(tagList);
			
			float[]cumulatedN1 = new float[tagList.size()];
			float[]cumulatedN2 = new float[tagList.size()];
			float[]distanceKS = new float[tagList.size()];
			
			cumulatedN1[0] = node1.histoMean.get(tagList.get(0));
			cumulatedN2[0] = node2.histoMean.get(tagList.get(0));
			distanceKS[0] = Math.abs(cumulatedN2[0]-cumulatedN1[0]);
			
			
			for (int i = 1; i < tagList.size(); i++) {
				cumulatedN1[i] = cumulatedN1[i-1]+ node1.histoMean.get(tagList.get(i));
				cumulatedN2[i] =  cumulatedN2[i-1]+ node2.histoMean.get(tagList.get(i));
				distanceKS[i] = Math.abs(cumulatedN2[i]-cumulatedN1[i]);
			}
			
	    	ArrayList<Float> tagDmax = new ArrayList<Float>();
	    	for(float s: distanceKS)
	    		tagDmax.add(s);
			
			Collections.sort(tagDmax);
			
			
			
			return (float) tagDmax.get(tagDmax.size()-1);
			
					
		}
		
	public String generateZones(){
		
		
		Cluster cluster = generateHierCluster();
		
		ArrayList<String> ol = new ArrayList<String>();
		ol = getClusterArray(cluster); // gets an array list with the leafs of cluster separing cluster by #
		
		nodesByCat.clear();
		catID.clear();
		
		int catCount = 1;
		
		for(String hj : ol){
			if(hj.contains("&")){
				String [] ram = hj.split("&");
				nodesByCat.add(ram[0]);
				nodesByCat.add(ram[1]);
				catID.add(String.valueOf(catCount));
				catID.add(String.valueOf(catCount));
				
			}else if (hj.equals("#")){
				catCount++;
				continue;
			}else{
				nodesByCat.add(hj);
				catID.add(String.valueOf(catCount));
			}
			//System.out.println(hj);
		}

		
		ArrayList<ArrayList<String>> auxFortags = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<Float>> auxFortagsValue = new ArrayList<ArrayList<Float>>();
		
		for (int i = 0; i < nodesByCat.size(); i++) {
				auxFortags.add(this.getNodeByName(nodesByCat.get(i)).getTopXNodes(3));
				auxFortagsValue.add(this.getNodeByName(nodesByCat.get(i)).getTopXNodesValues(3));
		}

	
		
		ArrayList<ArrayList<String>> auxFortagsLite = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<Float>> auxFortagsValueLite = new ArrayList<ArrayList<Float>>();
		
		
		
		String ramCat = catID.get(0);
		ArrayList<String> auxList = new ArrayList<String>();
		ArrayList<Float> auxListValues = new ArrayList<Float>();
		
		for (int i = 0; i < catID.size(); i++) {
			if(ramCat.equals(catID.get(i)))
			{
				auxList.addAll(auxFortags.get(i));
				auxListValues.addAll(auxFortagsValue.get(i));
			}else
			{
				auxFortagsLite.add(auxList);//añade el del anterior
				auxFortagsValueLite.add(auxListValues);
				auxList = new ArrayList<String>();
				auxListValues = new ArrayList<Float>();
				ramCat= catID.get(i);
				auxList.addAll(auxFortags.get(i));
				auxListValues.addAll(auxFortagsValue.get(i));
			}
			
			
			if(i == catID.size()-1){
				auxFortagsLite.add(auxList); //añade el ultimo
				auxFortagsValueLite.add(auxListValues); //añade el ultimo
			}
	    }
			
		ArrayList<String> resumeCats = new ArrayList<String>();
		resumeCats.add(catID.get(0));
		for (int i = 1; i < catID.size(); i++) {
			if(!resumeCats.contains(catID.get(i)))
			    resumeCats.add(catID.get(i));
			
		}
		
		//HashMap<String,String> catIdentifiedTags = new HashMap<String,String>();
		
		catIdentifiedTags2.clear();
		 
		for (int i = 0; i < auxFortagsLite.size(); i++) {
			ConcurrentHashMap<String, Float> probTopTags = new ConcurrentHashMap<String, Float>();
			for (int j = 0; j < auxFortagsLite.get(i).size(); j++) {
				
				if(probTopTags.containsKey(auxFortagsLite.get(i).get(j)))
					probTopTags.replace(auxFortagsLite.get(i).get(j), probTopTags.get(auxFortagsLite.get(i).get(j))+ auxFortagsValueLite.get(i).get(j));
					else
				     probTopTags.put(auxFortagsLite.get(i).get(j), auxFortagsValueLite.get(i).get(j));
				}
			ArrayList<String> maxTag = new ArrayList<>();
	        maxTag = ListMethods.getTopXNodes(1, probTopTags);
	        
	       /* if(catIdentifiedTags2.containsValue(maxTag.get(0))){
	        	catIdentifiedTags2.put(resumeCats.get(i), maxTag.get(0)+"_"+String.valueOf(i));  // diferencio los corredores identificados cada uno con numero diferente al final
	        }
	        else*/
	        	 catIdentifiedTags2.put(resumeCats.get(i), maxTag.get(0));
			
		}
		
		
				
			zones.clear();
			Zone auxZone = new Zone("AUX");
			//int u = 2;
			
			for (Entry<String, String> entry : catIdentifiedTags2.entrySet()) {
				auxZone = new Zone(entry.getValue());
				 for (int i = 0; i < catID.size(); i++) {
					if(catID.get(i).equals(entry.getKey())){
						auxZone.addNode(this.getNodeByName(nodesByCat.get(i)));
					}
				}
				zones.add(auxZone);
			}
		//Nuevo Metodo de Generacion de Zonas basado en similitud evaluada en cada nodo.	
			
			
			
			//Zone g = zones.add(currentZone);
					
			zones.clear();
			
			
			auxZone = new Zone("aa");
			Zone auxZone2 = new Zone("aux");
			Zone currentZone = createZone(nodes.get(0));
			double minDist = Double.MAX_VALUE; 
			double dist = 0;
			//boolean foundZone = false;
			
			
			for(int i = 1; i< nodes.size(); i++){
				auxZone2=null;
				minDist = Double.MAX_VALUE;
				for (int z= 0; z <zones.size();z++){
					auxZone = getZone(z);
					if (auxZone != currentZone){
						dist = distanceOfNodes_KS(nodes.get(i), auxZone.representative);
					    if(dist<minDist){
					    	minDist=dist;
					    	auxZone2 = auxZone;
					    }
					}
				}
				dist = distanceOfNodes_KS(nodes.get(i), currentZone.representative);
				
				if(dist<0.5){ //th2
					currentZone.addNode(nodes.get(i));
				}else
				{
					if(minDist<dist && minDist < 0.09){ //th1
						//foundZone = true;
						currentZone= auxZone2;
						currentZone.addNode(nodes.get(i));
					}else{
						auxZone = currentZone;
						currentZone = createZone(nodes.get(i));
					}
				}
			}	
				
				
			
			//Si no es similar se pasara a otra zona
			// pero se debe buscar siempre si dentro 
			// de las existentes se puede agregar
			//
			
			
			ArrayList<String> zNames = new ArrayList<String>();
			for(int i = 0; i< zones.size();i++){
				String ram =  zones.get(i).name;
				zNames.add(ram);
				zones.get(i).name = ram+"_"+String.valueOf(Collections.frequency(zNames, ram));
			}
			
			
			String text = "<html>\n";
			text +="<h1> Identified zones in the map</h1><br>";
						
			text += "<table border=\"1\"   style=\"font-size:12px\"    >";
			text +="<tr><th>Zone</th><th>Category</th><th>#Nodes</th><th>Nodes</th></tr>\n";
			int p = 1;
			for(Zone z : zones){
				text +="<tr>";
				text +="<td>";
				text+= String.valueOf(p) + "</td> <td>";
				text+=z.name +"</td> <td>";
				text+=String.valueOf(z.getSize()) +"</td> <td>";
				
				for(Node n : z.areas)
					text+= "-"+String.valueOf(n.nodeName)+"&nbsp;";
				
				text +="</td></tr>\n";
				p++;
			}
				
			text += "</table>";
			text += "\n</html>\n\n";
			
			return text;
			
		}
	
	
	
	
	class NodeCoef{
		float A, B, D;
		
		public NodeCoef(float a, float b, float d){
			A =a;
			B=b;
			D=d;
		}
		
		public float getA(){
			return A;
		}
		
		public float getB(){
			return B;
		}
		
		public float getD(){
			return D;
		}
		
		
		public void setA(float a){
			A = a;
		}
		
		public void setB(float b){
			B = b;
		}
		
		public void setD(float d){
			 D = d;
		}
		
	}
	
	
	class Edge {
		Node a, b;
		
		public Edge (Node ia, Node ib) {
			a=ia;
			b=ib;
		}
		
		public Node getA () {
			return a;
		}
		
		public Node getB () {
			return b;
		}
		
		public float getDistance(){
			float distance=0;
			
			distance = (float) Math.sqrt(Math.pow((a.representative.xcoord-b.representative.xcoord),2) +Math.pow((a.representative.ycoord- b.representative.ycoord),2));
			return distance;
			}
	}
	
	class AreaCoords{
		double[] values = new double[6];
				
		java.awt.Point A = new Point();
		java.awt.Point B = new Point();
		java.awt.Point C = new Point();
				
		public AreaCoords(double[] val){
			if(val.length<6)
				System.out.println("Requeried 6 values in the array");
			else
				A.setLocation(val[0], val[1]);
		    	B.setLocation(val[2], val[3]);
		    	C.setLocation(val[4], val[5]);
							
				for (int i = 0; i < 6; i++) {
					values[i] = val[i];
				}
		}
						
		public double getX1(){
			return values[0];
		}
		
		public double getX2(){
			return values[2];
		}
		
		public double getX3(){
			return values[4];
		}
		
	
		public double getY1(){
			return values[1];
		}
		
		public double getY2(){
			return values[3];
		}
		
		public double getY3(){
			return values[5];
		}
		
		public int[] getXArray(){
			int [] xValues = {(int)A.x, (int)B.x,(int)C.x};
			return xValues;
		}
		
		public int[] getYArray(){
			int [] yValues =   {(int)A.y, (int)B.y,(int)C.y};
			return yValues;
		}
		
		
	}

	}
