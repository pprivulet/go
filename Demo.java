import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
import java.awt.Color;
import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.event.*;


public class Demo extends JPanel implements MouseListener {
  
    Image img, wGo, bGo; 
    int i = 0, j = 0;
    int eage = 30;
    int gap = 30;
    int rows = 19;
    int cols = 19;
    ArrayList<Go> goList = new ArrayList<Go>();
    
    int[][] goboard = new int[rows][cols]; 
    int LIVE = 1;
    int DEAD = 2;  
    int[] MAYJIE = {-1,-1};
    static final int U = 1;
    static final int D = 2;
    static final int L = 3;
    static final int R = 4;
    
    
    
    
    public Demo() {
        img = Toolkit.getDefaultToolkit().getImage("smile.jpg"); 
        wGo = Toolkit.getDefaultToolkit().getImage("w30.png"); 
        bGo = Toolkit.getDefaultToolkit().getImage("b30.png");
        addMouseListener(this);
    }

    int depth4D(int i, int j, int dir, int color, ArrayList<Go> DeadList){
        Go go;
        System.out.println("<"+dir+">");
        switch(dir){
            case U: i = i - 1; break;
            case D: i = i + 1; break;
            case L: j = j - 1; break;
            case R: j = j + 1; break;
            default: break;
        }
        
        System.out.println("<depth4D: i="+i+" "+"j="+j+">");
        
        if (i<0 || i>18 || j<0 || j>18){
            System.out.println("<depth4D: out eage>");
            return DEAD;
        }
        
        if (goboard[i][j] == 0){
            System.out.println("<depth4D: position null>");
            return LIVE;
        } else if (goboard[i][j] != color) {
            System.out.println("<depth4D: oppo go>");
            return DEAD;
        } else {
            for (int k=0; k<DeadList.size(); k++) {
                go = DeadList.get(k);
                if (go.getX() == i && go.getY() == j){
                    if(go.getStatus()==DEAD){
                        System.out.println("<depth4D: go in dead list>");
                        return DEAD;
                    } else {
                        return LIVE;
                    }                    
                }
            }
            return depth(i, j, color, DeadList);     
        }  
    }


    int depth(int i, int j, int color, ArrayList<Go> DeadList){
        Go go = new Go(i, j, color);
        go.setStatus(DEAD);
        DeadList.add(go);
        if ( depth4D(i, j, U, color, DeadList) == LIVE ) {
            for(int k=0; k<DeadList.size();k++){
                go = DeadList.get(k);
                System.out.println("trace:i="+go.getY()+" "+"j="+go.getX());
                go.setStatus(LIVE);            
            }
            System.out.println("<depth: up live>");
            return LIVE;    
        }
        
        if ( depth4D(i, j, D, color, DeadList) == LIVE ) {
            for(int k=0; k<DeadList.size();k++){
                go = DeadList.get(k);
                System.out.println("trace:i="+go.getY()+" "+"j="+go.getX());            
                go.setStatus(LIVE);           
            }
            
            System.out.println("<depth: down live>");
            return LIVE;
        }
        
        if ( depth4D(i, j, L, color, DeadList) == LIVE ) {
            for(int k=0; k<DeadList.size();k++){
                go = DeadList.get(k);
                System.out.println("trace:i="+go.getY()+" "+"j="+go.getX());            
                go.setStatus(LIVE);           
            }
            
            System.out.println("<depth: left live>");
            return LIVE;
        }
        
        if ( depth4D(i, j, R, color, DeadList) == LIVE ) {
            for(int k=0; k<DeadList.size();k++){
                go = DeadList.get(k);
                System.out.println("trace:i="+go.getY()+" "+"j="+go.getX());            
                go.setStatus(LIVE);           
            }
            
            System.out.println("<depth: right live>");
            return LIVE;
        }
        
        return DEAD;
    }

