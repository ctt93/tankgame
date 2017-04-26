package tankgame;

import java.util.*;

import javax.sound.sampled.*;

import java.io.*;

class PlaySounds extends Thread {
	 
	 private String filename;
	 public PlaySounds(String wavfile) {

	  filename = wavfile;
	 }
	 public void run() {
	 
	  File soundFile = new File(filename);
	 
	  AudioInputStream audioInputStream = null;
	  try {
	   audioInputStream = AudioSystem.getAudioInputStream(soundFile);
	  } catch (Exception e1) {
	   e1.printStackTrace();
	   return;
	  }
	  AudioFormat format = audioInputStream.getFormat();
	  SourceDataLine auline = null;
	  DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
	  try {
	   auline = (SourceDataLine) AudioSystem.getLine(info);
	   auline.open(format);
	  } catch (Exception e) {
	   e.printStackTrace();
	   return;
	  }
	  auline.start();
	  int nBytesRead = 0;
	  //这是缓冲
	  byte[] abData = new byte[512];
	  try {
	   while (nBytesRead != -1) {
	    nBytesRead = audioInputStream.read(abData, 0, abData.length);
	    if (nBytesRead >= 0)
	     auline.write(abData, 0, nBytesRead);
	   }
	  } catch (IOException e) {
	   e.printStackTrace();
	   return;
	  } finally {
	   auline.drain();
	   auline.close();
	  }
	 } 
	}




class Node
{
	int x;
	int y;
	int direct;
	public Node(int x,int y,int direct){
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
}

//记录类，同时也可以保存玩家设置
class Recorder
{
	//记录每关有多少敌人
	private static int enum1=20;
	//记录总共消灭了多少敌人
	private static int enum2=0;
	
	private static FileWriter fw=null;
	private static BufferedWriter bw=null;
	private static Vector<EnemyTank> ets=new Vector<EnemyTank>();
	private static Vector<Node> nodes=new Vector<Node>();
	
	

	public static Vector<EnemyTank> getEts() {
		return ets;
	}

	public static void setEts(Vector<EnemyTank> ets) {
		Recorder.ets = ets;
	}

