/* Demo2.java */

/*=======================================================================*/
/* Imports                                                               */
/*=======================================================================*/

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.*;
import java.net.*;
import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent; 

import java.util.Enumeration;

//import java.net.*;                 // for getImage & getAudioClip
//import sun.applet.AppletAudioClip; // for AppletAudioClip

import com.ibm.graph.*;
import com.ibm.graph.draw.*;
import com.ibm.graph.layout.*;
import com.ibm.graph.awt.GraphCanvas;

import javax.swing.*;

public class Demo2 extends JPanel
{
  GraphCanvas gc;
  GraphCanvas gc2;
  ScrollPane scrollPane;
  Draw3VertexCircle d3vCircle;
  demoVertex d3vImage;
  LayoutTree lt;
  Tree tree;
  String strKeyIndex = "!!index";

  //--------------------------------------------------------------

  public void init() 
  {
    // The tree:
    tree = new Tree();

    // The vertex drawable:
    d3vCircle = new Draw3VertexCircle( 5 );
    d3vCircle.setColorFill( Color.red );
    //image = image.getScaledInstance(25, 25, 0);

    // The tree layout:
    lt = new LayoutTree();
    lt.setDirection( LayoutTree.TOP_TO_BOTTOM );
    lt.setPreOrder( false );
    tree.setGraphLayoutManager( lt );

    _init( tree );

    tree.layout();

    gc = new GraphCanvas( tree );
    gc2 = new GraphCanvas( tree );
    try
    {
      Dimension dimTree = tree.getSize();
      gc.setSize( dimTree.width + 200 , dimTree.height + 150 ); 
    }
    catch ( NotDrawableException e )
    {
      /* Should not happen */
      gc.setSize( 1000 , 500 );
      gc2.setSize( 1000 , 500 );
    }
    gc2.setBackground( Color.lightGray );
    gc.setBackground( Color.lightGray );
     scrollPane = new ScrollPane();
    scrollPane.setBackground( Color.lightGray );
    scrollPane.add( gc );

    this.setLayout(new BorderLayout());
    this.add( "Center" , scrollPane );
    this.validate();

    //this.add( gc );
  }

  private void _init( Tree tree )
  {
    boolean zFound;
  ;;boolean zAdded;
    MoteVertex vertex1 = null;

	       vertex1 = new MoteVertex();
	       vertex1.number = 5;
	       vertex1.light = 0xff;
	       vertex1.forwarded = 0;
	       vertex1.sent = 0;
	       vertex1.systemdict.def( strKeyIndex , 5 );
	       _init( tree , vertex1);
      tree.setRoot( vertex1 );

  }

  private void _init( Tree tree , Vertex vertex)
  {
    tree.add( vertex );
    d3vImage = new demoVertex();
    vertex.setDrawable( d3vImage );
    //vertex.setDrawable( d3vCircle );
  }

  private void _init( Tree tree , Edge edge )
  {
    tree.add( edge );
  }

  Dimension _getSize()
  {
    return gc.getSize();
  }

  //--------------------------------------------------------------
  // Lets this be run as either an applet or an application.
  // Generated by JavaStub: http://www.apl.jhu.edu/~hall/java/JavaStub.html
  static Frame mainFrame;
  static Demo2 app;
  public static void main(String[] args) {
    mainFrame = new Frame("Demo2");
    app = new Demo2();
    app.init();
    mainFrame.setSize( app._getSize() );
    mainFrame.add("Center", app);
    mainFrame.show();
    mainFrame.repaint(1000);
    mainFrame.addWindowListener
      (
        new WindowAdapter()
        {
          public void windowClosing    ( WindowEvent wevent )
          {
            System.exit(0);
          }
        } 
      );
    app.go();
  }
  //--------------------------------------------------------------

    static int GRAPH_TABLE_DEPTH = 25;
    static int GRAPH_TABLE_ENTRY_SIZE = 6;
    static int GRAPH_TABLE_ENTRIES_PER_LINE = 30;
    byte[][] rd_data = new byte[GRAPH_TABLE_DEPTH][GRAPH_TABLE_ENTRY_SIZE * GRAPH_TABLE_ENTRIES_PER_LINE];