    void check4D(int i, int j, int dir, int oppoColor, ArrayList<Go> oppoDeadList){
        switch(dir){
            case U: i = i - 1; break;
            case D: i = i + 1; break;
            case L: j = j - 1; break;
            case R: j = j + 1; break;
            default: break;
        }
        
        if (i<0 || i>18 || j<0 || j>18){
            System.out.println("<check4D: out eage>");
            return;
        }
        
        if (goboard[i][j] == 0) {
            System.out.println("<check4D: position null>");
            return;
        }        
        
        if (goboard[i][j] != oppoColor){
            System.out.println("<check4D: same color>");
            return;
        }
        
        System.out.println("<check4D: depth>");
        depth(i, j, oppoColor, oppoDeadList);
        System.out.println("</check4D: depth>");
        
        return;

    }


    void getDeadList(ArrayList<Go> DeadList, ArrayList<Go> TraceList){
        Go go;
        int status=0;
        
        for(int k=0; k<TraceList.size();k++){
            go = TraceList.get(k);
            System.out.println("----traceList:i="+go.getY()+" "+"j="+go.getX()+" s:"+go.getStatus());            
        }
        
         for(int k=0; k<DeadList.size();k++){
            go = DeadList.get(k);
            System.out.println("----DeadList:i="+go.getY()+" "+"j="+go.getX()+" s:"+go.getStatus());            
        }
        
        for(int i=0; i<TraceList.size(); i++){
            go = TraceList.get(i);
            if(go.getStatus()==DEAD){
                for(int j=0; j<DeadList.size(); j++){
                    if(DeadList.get(j).getX()==go.getX() && DeadList.get(j).getY()==go.getY()){
                        status = 1;
                        break;
                    }    
                }
                if(status != 1)DeadList.add(go);            
            }
            status = 0;       
        }
    }





    ArrayList<Go> checkOppoLife(int color, int i, int j, ArrayList<Go> oppoTraceList){
        int oppoColor = 3 - color;
        ArrayList<Go> DeadList = new ArrayList<Go>();
        Go go;        
        
        System.out.println("check up:");    
        check4D(i, j, U, oppoColor, oppoTraceList);
        getDeadList(DeadList, oppoTraceList); 
                                
        System.out.println("check down:");
        check4D(i, j, D, oppoColor, oppoTraceList);
        getDeadList(DeadList, oppoTraceList);        
               
        System.out.println("check left:");
        check4D(i, j, L, oppoColor, oppoTraceList);
        getDeadList(DeadList, oppoTraceList);
        
        System.out.println("check right:");
        check4D(i, j, R, oppoColor, oppoTraceList);
        getDeadList(DeadList, oppoTraceList);               
        
        for(int k=0; k<oppoTraceList.size();k++){
            go = oppoTraceList.get(k);
            System.out.println("traceList:i="+go.getY()+" "+"j="+go.getX()+" s:"+go.getStatus());            
        }
        
         for(int k=0; k<DeadList.size();k++){
            go = DeadList.get(k);
            System.out.println("DeadList:i="+go.getY()+" "+"j="+go.getX()+" s:"+go.getStatus());            
        }
        
        return DeadList;                

    }
    



    ArrayList<Go> checkSelfLife(int color, int i, int j, ArrayList<Go> selfTraceList){
        ArrayList<Go> DeadList = new ArrayList<Go>();        
        depth(i, j, color, selfTraceList);
        getDeadList(DeadList, selfTraceList);  
        return DeadList;
    }

    void pickOffDeadGo(ArrayList<Go> goList, ArrayList<Go> DeadList) {
        int i, j, hd, wd, hg, wg;
        Go go;
        for (i=0; i<DeadList.size();i++){
            go = DeadList.get(i);
            hd = go.getX();
            wd = go.getY();
            for(j=0; j<goList.size(); j++){
                hg = goList.get(j).getX();
                wg = goList.get(j).getY();
                if ( (hg==hd) && (wg==wd) ) {
                    goList.remove(j);
                    goboard[hg][wg] = 0;
                    System.out.println("pick off:i="+hg+" "+"j="+wg+" "+goboard[hg][wg]);
                    break;
                }      
           }            
        }        
    }

