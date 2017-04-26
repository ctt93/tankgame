package tankgame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class MyTankGame1 extends JFrame implements ActionListener{
MyPanel mp=null;

//定义一个开始面板
MyStartPanel msp=null;

//做出我需要的菜单
JMenuBar jmb=null;
//开始游戏
JMenu jm1=null;
JMenuItem jmi1=null;
JMenuItem jmi2=null;
JMenuItem jmi3=null;
JMenuItem jmi4=null;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
MyTankGame1 mtg=new MyTankGame1();
	}
//构造函数
	public MyTankGame1()
	{
		
		//mp=new MyPanel();
		//启动mp线程
		//Thread t=new Thread(mp);
		//t.start();
		
		//this.add(mp);
		
		//this.addKeyListener(mp);
		
		//创建菜单及菜单选项
		jmb=new JMenuBar();
		jm1= new JMenu("游戏（G）");
		//设置快捷方式
		jm1.setMnemonic('G');
		jmi1=new JMenuItem("开始新游戏（M）");
		//对jmi1响应
		jmi1.addActionListener(this);
		jmi1.setActionCommand("newgame");
		jm1.add(jmi1);
		
		
		jmi2=new JMenuItem("退出（B）");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("exit");
		jm1.add(jmi2);
		
		jmi3=new JMenuItem("存盘退出（C）");
		jmi3.addActionListener(this);
		jmi3.setActionCommand("saveexit");
		jm1.add(jmi3);
		
		jmi4=new JMenuItem("继续上一局游戏（S）");
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
	//对用户不同的点击做出不同的处理
	if(arg0.getActionCommand().equals("newgame"))
	{
		//创建战场
		mp=new MyPanel("newgame");
				//启动mp线程
				Thread t=new Thread(mp);
				t.start();
				
				//先删除旧的面板
				this.remove(msp);
				this.add(mp);
				
				this.addKeyListener(mp);
				this.setVisible(true); 
	}
	else if(arg0.getActionCommand().equals("exit"))
	{
	 //用户点击了退出系统的菜单
		//保存击毁敌人的数量
		Recorder.keepRecording();
		
		System.exit(0);
	}else if(arg0.getActionCommand().equals("saveexit"))
	{
		Recorder.setEts(mp.ets);
        Recorder.keepRecorderandEnenmyTank();
		
		System.exit(0);
	}else if(arg0.getActionCommand().equals("congame")){
		//创建战场
				mp=new MyPanel("con");
						//启动mp线程
						Thread t=new Thread(mp);
						t.start();
						
						//先删除旧的面板
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
		//提示信息
		if(times%2==0)
		{
		g.setColor(Color.yellow);
		//开关信息的字体
		Font myFont=new Font("华文新魏", Font.BOLD, 30);
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


//我的面板
class MyPanel extends JPanel implements KeyListener,Runnable
{
	//定义一个我的坦克
	Hero hero=null;
	//定义敌人坦克组
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	int enSize=3;
	//定义炸弹集合
	Vector<Bomb> bombs=new Vector<Bomb>();
	Vector<Node> nodes=new Vector<Node>();
	//定义三张图片,三张图片组成一颗炸弹
	Image image1=null;
	Image image2=null;
	Image image3=null;
	
	//构造函数
	public MyPanel(String flag)
	{
		//恢复记录
		Recorder.getRecording();
		
		hero=new Hero(100,100);
		
		if(flag=="newgame")
		{	//初始化敌人坦克
		for(int i=0;i<enSize;i++)
		{
			//创建敌人坦克对象
			EnemyTank et=new EnemyTank((i+1)*50,0);
			et.setColor(0);
			et.setDirect(2);
			
			et.setEts(ets);
			//启动敌人坦克
			Thread t=new Thread(et);
			t.start();
//			//给敌人坦克添加一颗子弹
//			Shot s=new Shot(et.x+10, et.y+30, 2);
//			et.ss.add(s);
//			Thread t2=new Thread(s);
//			t2.start();
			//加入
			ets.add(et);
		}
		}else 
		{
			 nodes=new Recorder().getnodes();
			 System.out.println(nodes.size());
			for(int i=0;i<nodes.size();i++)
			{
				Node node=nodes.get(i);
				//创建敌人坦克对象
				EnemyTank et=new EnemyTank(node.x,node.y);
				et.setColor(0);
				et.setDirect(node.direct);
				
				et.setEts(ets);
				//启动敌人坦克
				Thread t=new Thread(et);
				t.start();
//				//给敌人坦克添加一颗子弹
//				Shot s=new Shot(et.x+10, et.y+30, 2);
//				et.ss.add(s);
//				Thread t2=new Thread(s);
//				t2.start();
				//加入
				ets.add(et);
			}
		}
		//初始化图片
		try {
			image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/1.jpg"));
			image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/2.jpg"));
			image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/3.jpg"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		//播放开战声音
		PlaySounds apw=new PlaySounds("d:\\120.wav");
		apw.start();
	
	}
	
	public void showInfo(Graphics g){
		//画出提示信息坦克
				this.drawTank(80, 330, g, 0, 0);
				g.setColor(Color.black);
				g.drawString(Recorder.getEnum1()+"", 110, 350);
				this.drawTank(130, 330, g, 0, 1);
				g.setColor(Color.black);
				g.drawString(Recorder.getMylife()+"", 160, 350);
				
				//画出玩家的总成绩
				g.setColor(Color.black);
				Font f=new Font("宋体", Font.BOLD, 20);
				g.setFont(f);
				g.drawString("您的总成绩", 420, 30);
				
				this.drawTank(420, 60, g, 0, 0);
				g.setColor(Color.black);
				g.drawString(" * "+Recorder.getEnum2(), 450, 80);
				
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fillRect(0,0,400,300);
		
		//画出提示信息
		this.showInfo(g);
		
		//画出自己的坦克
		if(hero.islive)
		{
		this.drawTank(hero.getX(), hero.getY(), g, this.hero.direct, 1);
		}
		//从ss中取出所有子弹
		for(int i=0;i<this.hero.ss.size();i++)
		{
			Shot myShot=hero.ss.get(i);
		//画出子弹,画出一颗子弹
		if(myShot!=null&&myShot.islive==true)
		{
			g.draw3DRect(myShot.x, myShot.y, 1, 1, false);
		}
		if(myShot.islive==false)
		{
			hero.ss.remove(myShot);
		}
		}
		
		//画出炸弹
		for (int i = 0; i < bombs.size(); i++) 
		{
			//取出炸弹
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
		
		//画出敌人坦克
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			//判断坦克是否有效
			if(et.islive)
			{
			this.drawTank(et.getX(), et.getY(), g, et.getDirect(), 0);
			//再画出敌人的子弹
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
	
	//判断敌人的子弹是否击中我
	public void hitme(){
		//取出每一个敌人的tank
		for (int i = 0; i < this.ets.size(); i++) {
			//取出坦克
			EnemyTank et=ets.get(i);
			//取出每颗子弹
			for (int j = 0; j < et.ss.size(); j++) {
				//取出子弹
				Shot enemyShot=et.ss.get(j);
				if(hero.islive)
				{
				this.hitTank2(enemyShot, hero);
				
				}
			}
		}
		
	}
	
	//判断我的子弹是否击中敌人坦克
	public void hitEnemyTank(){
		//判断是否击中敌人的坦克
		for (int i = 0; i < hero.ss.size(); i++) 
		{
			//取出子弹
			Shot myShot=hero.ss.get(i);
			//判断子弹是否有效
			if(myShot.islive)
			{
				//取出每个坦克与之匹配
				for (int j = 0; j < ets.size(); j++) 
				{
					//取出坦克
					EnemyTank et=ets.get(j);
					//判断坦克是否有效
					if(et.islive)
					{
						this.hitTank1(myShot, et);
					
					}
				}
			}
	}
		
	}
	
	//写一个函数专门判断子弹是否击中敌人
	public void hitTank1(Shot s,Tank et)
	{
		//判断坦克的方向
		switch(et.direct)
		{
		//如果敌人坦克是向上或者下
		case 0:
		case 2:
			if(s.x>et.x&&s.x<et.x+20&&s.y>et.y&&s.y<et.y+30)
			{
				//击中
				//子弹死亡
				s.islive=false;
				//敌人坦克死亡
				et.islive=false;
				Recorder.reduce();
				Recorder.addenum();
				//创建一颗炸弹
				Bomb b=new Bomb(et.x,et.y);
			bombs.add(b);
				
			}
			break;
			//向右或左
		case 1:
		case 3:
			if(s.x>et.x&&s.x<et.x+30&&s.y>et.y&&s.y<et.y+20)
			{
				//击中
				//子弹死亡
				s.islive=false;
				//敌人坦克死亡
				et.islive=false;
				Recorder.reduce();
				Recorder.addenum();
				//创建一颗炸弹
			Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
			}
			break;
		}
	}
	
	public void hitTank2(Shot s,Tank et)
	{
		//判断坦克的方向
		switch(et.direct)
		{
		//如果敌人坦克是向上或者下
		case 0:
		case 2:
			if(s.x>et.x&&s.x<et.x+20&&s.y>et.y&&s.y<et.y+30)
			{
				//击中
				//子弹死亡
				s.islive=false;
				//敌人坦克死亡
				et.islive=false;
				Recorder.lostlife();
				
				//创建一颗炸弹
				Bomb b=new Bomb(et.x,et.y);
			bombs.add(b);
				
			}
			break;
			//向右或左
		case 1:
		case 3:
			if(s.x>et.x&&s.x<et.x+30&&s.y>et.y&&s.y<et.y+20)
			{
				//击中
				//子弹死亡
				s.islive=false;
				//敌人坦克死亡
				et.islive=false;
				Recorder.lostlife();
				//创建一颗炸弹
			Bomb b=new Bomb(et.x,et.y);
				bombs.add(b);
			}
			break;
		}
	}
	
	//画出坦克的函数
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
		//向上
		case 0:
			
			
			//画出左边的矩形
			g.fill3DRect(x,y,5,30,false);
			//画出右边矩形
			g.fill3DRect(x+15,y,5,30,false);
			//中间矩形
			g.fill3DRect(x+5,y+5,10,20,false);
			//画出圆形
			g.fillOval(x+5, y+10,10, 10);
			//画出线
			g.drawLine(x+10, y+15,x+10, y);
			break;
		//向右
		case 1:
			//画出上面的矩形
			g.fill3DRect(x, y, 30, 5,false);
			//画出下面的矩形
			g.fill3DRect(x, y+15, 30, 5, false);
			//画出中间的矩形
			g.fill3DRect(x+5, y+5, 20, 10, false);
			//画出圆形
			g.fillOval(x+10, y+5, 10, 10);
			//画出线
			g.drawLine(x+15, y+10, x+30, y+10);
			break;
//向下
		case 2:
			
			
			//画出左边的矩形
			g.fill3DRect(x,y,5,30,false);
			//画出右边矩形
			g.fill3DRect(x+15,y,5,30,false);
			//中间矩形
			g.fill3DRect(x+5,y+5,10,20,false);
			//画出圆形
			g.fillOval(x+5, y+10,10, 10);
			//画出线
			g.drawLine(x+10, y+15,x+10, y+30);
			break;
			//向左
					case 3:
						//画出上面的矩形
						g.fill3DRect(x, y, 30, 5,false);
						//画出下面的矩形
						g.fill3DRect(x, y+15, 30, 5, false);
						//画出中间的矩形
						g.fill3DRect(x+5, y+5, 20, 10, false);
						//画出圆形
						g.fillOval(x+10, y+5, 10, 10);
						//画出线
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
			//设置我的坦克方向
			this.hero.setDirect(0);
			this.hero.moveUp();
		}else if(e.getKeyCode()==KeyEvent.VK_D)
		{
			//向右
			this.hero.setDirect(1);
			this.hero.moveRight();
		}else if(e.getKeyCode()==KeyEvent.VK_S)
		{
			//向下
			this.hero.setDirect(2);
			this.hero.moveDown();
		}else if(e.getKeyCode()==KeyEvent.VK_A)
		{
			//向左
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}
		//判断玩家是否按下j键
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
		//每隔100毫秒重画
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
			//重画
			this.repaint();
		}
	}
}

