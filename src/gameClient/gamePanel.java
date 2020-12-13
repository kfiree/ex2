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
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

public class gamePanel extends JPanel {
    private long timeLeft;
    private int _ind;
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private boolean didThatalready = false, doneThat;

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
        g.setFont(new Font("MV Boli", Font.BOLD,15));

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

    private void drawProgressBarAndTitle(Graphics g){
        int currValue=0;
        for(CL_Agent a: _ar.getAgents()){
            currValue+= a.getValue();
        }

        int totalValue = currValue;
        for(CL_Pokemon p: _ar.getPokemons()){
            totalValue += p.getValue();
        }

        ImageIcon headline = new ImageIcon("arena.png");
        g.drawImage(headline.getImage(), (this.getWidth()/2)-200, 10, 400, 45, this);

        JProgressBar bar = new JProgressBar(0,totalValue);
        bar.setBounds(0, 80 ,this.getWidth(),50);
        bar.setStringPainted(true);
        bar.setBackground(Color.black);
        bar.setForeground(Color.red);
        this.add(bar);
        g.setColor(Color.white);
        bar.setString("Time left for game " + timeLeft );
//        bar.setString("Value of " + currValue + "Pokemons have been captured out of " + totalValue);
        bar.setValue(currValue);
        this.add(bar);
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
        ImageIcon yellowPokemon = new ImageIcon("pikachu.png");
        ImageIcon greenPokemon = new ImageIcon("bulbasaur.png");
        ImageIcon pokemonIcon = yellowPokemon;
        if(fs!=null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while(itr.hasNext()) {
                CL_Pokemon pokemon = itr.next();

                Point3D point = pokemon.getLocation();
                int r=10;
                g.setColor(Color.green);
//                if(pokemon.getType()<0) {g.setColor(Color.orange);}
                if(point!=null) {
                    geo_location pokemonLocation = this._w2f.world2frame(point);

                    if(pokemon.getType()<0) {
                        pokemonIcon = greenPokemon;
                    }else {
                        pokemonIcon = yellowPokemon;
                    }
                    g.drawImage(pokemonIcon.getImage(), (int) pokemonLocation.x() - r, (int) pokemonLocation.y() - r, 2 * r + 5, 2 * r + 5, this);
                    g.setFont(new Font("Wide Latin", Font.BOLD,15));
                    g.setColor(new Color(0x890808));
                    g.drawString(new DecimalFormat(".#").format(pokemon.getValue()) + "", (int)pokemonLocation.x()-2*r, (int)pokemonLocation.y()-r);
                    g.setFont(new Font("MV Boli", Font.BOLD,15));
//                  g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

                }
            }
            didThatalready = true;
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

                drawProgressBarAndTitle(g);
                geo_location fp = this._w2f.world2frame(agentLocation);
                g.drawImage(ashImg.getImage(), (int)fp.x()-r, (int)fp.y()-r,4*r, 4*r, this);
                g.setColor(new Color(0x000000));

                g.drawString(agent.getID() + ": to-" + agent.getNextNode() + " from- " + agent.getSrcNode() , (int)fp.x()-50, (int)fp.y()-2*r);
                g.setFont(new Font("Wide Latin", Font.BOLD,15));
                g.setColor(new Color(0x890808));
                g.drawString(new DecimalFormat(".#").format(agent.getValue()) + "", (int)fp.x()-20, (int)fp.y()+4*r);
                g.setFont(new Font("MV Boli", Font.BOLD,15));
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

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }
}
