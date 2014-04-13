import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class ControlServer {

	private static Socket mSocket;
	public static int desktopWidth = 0;
	public static int desktopHeight = 0;
	
	
	
	public static void main(String[] args) throws AWTException, InterruptedException, IOException {
		
		desktopWidth = Toolkit.getDefaultToolkit().getScreenSize().width; //桌面的宽

		desktopHeight = Toolkit.getDefaultToolkit().getScreenSize().height;  //桌面的高
		
		int a = 10;
		int b = 15;
		
		a = b + (b = a) * 0;
		System.out.println(a +" "+ b);
		
		
		
		ServerSocket ss = new ServerSocket(40000);
		while(true){
			mSocket = ss.accept();//此代码会一直阻塞直到有客户端发送来连接请求
			System.out.println("连接成功");
			new Thread(new ServerThread(mSocket)).start();//开启一个线程处理来自客户端的数据
			
			/*if(mSocket != null){//加入已经接收到一个客户端请求则不再接收其他客户端请求
				break;
			}*/
		}	
	}
	
	
	
	
}

 class ServerThread implements Runnable{
	 private Socket mScoket;
	 private BufferedReader br;
	 private Robot rr = null;
	 
	public ServerThread(Socket mSocket) throws IOException, AWTException{
		this.mScoket = mSocket;
		this.br = new BufferedReader(new InputStreamReader(this.mScoket.getInputStream()));
		rr = new Robot();
	}
	
	private void previousTab(){//显示上一个标签页
		
	}
	
	private void nextTab(){//显示下一个标签页
		
	}
	
	private void closeTab(){//关闭当前标签页
		
	}
	
	private void minWindow(){//最小化当前窗口
		
	}
	
	private void maxWindow(){//最大化当前窗口
		
	}
	
	private void showDesktop(){//显示桌面
		
	}
	 
	public void run(){
		String content = null;
		Point point = null;
				
		try {
			while((content = this.br.readLine()) != null){
				//根据传送过来的信息进行相对应的操作
//				System.out.println(content);
//				//测试
//				r.mousePress(InputEvent.BUTTON1_MASK);
//				r.mouseRelease(InputEvent.BUTTON1_MASK);
				
				if(content.length() != 1){//如果是移动指令(注：注了移动指令:"0|1 x y"以外，其它的指令都是一个字符长度)
					String[] moveInfo = content.split(" ");
					//System.out.println(moveInfo[0]+" "+moveInfo[1]+" "+moveInfo[2]);
					int[] moveOrder = new int[3];
					
					moveOrder[0] = Integer.parseInt(moveInfo[0]);
					moveOrder[1] = (int)Float.parseFloat(moveInfo[1]);//x轴向移动的距离
					moveOrder[2] = (int)Float.parseFloat(moveInfo[2]);//y轴向移动的距离
					point = MouseInfo.getPointerInfo().getLocation();//得到当前鼠标的屏幕坐标
					int mouse_x = (int)point.getX(); 
					int mouse_y = (int)point.getY();
					if(moveOrder[0] == 0){
						int move_x = moveOrder[1] + mouse_x;
						int move_y = moveOrder[2] + mouse_y;
						if(move_x > 0 && move_y > 0 && move_x < ControlServer.desktopWidth && move_y < ControlServer.desktopHeight){
							rr.mouseMove(move_x,move_y);//移动鼠标
						}
					}else if(moveOrder[0] == 1){
						rr.mouseWheel(moveOrder[1]);
					}
					
				}else{
					//发送的信息
					//0.移动鼠标的信息：0 , velocityX , velocityY 
					//1.按下鼠标左键的信息:	1
					//2.抬起鼠标左键的信息: 2
					//3.按下鼠标右键的信息: 3
					//4.抬起鼠标右键的信息: 4
					//5.最小化窗口的信息:10
					//6.最大化窗口：11
					//7.到前一标签页:12
					//8.到后一标签页:13
					//9.关闭当前标签页:14
					//10.显示桌面：15			
					int a = Integer.parseInt(content);//将指令转换成整形变量,这里提示java的版本原因不能在switch()的括号中填写字符串变量
					
					switch(a){
					case 1:
						rr.mousePress(InputEvent.BUTTON1_MASK);
						break;
					case 2:
						rr.mouseRelease(InputEvent.BUTTON1_MASK);
						break;
					case 3:
						rr.mousePress(InputEvent.BUTTON3_MASK);
						break;
					case 4:
						rr.mouseRelease(InputEvent.BUTTON3_MASK);
						break;
					case 12:
						previousTab();
						break;
					case 13:
						nextTab();
						break;
					case 14:
						closeTab();
						break;
					case 15:
						showDesktop();
						break;
					case 10:
						minWindow();
						break;
					case 11:
						maxWindow();
						break;
					}
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


