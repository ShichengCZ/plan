package application;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
 
class JPanelDemo extends JPanel {
     
    JLabel label;
    ImageIcon image;
    int x = 550;
    int y = 554;
    public JPanelDemo() {
         
        image = new ImageIcon("src/application/abc.png");
        label = new JLabel(image);
        add(label);
        image.setImage(image.getImage().getScaledInstance(x, y, 100));
    
    }
}
 
class App extends JFrame {
     
    public App() {
         
        this.setSize(675, 633);
        
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
        this.add(new JPanelDemo());
    }
}