    public void mousePressed(MouseEvent e){
        int color = 1; 
        i = (e.getY() - eage + gap/2 ) / gap;
        j = (e.getX() - eage + gap/2 ) / gap;       
        
        
                
        
        switch(e.getModifiers()) {
             case InputEvent.BUTTON1_MASK: {
                 if (goboard[i][j] != 0) {
                    System.out.println("go exsits"); 
                    return;
                 }
                 System.out.println("That's the LEFT button");   
                 color = 1;  
                 break;
             }
             case InputEvent.BUTTON2_MASK: {
                 System.out.println("That's the MIDDLE button");
                 goList = new ArrayList<Go>();
                 goboard = new int[rows][cols];
                 MAYJIE[0] = -1; MAYJIE[1] = -1; 
                 repaint();     
                 return;
             }
             case InputEvent.BUTTON3_MASK: {
                 if (goboard[i][j] != 0) {
                    System.out.println("go exsits"); 
                    return;
                 }
                 System.out.println("That's the RIGHT button"); 
                 color = 2;    
                 break;
             }
        }      
        
	    ArrayList<Go> selfTraceList = new ArrayList<Go>();
        ArrayList<Go> oppoTraceList = new ArrayList<Go>();
        ArrayList<Go> oppoDeadList = new ArrayList<Go>();
        ArrayList<Go> selfDeadList = new ArrayList<Go>();
        
        Go go = new Go(i, j, color);
        goList.add(go);
        goboard[i][j] = color;
        System.out.println("<put "+i+","+j+" "+goboard[i][j]+">");


	    oppoDeadList = checkOppoLife(color, i, j, oppoTraceList);         
        if (oppoDeadList.size()==0){
            //MAYJIE = {-1, -1}
            MAYJIE[0] = -1;
            MAYJIE[1] = -1;
            selfDeadList = checkSelfLife(color, i, j, selfTraceList);
            if(selfDeadList.size()!=0){
                goList.remove(go);
                goboard[i][j] = 0;
                System.out.println("oppo live , self dead");                    
            }    
        } else if (oppoDeadList.size()==1){
            
            if (MAYJIE[0]==i && MAYJIE[1]==j) {
                goList.remove(go);
                goboard[i][j] = 0;
                System.out.println("oppo 1 dead , self 1 dead, jie");
            } else {
                int DeadGoPosH = oppoDeadList.get(0).getX();
                int DeadGoPosW = oppoDeadList.get(0).getY();
                pickOffDeadGo(goList, oppoDeadList);
                MAYJIE[0] = DeadGoPosH;
                MAYJIE[1] = DeadGoPosW;
            } 
        } else {
            MAYJIE[0] = -1;
            MAYJIE[1] = -1;
            pickOffDeadGo(goList, oppoDeadList);
        }
        System.out.println("Dead goes: "+oppoDeadList.size());
        
        
 
        repaint();
    }

    public void mouseClicked(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Go go;
        int h,w;   
        g.drawImage(img, 0, 0, this);      
        for (int i=0; i<rows; i++){
            g.drawLine(eage, eage+i*gap, eage+(cols-1)*gap, eage+i*gap);
        }

        for (int i=0; i<cols; i++) {
            g.drawLine(eage+i*gap, eage, eage+i*gap, eage+(rows-1)*gap);
        }  
        
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                 g.fillOval(eage+3*gap+i*6*gap-4, eage+3*gap+j*6*gap-4, 8, 8);
            }
        }
        
        for (int i=0; i< goList.size(); i++) {
            go = goList.get(i);
            //System.out.println(i + ":" + go.getX());
            w = go.getX() * gap + eage - gap/2;
            h = go.getY() * gap + eage - gap/2;
            if (go.getColor() == 1)
                g.drawImage(wGo, h, w, this);
            else if (go.getColor() == 2){
                g.drawImage(bGo, h, w, this);
            }
        }                                        
                                                                    
    }

    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }



    public static void main(String[] args) {
        Demo demo = new Demo();
        JFrame jframe = new JFrame("Demo");
        jframe.add(demo);
        demo.setOpaque(true);
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        jframe.pack();
        jframe.setVisible(true);
    }
}

