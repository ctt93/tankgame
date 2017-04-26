package tankgame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class MyTankGame1 extends JFrame implements ActionListener{
MyPanel mp=null;

//����һ����ʼ���
MyStartPanel msp=null;

//��������Ҫ�Ĳ˵�
JMenuBar jmb=null;
//��ʼ��Ϸ
JMenu jm1=null;
JMenuItem jmi1=null;
JMenuItem jmi2=null;
JMenuItem jmi3=null;
JMenuItem jmi4=null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
MyTankGame1 mtg=new MyTankGame1();
	}
//���캯��
	public MyTankGame1()
	{
		
		//mp=new MyPanel();
		//����mp�߳�
		//Thread t=new Thread(mp);
		//t.start();
		
		//this.add(mp);
		
		//this.addKeyListener(mp);
		
		//�����˵����˵�ѡ��
		jmb=new JMenuBar();
		jm1= new JMenu("��Ϸ��G��");
		//���ÿ�ݷ�ʽ
		jm1.setMnemonic('G');
		jmi1=new JMenuItem("��ʼ����Ϸ��M��");
		//��jmi1��Ӧ
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		jm1.add(jmi1);
		
		
		jmi2=new JMenuItem("�˳���B��");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exit");
		jm1.add(jmi2);
		
		jmi3=new JMenuItem("�����˳���C��");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("saveexit");
		jm1.add(jmi3);
		
		jmi4=new JMenuItem("������һ����Ϸ��S��");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("congame");
		jm1.add(jmi4);
		
		jmb.add(jm1);
		
		msp=new MyStartPanel();
		Thread t=new Thread(msp);
		t.start();
		
		this.setJMenuBar(jmb);
		this.add(msp);
		this.setSize(600, 500);
		this.setVisible(true);
	}
@Override
public void actionPerformed(ActionEvent arg0) {
	// TODO Auto-generated method stub
	//���û���ͬ�ĵ��������ͬ�Ĵ���
	if(arg0.getActionCommand().equals("newgame"))
	{
		//����ս��
		mp=new MyPanel("newgame");
				//����mp�߳�
				Thread t=new Thread(mp);
				t.start();
				
				//��ɾ���ɵ����
				this.remove(msp);
				this.add(mp);
				
				this.addKeyListener(mp);
				this.setVisible(true); 
	}
	else if(arg0.getActionCommand().equals("exit"))
	{
	 //�û�������˳�ϵͳ�Ĳ˵�
		//������ٵ��˵�����
		Recorder.keepRecording();
		
		System.exit(0);
	}else if(arg0.getActionCommand().equals("saveexit"))
	{
		Recorder.setEts(mp.ets);
        Recorder.keepRecorderandEnenmyTank();
		
		System.exit(0);
	}else if(arg0.getActionCommand().equals("congame")){
		//����ս��
				mp=new MyPanel("con");
						//����mp�߳�
						Thread t=new Thread(mp);
						t.start();
						
						//��ɾ���ɵ����
						this.remove(msp);
						this.add(mp);
						
						this.addKeyListener(mp);
						this.setVisible(true); 
	}
}




}


class MyStartPanel extends JPanel implements Runnable{
	
	int times=0;
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		//��ʾ��Ϣ
		if(times%2==0)
		{
		g.setColor(Color.yellow);
		//������Ϣ������
		Font myFont=new Font("������κ", Font.BOLD, 30);
		g.setFont(myFont);
		g.drawString("Stage: 1", 150, 150);
		}
	}

	
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try{
				Thread.sleep(500);
			}catch(Exception e){
				e.printStackTrace();
			}
			times++;
			this.repaint();
		}
	}
}


//�ҵ����
class MyPanel extends JPanel implements KeyListener,Runnable
{
	//����һ���ҵ�̹��
	Hero hero=null;
	//�������̹����
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	int enSize=3;
	//����ը������
	Vector<Bomb> bombs=new Vector<Bomb>();
	Vector<Node> nodes=new Vector<Node>();
	//��������ͼƬ,����ͼƬ���һ��ը��
	Image image1=null;
	Image image2=null;
	Image image3=null;
	
