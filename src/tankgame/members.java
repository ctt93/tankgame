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
	  //���ǻ���
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

//��¼�࣬ͬʱҲ���Ա����������
class Recorder
{
	//��¼ÿ���ж��ٵ���
	private static int enum1=20;
	//��¼�ܹ������˶��ٵ���
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

	//����һ��ٵ��˵��������浽�ļ�
	public static void keepRecording(){
		try {
			//����
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			bw.write(enum2+"\r\n");
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			//�ر���
			try {
				//���ȹ�
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
			//����
			fw=new FileWriter("d:\\myRecording.txt");
			bw=new BufferedWriter(fw);
			bw.write(enum2+"\r\n");
			//���浱ǰ���ŵ�����ͷ���
			for (int i = 0; i < ets.size(); i++) {
				//ȡ����һ��
				EnemyTank et=ets.get(i);
				if(et.islive)
				{
					String record=et.x+" "+et.y+" "+et.direct;
					//д��
					bw.write(record+"\r\n"); 
				}
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			//�ر���
			try {
				//���ȹ�
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
	//���ļ��ж�ȡ��¼
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
			//�ȶ���һ��
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
	//�������ж��ٿ��õ���
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
	//���ٵ�����
	public static void reduce(){
		enum1--;
	}
	//���������
	public static void addenum(){
		enum2++;
	}
	
	public static void lostlife(){
		mylife--;
	}
	
}

//ը����
class Bomb
{
	int x;
	int y;
	//ը��������
	int life=9;
	boolean islive=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	//��������ֵ
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



//�ӵ���
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
				//��
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
			//�ӵ���ʱ����
			if(x<0||x>400||y<0||y>300)
			{
				this.islive=false;
				break;
			}
		}
	}
}

//̹����
class Tank
{
	//��ʾ̹�˺�����
	int x=0;
	//��ʾ������
	int y=0;
	//̹�˷���,0��ʾ��,1��ʾ�ң�2��ʾ�£�3��ʾ��
	int direct=0;
	//̹���ٶ�
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
//����̹��
class EnemyTank extends Tank implements Runnable
{
	Vector<Shot> ss=new Vector<Shot>();
	
	int times=0;
	//����һ������������ ���ʵ�MyPanel�����е��˵�̹��
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	
	public EnemyTank(int x,int y)
	{
		super(x,y);
	}

	//�õ�MyPanel�ĵ���̹������
	public void setEts(Vector<EnemyTank> vv){
		this.ets=vv;
	}
	
	//�ж��Ƿ������˱�ĵ���̹��
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
						//û���ӵ�
						//���
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
			//��̹���������һ���µķ���
			this.direct=(int)(Math.random()*4);
			
			if(this.islive==false)
			{
				//̹���������˳��߳�
				break;
			}
			
		}
	}
}

//�ҵ�̹��
class Hero extends Tank 
{
	//�ӵ�
	//Shot s=null;
	Vector<Shot> ss=new Vector<Shot>();
	Shot s=null;
	
	public Hero(int x,int y)
	{
		super(x,y);
	}
	
	
	
	
	//����
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
		//�����ӵ��߳�
		Thread t=new Thread(s);
		t.start();
		
	}
	
	//̹�������ƶ�
	public void moveUp()
	{if(y>0)
		y-=speed;
	}
	//����
	public void moveRight()
	{
		if(x<400)
		x+=speed;
	}
	//����
	public void moveDown(){
		if(y<300)
		y+=speed;
	}
	//����
	public void moveLeft(){
		if(x>0)
		x-=speed;
	}
}