	//把玩家击毁敌人的数量保存到文件
	public static void keepRecording(){
		try {
			//创建
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			bw.write(enum2+"\r\n");
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			//关闭流
			try {
				//后开先关
				bw.close();
				fw.close();
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public static void keepRecorderandEnenmyTank(){
		try {
			//创建
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			bw.write(enum2+"\r\n");
			//保存当前活着的坐标和方向
			for (int i = 0; i < ets.size(); i++) {
				//取出第一个
				EnemyTank et=ets.get(i);
				if(et.islive)
				{
					String record=et.x+" "+et.y+" "+et.direct;
					//写入
					bw.write(record+"\r\n"); 
				}
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			//关闭流
			try {
				//后开先关
				bw.close();
				fw.close();
				
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	private static FileReader fr=null;
	private static BufferedReader br=null;
	//从文件中读取记录
	public static void getRecording(){
		try {
			fr=new FileReader("d:\\myRecording.txt");
			br=new BufferedReader(fr);
			String n=br.readLine();
			enum2=Integer.parseInt(n);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public Vector<Node> getnodes(){
		try {
			fr=new FileReader("d:\\myRecording.txt");
			br=new BufferedReader(fr);
			String n="";
			//先读第一行
			n=br.readLine();
			enum2=Integer.parseInt(n);
			while((n=br.readLine())!=null)
			{
				String []xyz=n.split(" ");
				
				Node node=new Node(Integer.parseInt(xyz[0]), Integer.parseInt(xyz[1]), Integer.parseInt(xyz[2]));
				nodes.add(node);
				
			}
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return nodes;
	}
	
	public static int getEnum2() {
		return enum2;
	}
	public static void setEnum2(int enum2) {
		Recorder.enum2 = enum2;
	}
	//设置我有多少可用的人
	private static int mylife=3;
	
	public static int getEnum1() {
		return enum1;
	}
	public static void setEnum1(int enum1) {
		Recorder.enum1 = enum1;
	}
	public static int getMylife() {
		return mylife;
	}
	public static void setMylife(int mylife) {
		Recorder.mylife = mylife;
	}
	//减少敌人数
	public static void reduce(){
		enum1--;
	}
	//消灭敌人数
	public static void addenum(){
		enum2++;
	}
	
	public static void lostlife(){
		mylife--;
	}
	
}

//炸弹类
class Bomb
{
	int x;
	int y;
	//炸弹的生命
	int life=9;
	boolean islive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	//减少生命值
	public void lifedown()
	{
		if(life>0)
		{
			life--;
		}else{
			this.islive=false;
		}
	}
}



//子弹类
class Shot implements Runnable
{
	int x;
	int y;
	int direct;
	int speed=2;
	boolean islive=true;
	public Shot(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				// TODO: handle exception
			}
			switch(direct)
			{
			case 0:
				//上
				y-=speed;
				break;
			case 1:
				x+=speed;
				break;
			case 2:
				y+=speed;
				break;
			case 3:
				x-=speed;
				break;
			}
			//子弹何时死亡
			if(x<0||x>400||y<0||y>300)
			{
				this.islive=false;
				break;
			}
		}
	}
}

//坦克类
class Tank
{
	//表示坦克横坐标
	int x=0;
	//表示纵坐标
	int y=0;
	//坦克方向,0表示上,1表示右，2表示下，3表示左
	int direct=0;
	//坦克速度
	int speed=1;
	
	boolean islive=true;
	int color;
	 
	

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	
	
	public Tank(int x,int y){
		this.x=x;
		this.y=y;
	}
}
//敌人坦克
class EnemyTank extends Tank implements Runnable
{
	Vector<Shot> ss=new Vector<Shot>();
	
	int times=0;
	//定义一个向量，可以 访问到MyPanel上所有敌人的坦克
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	
	public EnemyTank(int x,int y)
	{
		super(x,y);
	}

	//得到MyPanel的敌人坦克向量
	public void setEts(Vector<EnemyTank> vv){
		this.ets=vv;
	}
	
	//判断是否碰到了别的敌人坦克
	public boolean isToachOther(){
		boolean b= false;
		switch (this.direct) {
		case 0:
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et=ets.get(i);
				if(et!=this){
					if(et.direct==0||et.direct==2)
					{
						if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return true;
						}
						if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return true;
						}
					}
					if(et.direct==1||et.direct==3)
					{
						if(this.x>=et.x&&this.x<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
						{
							return true;
						}
						if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
						{
							return true;
						}
					}
				}
			}
			break;
		case 1:
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et=ets.get(i);
				if(et!=this){
					if(et.direct==0||et.direct==2)
					{
						if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return true;
						}
						if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y+20>=et.y&&this.y+20<=et.y+30)
						{
							return true;
						}
					}
					if(et.direct==1||et.direct==3)
					{
						if(this.x+30>=et.x&&this.x+30<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
						{
							return true;
						}
						if(this.x+30>=et.x&&this.x+30<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20)
						{
							return true;
						}
					}
				}
			}
			break;
		case 2:
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et=ets.get(i);
				if(et!=this){
					if(et.direct==0||et.direct==2)
					{
						if(this.x>=et.x&&this.x<=et.x+20&&this.y+30>=et.y&&this.y+30<=et.y+30)
						{
							return true;
						}
						if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y+30>=et.y&&this.y+30<=et.y+30)
						{
							return true;
						}
					}
					if(et.direct==1||et.direct==3)
					{
						if(this.x>=et.x&&this.x<=et.x+30&&this.y+30>=et.y&&this.y+30<=et.y+20)
						{
							return true;
						}
						if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y+30>=et.y&&this.y+30<=et.y+20)
						{
							return true;
						}
					}
				}
			}
			break;
		case 3:
			for (int i = 0; i < ets.size(); i++) {
				EnemyTank et=ets.get(i);
				if(et!=this){
					if(et.direct==0||et.direct==2)
					{
						if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return true;
						}
						if(this.x>=et.x&&this.x<=et.x+20&&this.y+20>=et.y&&this.y+20<=et.y+30)
						{
							return true;
						}
					}
					if(et.direct==1||et.direct==3)
					{
						if(this.x>=et.x&&this.x<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
						{
							return true;
						}
						if(this.x>=et.x&&this.x<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20)
						{
							return true;
						}
					}
				}
			}
			break;
			
		}
		
		
		return b;
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			
			switch(this.direct)
			{
			case 0:
				for (int i = 0; i <30; i++) {
					if(y>0&&!this.isToachOther()){
						y-=speed;
					}
					
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				break;
			case 1:
				for (int i = 0; i < 30; i++) {
					if(x<400&&!this.isToachOther())
					{
						x+=speed;
					}
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				
				break;
			case 2:
				for (int i = 0; i < 30; i++) {
					if(y<300&&!this.isToachOther())
					{
						y+=speed;
					}
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				
				break;
			case 3:
				for (int i = 0; i < 30; i++) {
					if(x>0&&!this.isToachOther())
					{
						x-=speed;
					}
					try {
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				
				break;
			}
			
			this.times++;
			
			if(times%2==0)
			{
				if(islive)
				{
					if(ss.size()<100)
					{
						Shot s=null;
						//没有子弹
						//添加
						switch(direct)
						{
						case 0:
							s=new Shot(x+10, y, 0);
							ss.add(s);
							break;
						case 1:
							s=new Shot(x+30, y+10, 1);
							ss.add(s);
							break;
						case 2:
							s=new Shot(x+10, y+30, 2);
							ss.add(s);
							break;
						case 3:
							s=new Shot(x, y+10, 3);
							ss.add(s);
							break;
							
							
						}
						Thread t=new Thread(s);
						t.start();
						
					}
				}
			}
			//让坦克随机产生一个新的方向
			this.direct=(int)(Math.random()*4);
			
			if(this.islive==false)
			{
				//坦克死亡，退出线程
				break;
			}
			
		}
	}
}

//我的坦克
class Hero extends Tank 
{
	//子弹
	//Shot s=null;
	Vector<Shot> ss=new Vector<Shot>();
	Shot s=null;
	
	public Hero(int x,int y)
	{
		super(x,y);
	}
	
	
	
	
	//开火
	public void fire()
	{
		
		switch(this.direct)
		{
		case 0:
			s=new Shot(x+10, y,0);
			ss.add(s);
			break;
		case 1:
			s=new Shot(x+30, y+10,1);
			ss.add(s);
			break;
		case 2:
			s=new Shot(x+10, y+30,2);
			ss.add(s);
			break;
		case 3:
			s=new Shot(x, y+10,3);
			ss.add(s);
			break;
		}
		//启动子弹线程
		Thread t=new Thread(s);
		t.start();
		
	}
	
	//坦克向上移动
	public void moveUp()
	{if(y>0)
		y-=speed;
	}
	//向右
	public void moveRight()
	{
		if(x<400)
		x+=speed;
	}
	//向下
	public void moveDown(){
		if(y<300)
		y+=speed;
	}
	//向左
	public void moveLeft(){
		if(x>0)
		x-=speed;
	}
}