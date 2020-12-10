package gameClient;
import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class gamePanel extends JPanel {
    int totalValue = 0;
    private int _ind;
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    int barlocation = 0;

    gamePanel(){
    }

    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
    }

    private void updateFrame() {
        double i = 0.0;
        Range rx = new Range(20,this.getWidth()-20);
        Range ry = new Range(this.getHeight()-10,150);
        Range2D frame = new Range2D(rx,ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g,frame);
    }
    public void
    paint(Graphics g) {

        int w = this.getWidth();
        int h = this.getHeight();
//        g.clearRect(0, 0, w, h);
        g.setFont(new Font("MV Boli", Font.BOLD,20));
        updateFrame();

        drawGraph(g);
        drawPokemons(g);
        drawAgents(g);
//        drawInfo(g);

    }
//    private void drawInfo(Graphics g) {
//        java.util.List<String> str = _ar.get_info();
//        String dt = "none";
//        for(int i=0;i<str.size();i++) {
//            g.drawString(str.get(i)+" dt: "+dt,100,60+i*20);
//        }
//
//    }

    private void drawProgressBar(Graphics g, CL_Agent agent) {
        JProgressBar bar = new JProgressBar();
        bar=new JProgressBar();
        bar.setValue(0);
        bar.setBounds(0, agent.getID()*15 ,1000,15);
        bar.setMinimum(0);
        bar.setMaximum(totalValue);
        bar.setBackground(Color.black);
        bar.setForeground(Color.red);

        this.add(bar);
        g.setColor(Color.white);
        bar.setString("agent " + agent.getID() + " caught " + agent.getValue() + " pokemons");
        bar.setValue((int)agent.getValue());
        this.add(bar);
        barlocation++;
    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while(iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.blue);
            drawNode(n,5,g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while(itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }
    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        ImageIcon pokemonImg = new ImageIcon("pikachu.png");
        if(fs!=null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while(itr.hasNext()) {
                CL_Pokemon pokemon = itr.next();
                totalValue +=pokemon.getValue();
                Point3D point = pokemon.getLocation();
                int r=10;
                g.setColor(Color.green);
//                if(pokemon.getType()<0) {g.setColor(Color.orange);}
                if(point!=null && pokemon.getType()<0) {
                    geo_location pokemonLocation = this._w2f.world2frame(point);
                    g.drawImage(pokemonImg.getImage(), (int)pokemonLocation.x()-r, (int)pokemonLocation.y()-r,2*r+5, 2*r+5, this);

//                  g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

                }
            }
        }
    }
    private void drawAgents(Graphics g) {
        List<CL_Agent> agentsList = _ar.getAgents();
        ImageIcon ashImg = new ImageIcon("ash.png");
//        Iterator<OOP_Point3D> itr = rs.iterator();
        g.setColor(Color.red);

        int i=0;
        while(agentsList!=null && i<agentsList.size()) {
            CL_Agent agent = agentsList.get(i);
            geo_location agentLocation = agent.getLocation();
            int r=8;
            i++;
            if(agentLocation!=null) {
//                drawProgressBar(g, agent);
                geo_location fp = this._w2f.world2frame(agentLocation);
                g.drawImage(ashImg.getImage(), (int)fp.x()-r, (int)fp.y()-r,4*r, 6*r, this);
                g.setColor(new Color(0x000000));

                g.drawString(agent.getID() + " - " + agent.getValue(), (int)fp.x(), (int)fp.y()-4*r);
            //                g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
            }
        }
    }
    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.setColor(new Color(0x8D490E));
        g.fillOval((int)fp.x()-r, (int)fp.y()-r, 2*r, 2*r);
        g.setColor(new Color(0x000000));
        g.drawString(""+n.getKey(), (int)fp.x(), (int)fp.y()-4*r);
    }
    private void drawEdge(edge_data e, Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g2D.setColor(new Color(0x8D490E));
        g2D.setStroke(new BasicStroke(4));
        g2D.drawLine((int)s0.x(), (int)s0.y(), (int)d0.x(), (int)d0.y());
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }
}