   public void go(){
	DatagramSocket sk = null;
	try{
                sk = new DatagramSocket(5001);
        }catch(Exception e){
             System.out.println(e.getMessage());
             e.printStackTrace();
         }



       while(true){
	   try{
	       byte[] data = new byte[4500];
	       DatagramPacket pack = new DatagramPacket(data, 4500);
	       sk.receive(pack);

	       tree.clear();
	       MoteVertex vertex1 = null;
	       MoteVertex vertex2 = null;
	      for(int i = 0; i < GRAPH_TABLE_DEPTH; i ++){
		  for(int j = 0; j < GRAPH_TABLE_ENTRY_SIZE * GRAPH_TABLE_ENTRIES_PER_LINE; j ++){
		      rd_data[i][j] = data[i*GRAPH_TABLE_ENTRY_SIZE * GRAPH_TABLE_ENTRIES_PER_LINE + j];
		  }
	      }
 	      int sent = ((int)rd_data[0][5]) & 0xff;
             int forwarded = ((int)rd_data[0][6]) & 0xff;
	       vertex1 = new MoteVertex();
	       vertex1.systemdict.def( strKeyIndex , 5 );
	       vertex1.number = 5;
	       vertex1.light = 0xff;
	       vertex1.forwarded = 0;
	       vertex1.sent = 0;
	       _init( tree , vertex1);
	       tree.setRoot(vertex1);

	       for(int i = 1; i < 25; i ++){
		   for(int j = 0; j < rd_data[i][0]; j ++){
		       int number = rd_data[i][GRAPH_TABLE_ENTRY_SIZE*j + 1];
		       int parent = rd_data[i][GRAPH_TABLE_ENTRY_SIZE*j + 2];
		       //       System.out.println("parent " + parent + " " + j + " " + 
				//	  i);
		       parent = rd_data[i - 1][GRAPH_TABLE_ENTRY_SIZE*parent + 1];
		       int temp = ((int) rd_data[i][GRAPH_TABLE_ENTRY_SIZE*j + 4]) & 0xff;
		       temp |= (((int) rd_data[i][GRAPH_TABLE_ENTRY_SIZE*j + 3]) & 0xff) << 8;
		       sent = ((int)rd_data[i][GRAPH_TABLE_ENTRY_SIZE*j + 5]) & 0xff;
	               forwarded = ((int)rd_data[i][GRAPH_TABLE_ENTRY_SIZE*j + 6]) & 0xff;

		       System.out.println("Looking for : " + parent + " for " + 
					  number + " temp: " + temp);
		       Enumeration vertices =
			   tree.enumerateVerticesBySystemKeySetToValue(
								       strKeyIndex , parent);
		       vertex1 = (MoteVertex)vertices.nextElement();
		       vertex2 = new MoteVertex();
		       vertex2.systemdict.def( strKeyIndex , number );
		       MoteVertex thisMote = (MoteVertex)vertex2;
			thisMote.number = number;
			thisMote.light =  temp >> 2 & 0xff;
			thisMote.sent = sent;
			thisMote.forwarded = forwarded;
			thisMote.parent = parent;
		       _init( tree , vertex2);
		       tree.add( new Edge( vertex1 , vertex2 ) );
		   }
	       }
 
	System.out.println("here");
	       //vertex2 = new Vertex();
	       //vertex2.systemdict.def( strKeyIndex , 7 );
	       //_init( tree , vertex1, 5, 0xff);
	       //	       _init( tree , vertex2, 7, 0xff);
	       //	       tree.setRoot(vertex1);
	       //	       tree.add( new Edge( vertex1 , vertex2 ) );
	       //for ( Enumeration vertices = tree.enumerateVertices() ; vertices.hasMoreElements() ; )
	       //  {
	       //      tree.setRoot( (Vertex)vertices.nextElement() );
	       //  }
	       tree.layout();
	      //scrollPane.remove(gc);
	      //scrollPane.add(gc2);
	      //mainFrame.repaint();
  	//GraphCanvas gc3 = gc2;
	       //gc = gc2;
	//gc2 = gc3;
      	Dimension dimTree = tree.getSize();
      		gc.setSize( dimTree.width + 200 , dimTree.height + 150 ); 
	       gc.repaint();
		
	       System.out.println("got update");
	   }catch(Exception e){
	       System.out.println(e.getMessage());
	       e.printStackTrace();
	   }
	   
       }

}

	void displayUpdate(){
	       tree.layout();
	       gc.repaint();
	}


} // class Demo2


class demoVertex extends DrawVertex {

	static Image img;
	static int img_width = 50;
	public demoVertex(){
		if(this.img == null) this.img = new ImageIcon("mote2.jpg").getImage();
	}

	//public void draw(GraphObject goVertex, java.awt.Graphics g){
		//System.out.println("draw called");
	//}
	public void drawVertex(Vertex goVertex, java.awt.Graphics g){
		try{
			int number = ((MoteVertex)goVertex).number;
			int light = ((MoteVertex)goVertex).light;
			int sent = ((MoteVertex)goVertex).sent;
			int forwarded = ((MoteVertex)goVertex).forwarded;
			//System.out.println("drawing vertex");
			int x = getX(goVertex) - (img_width /2);
			int y = getY(goVertex) - (img_width /2);
			g.drawImage(img, x, y, img_width, img_width, null);
			 g.drawString("Mote: " + (number / 16) + number % 16, x+img_width, y+img_width);
			 g.drawString(sent + "/"+ forwarded, x+img_width+15, y + 40);


			 g.setColor(new Color(light, light, light));
      			  g.fillRect(x+img_width, y, 40,  img_width/2);
       			 g.setColor(new Color(0, 0, 0));
      			  g.drawRect(x+img_width, y, 40,  img_width/2);
	
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public java.awt.Dimension getSizeVertex(Vertex vertex){
		return new Dimension(img_width + 50, img_width + 30);
	}
	
	public java.awt.Rectangle getBoundsVertex(Vertex vertex){
		return new Rectangle(img_width + 50, img_width + 30);
	}

}
	
class MoteVertex extends Vertex{

	int number;
	int parent;
	int light;
	int sent;
	int forwarded;
}