	//���캯��
	public MyPanel(String flag)
	{
		//�ָ���¼
		Recorder.getRecording();
		
		hero=new Hero(100,100);
		
		if(flag=="newgame")
		{	//��ʼ������̹��
		for(int i=0;i<enSize;i++)
		{
			//��������̹�˶���
			EnemyTank et=new EnemyTank((i+1)*50,0);
			et.setColor(0);
			et.setDirect(2);
			
			et.setEts(ets);
			//��������̹��
			Thread t=new Thread(et);
			t.start();
//			//������̹�����һ���ӵ�
//			Shot s=new Shot(et.x+10, et.y+30, 2);
//			et.ss.add(s);
//			Thread t2=new Thread(s);
//			t2.start();
			//����
			ets.add(et);
		}
		}else 
		{
			 nodes=new Recorder().getnodes();
			 System.out.println(nodes.size());
			for(int i=0;i<nodes.size();i++)
			{
				Node node=nodes.get(i);
				//��������̹�˶���
				EnemyTank et=new EnemyTank(node.x,node.y);
				et.setColor(0);
				et.setDirect(node.direct);
				
				et.setEts(ets);
				//��������̹��
				Thread t=new Thread(et);
				t.start();
//				//������̹�����һ���ӵ�
//				Shot s=new Shot(et.x+10, et.y+30, 2);
//				et.ss.add(s);
//				Thread t2=new Thread(s);
//				t2.start();
				//����
				ets.add(et);
			}
		}
		//��ʼ��ͼƬ
		try {
			image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/1.jpg"));
			image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/2.jpg"));
			image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/3.jpg"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		//���ſ�ս����
		PlaySounds apw=new PlaySounds("d:\\120.wav");
		apw.start();
	
	}
	
	public void showInfo(Graphics g){
		//������ʾ��Ϣ̹��
				this.drawTank(80, 330, g, 0, 0);
				g.setColor(Color.black);
				g.drawString(Recorder.getEnum1()+"", 110, 350);
				this.drawTank(130, 330, g, 0, 1);
				g.setColor(Color.black);
				g.drawString(Recorder.getMylife()+"", 160, 350);
				
				//������ҵ��ܳɼ�
				g.setColor(Color.black);
				Font f=new Font("����", Font.BOLD, 20);
				g.setFont(f);
				g.drawString("�����ܳɼ�", 420, 30);
				
				this.drawTank(420, 60, g, 0, 0);
				g.setColor(Color.black);
				g.drawString(" * "+Recorder.getEnum2(), 450, 80);
				
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0,0,400,300);
		
		//������ʾ��Ϣ
		this.showInfo(g);
		
		//�����Լ���̹��
		if(hero.islive)
		{
		this.drawTank(hero.getX(), hero.getY(), g, this.hero.direct, 1);
		}
		//��ss��ȡ�������ӵ�
		for(int i=0;i<this.hero.ss.size();i++)
		{
			Shot myShot=hero.ss.get(i);
		//�����ӵ�,����һ���ӵ�
		if(myShot!=null&&myShot.islive==true)
		{
			g.draw3DRect(myShot.x, myShot.y, 1, 1, false);
		}
		if(myShot.islive==false)
		{
			hero.ss.remove(myShot);
		}
		}
		
		//����ը��
		for (int i = 0; i < bombs.size(); i++) 
		{
			//ȡ��ը��
			Bomb b=bombs.get(i);
			if(b.life>6)
			{
				g.drawImage(image1, b.x, b.y, 30,30,this);
			}else if(b.life>3)
			{
				g.drawImage(image2, b.x, b.y, 30, 30,this);
			}else if(b.life>0)
			{
				g.drawImage(image3, b.x, b.y, 30, 30,this);
			}
			b.lifedown();
			if(b.life==0)
			{
				bombs.remove(b);
			}
			
		}
		
		//��������̹��
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			//�ж�̹���Ƿ���Ч
			if(et.islive)
			{
			this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 0);
			//�ٻ������˵��ӵ�
			for (int j = 0; j < et.ss.size(); j++) {
				Shot enemyShot=et.ss.get(j);
				if(enemyShot.islive)
				{
					g.draw3DRect(enemyShot.x, enemyShot.y, 1, 1, false);
				}
				
			}
			}
		}
		
	}
	
	//�жϵ��˵��ӵ��Ƿ������
	public void hitme(){
		//ȡ��ÿһ�����˵�tank
		for (int i = 0; i < this.ets.size(); i++) {
			//ȡ��̹��
			EnemyTank et=ets.get(i);
			//ȡ��ÿ���ӵ�
			for (int j = 0; j < et.ss.size(); j++) {
				//ȡ���ӵ�
				Shot enemyShot=et.ss.get(j);
				if(hero.islive)
				{
				this.hitTank2(enemyShot, hero);
				
				}
			}
		}
		
	}
	
	//�ж��ҵ��ӵ��Ƿ���е���̹��
	public void hitEnemyTank(){
		//�ж��Ƿ���е��˵�̹��
		for (int i = 0; i < hero.ss.size(); i++) 
		{
			//ȡ���ӵ�
			Shot myShot=hero.ss.get(i);
			//�ж��ӵ��Ƿ���Ч
			if(myShot.islive)
			{
				//ȡ��ÿ��̹����֮ƥ��
				for (int j = 0; j < ets.size(); j++) 
				{
					//ȡ��̹��
					EnemyTank et=ets.get(j);
					//�ж�̹���Ƿ���Ч
					if(et.islive)
					{
						this.hitTank1(myShot, et);
					
					}
				}
			}
	}
		
	}
	
	//дһ������ר���ж��ӵ��Ƿ���е���
	public void hitTank1(Shot s,Tank et)
	{
		//�ж�̹�˵ķ���
		switch(et.direct)
		{
		//�������̹�������ϻ�����
		case 0:
		case 2:
			if(s.x>et.x&&s.x<et.x+20&&s.y>et.y&&s.y<et.y+30)
			{
				//����
				//�ӵ�����
				s.islive=false;
				//����̹������
				et.islive=false;
				Recorder.reduce();
				Recorder.addenum();
				//����һ��ը��
				Bomb b=new Bomb(et.x,et.y);
			bombs.add(b);
				
			}
			break;
			//���һ���
		case 1:
		case 3:
			if(s.x>et.x&&s.x<et.x+30&&s.y>et.y&&s.y<et.y+20)
			{
				//����
				//�ӵ�����
				s.islive=false;
				//����̹������
				et.islive=false;
				Recorder.reduce();
				Recorder.addenum();
				//����һ��ը��
			Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
			}
			break;
		}
	}
	
	public void hitTank2(Shot s,Tank et)
	{
		//�ж�̹�˵ķ���
		switch(et.direct)
		{
		//�������̹�������ϻ�����
		case 0:
		case 2:
			if(s.x>et.x&&s.x<et.x+20&&s.y>et.y&&s.y<et.y+30)
			{
				//����
				//�ӵ�����
				s.islive=false;
				//����̹������
				et.islive=false;
				Recorder.lostlife();
				
				//����һ��ը��
				Bomb b=new Bomb(et.x,et.y);
			bombs.add(b);
				
			}
			break;
			//���һ���
		case 1:
		case 3:
			if(s.x>et.x&&s.x<et.x+30&&s.y>et.y&&s.y<et.y+20)
			{
				//����
				//�ӵ�����
				s.islive=false;
				//����̹������
				et.islive=false;
				Recorder.lostlife();
				//����һ��ը��
			Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
			}
			break;
		}
	}
	
	//����̹�˵ĺ���
	public void drawTank(int x,int y,Graphics g,int direct,int type){
		switch(type)
		{
		case 0:
			g.setColor(Color.CYAN);
			break;
		case 1:
			g.setColor(Color.yellow);
			break;
		}
		switch(direct)
		{
		//����
		case 0:
			
			
			//������ߵľ���
			g.fill3DRect(x,y,5,30,false);
			//�����ұ߾���
			g.fill3DRect(x+15,y,5,30,false);
			//�м����
			g.fill3DRect(x+5,y+5,10,20,false);
			//����Բ��
			g.fillOval(x+5, y+10,10, 10);
			//������
			g.drawLine(x+10, y+15,x+10, y);
			break;
		//����
		case 1:
			//��������ľ���
			g.fill3DRect(x, y, 30, 5,false);
			//��������ľ���
			g.fill3DRect(x, y+15, 30, 5, false);
			//�����м�ľ���
			g.fill3DRect(x+5, y+5, 20, 10, false);
			//����Բ��
			g.fillOval(x+10, y+5, 10, 10);
			//������
			g.drawLine(x+15, y+10, x+30, y+10);
			break;
//����
		case 2:
			
			
			//������ߵľ���
			g.fill3DRect(x,y,5,30,false);
			//�����ұ߾���
			g.fill3DRect(x+15,y,5,30,false);
			//�м����
			g.fill3DRect(x+5,y+5,10,20,false);
			//����Բ��
			g.fillOval(x+5, y+10,10, 10);
			//������
			g.drawLine(x+10, y+15,x+10, y+30);
			break;
			//����
					case 3:
						//��������ľ���
						g.fill3DRect(x, y, 30, 5,false);
						//��������ľ���
						g.fill3DRect(x, y+15, 30, 5, false);
						//�����м�ľ���
						g.fill3DRect(x+5, y+5, 20, 10, false);
						//����Բ��
						g.fillOval(x+10, y+5, 10, 10);
						//������
						g.drawLine(x+15, y+10, x, y+10);
						break;
		}
	}
	
	public void keyTyped(KeyEvent e) {
		
	
	}
	
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_W)
		{
			//�����ҵ�̹�˷���
			this.hero.setDirect(0);
			this.hero.moveUp();
		}else if(e.getKeyCode()==KeyEvent.VK_D)
		{
			//����
			this.hero.setDirect(1);
			this.hero.moveRight();
		}else if(e.getKeyCode()==KeyEvent.VK_S)
		{
			//����
			this.hero.setDirect(2);
			this.hero.moveDown();
		}else if(e.getKeyCode()==KeyEvent.VK_A)
		{
			//����
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}
		//�ж�����Ƿ���j��
		if(e.getKeyCode()==KeyEvent.VK_J)
		{
			if(this.hero.ss.size()<=100)
			{
			this.hero.fire();
			}
		}
	this.repaint();
	}
	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//ÿ��100�����ػ�
		while(true)
		{
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
			this.hitEnemyTank();
			this.hitme();
			//�ػ�
			this.repaint();
		}
	}
}

