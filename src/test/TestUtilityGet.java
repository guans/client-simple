package test;

import coordinate.TwoDCoordinate;
import test.TestServerSend.Node;
import ubimessage.client.MOMClient;
import ubimessage.message.Message;
import ubimessage.message.MessageListener;

/**
 * ��Ҫ���ܣ��ƶ��˻�ȡ��Ϣ
 * 
 */

public class TestUtilityGet {
	
	
	//---------------------------------------------------------------------------------------------
	// �ڵ���
				public static class Node {
					/**
					 * number �������
					 */
					int number;

					/**
					 * type �������
					 */
					int type;

					/**
					 * coor_x ���x����
					 */
					double coor_x;

					/**
					 * coor_y ���y����
					 */
					double coor_y;

					/**
					 * coor_z ���z����
					 */
					double coor_z;

					/**
					 * weight �洢����Ȩ��
					 */
					double weight;

					/**
					 * next �����ڽӽ��
					 */
					Node next = null;

					public Node(double a, double b, double c) {

						coor_x = a;
						coor_y = b;
						coor_z = c;
					}

					Node(Node p) {
						coor_x = p.coor_x;
						coor_y = p.coor_y;
						coor_z = p.coor_z;
					}
				}

	// ���㵽�ֽ�ת��   ÿ��λһ��������
		public static byte[] doubleToByte(double x,double y,double z) {
			byte[] b = new byte[24];
			long l = Double.doubleToLongBits(x);
			for (int i = 0; i < 8; i++) {
				b[i] = new Long(l).byteValue();
				l = l >> 8;

			}
			l = Double.doubleToLongBits(y);
			for (int i = 8; i < 16; i++) {
				b[i] = new Long(l).byteValue();
				l = l >> 8;

			}
			l = Double.doubleToLongBits(z);
			for (int i = 16; i < b.length; i++) {
				b[i] = new Long(l).byteValue();
				l = l >> 8;

			}
			return b;
		}

		// �ֽڵ�����ת��
		public static double[] byteToDouble(byte[] b) {
			long l[]=new long[3];
			for(int i=0;i<3;i++){
			l[0] = b[0];
			l[0] &= 0xff;
			l[0] |= ((long) b[1] << 8);
			l[0] &= 0xffff;
			l[0] |= ((long) b[2] << 16);
			l[0] &= 0xffffff;
			l[0] |= ((long) b[3] << 24);
			l[0] &= 0xffffffffl;
			l[0] |= ((long) b[4] << 32);
			l[0] &= 0xffffffffffl;

			l[0] |= ((long) b[5] << 40);
			l[0] &= 0xffffffffffffl;
			l[0] |= ((long) b[6] << 48);
			l[0] &= 0xffffffffffffffl;
			l[0] |= ((long) b[7] << 56);
			
			
			
			
			l[1] = b[8];
			l[1] &= 0xff;
			l[1] |= ((long) b[9] << 8);
			l[1] &= 0xffff;
			l[1] |= ((long) b[10] << 16);
			l[1] &= 0xffffff;
			l[1] |= ((long) b[11] << 24);
			l[1] &= 0xffffffffl;
			l[1] |= ((long) b[12] << 32);
			l[1] &= 0xffffffffffl;

			l[1] |= ((long) b[13] << 40);
			l[1] &= 0xffffffffffffl;
			l[1] |= ((long) b[14] << 48);
			l[1] &= 0xffffffffffffffl;
			l[1] |= ((long) b[15] << 56);
			
			
			
			
			l[2] = b[16];
			l[2] &= 0xff;
			l[2] |= ((long) b[17] << 8);
			l[2] &= 0xffff;
			l[2] |= ((long) b[18] << 16);
			l[2] &= 0xffffff;
			l[2] |= ((long) b[19] << 24);
			l[2] &= 0xffffffffl;
			l[2] |= ((long) b[20] << 32);
			l[2] &= 0xffffffffffl;

			l[2] |= ((long) b[21] << 40);
			l[2] &= 0xffffffffffffl;
			l[2] |= ((long) b[22] << 48);
			l[2] &= 0xffffffffffffffl;
			l[2] |= ((long) b[23] << 56);
			}
			
			double d[]=new double[3];
			d[0]=Double.longBitsToDouble(l[0]);
			d[1]=Double.longBitsToDouble(l[1]);
			d[2]=Double.longBitsToDouble(l[2]);
			return d;
		}
		// ���㵽�ֽ�ת��(���)
		public static byte[] doubleToByteSome(Node points[], int num) {

			byte b[] = new byte[24 * num];
			for (int i = 0; i < num; i++) {

				// byte[] b = new byte[24];
				long l = Double.doubleToLongBits(points[i].coor_x);
				for (int j = 0; j < 8; j++) {
					b[24 * i + j] = new Long(l).byteValue();
					l = l >> 8;
				}
				l = Double.doubleToLongBits(points[i].coor_y);
				for (int j = 8; j < 16; j++) {
					b[24 * i + j] = new Long(l).byteValue();
					l = l >> 8;

				}
				l = Double.doubleToLongBits(points[i].coor_z);
				for (int j = 16; j < 24; j++) {
					b[24 * i + j] = new Long(l).byteValue();
					l = l >> 8;
				}

			}
			return b;
		}
		// �ֽڵ�����ת������㣩
		public static Node[] byteToDoubleSome(byte[] b, int num) {
			Node nodes[]=new Node[num];
			for (int i = 0; i < num; i++) {
				
				long l[] = new long[3];
				for (int j = 0; j < 3; j++) {
					l[0] = b[24*i+0];
					l[0] &= 0xff;
					l[0] |= ((long) b[24*i+1] << 8);
					l[0] &= 0xffff;
					l[0] |= ((long) b[24*i+2] << 16);
					l[0] &= 0xffffff;
					l[0] |= ((long) b[24*i+3] << 24);
					l[0] &= 0xffffffffl;
					l[0] |= ((long) b[24*i+4] << 32);
					l[0] &= 0xffffffffffl;

					l[0] |= ((long) b[24*i+5] << 40);
					l[0] &= 0xffffffffffffl;
					l[0] |= ((long) b[24*i+6] << 48);
					l[0] &= 0xffffffffffffffl;
					l[0] |= ((long) b[24*i+7] << 56);

					l[1] = b[24*i+8];
					l[1] &= 0xff;
					l[1] |= ((long) b[24*i+9] << 8);
					l[1] &= 0xffff;
					l[1] |= ((long) b[24*i+10] << 16);
					l[1] &= 0xffffff;
					l[1] |= ((long) b[24*i+11] << 24);
					l[1] &= 0xffffffffl;
					l[1] |= ((long) b[24*i+12] << 32);
					l[1] &= 0xffffffffffl;

					l[1] |= ((long) b[24*i+13] << 40);
					l[1] &= 0xffffffffffffl;
					l[1] |= ((long) b[24*i+14] << 48);
					l[1] &= 0xffffffffffffffl;
					l[1] |= ((long) b[24*i+15] << 56);

					l[2] = b[24*i+16];
					l[2] &= 0xff;
					l[2] |= ((long) b[24*i+17] << 8);
					l[2] &= 0xffff;
					l[2] |= ((long) b[24*i+18] << 16);
					l[2] &= 0xffffff;
					l[2] |= ((long) b[24*i+19] << 24);
					l[2] &= 0xffffffffl;
					l[2] |= ((long) b[24*i+20] << 32);
					l[2] &= 0xffffffffffl;

					l[2] |= ((long) b[24*i+21] << 40);
					l[2] &= 0xffffffffffffl;
					l[2] |= ((long) b[24*i+22] << 48);
					l[2] &= 0xffffffffffffffl;
					l[2] |= ((long) b[24*i+23] << 56);
				}

				nodes[i]=new Node(Double.longBitsToDouble(l[0]),Double.longBitsToDouble(l[1]),Double.longBitsToDouble(l[2]));
				/*nodes[i].coor_x=Double.longBitsToDouble(l[0]);
				nodes[i].coor_y=Double.longBitsToDouble(l[1]);
				nodes[i].coor_z=Double.longBitsToDouble(l[2]);*/
				
				/*double d[] = new double[3];
				d[0] = Double.longBitsToDouble(l[0]);
				d[1] = Double.longBitsToDouble(l[1]);
				d[2] = Double.longBitsToDouble(l[2]);*/
				
			}
			return nodes;
		}

		//----------------------------------------------------------------------------------------------
	// �����½��֤��Ϣ
	public static boolean DoLogin(Message m) throws Exception {
		byte[] newByte1 = new byte[1];
		System.arraycopy(m.getContentAsBytes(), 0, newByte1, 0, 1);
		if (newByte1[0] == (1))
			return true;
		else
			return false;

	}

	// ����ע��״̬��Ϣ
	public static boolean DoEnroll(Message m) throws Exception {
		byte[] newByte1 = new byte[1];
		System.arraycopy(m.getContentAsBytes(), 0, newByte1, 0, 1);
		if (newByte1[0] == (1))
			return true;
		else
			return false;
	}

	// ��ȡ����λ��Ϣ
	public static TwoDCoordinate DoLocateSelf(Message m) throws Exception {
		
		double x[]=byteToDouble(m.getContentAsBytes());
		/*System.out.println((x[0]));
		System.out.println((x[1]));
		System.out.println((x[2]));*/
		TwoDCoordinate coordinate=new TwoDCoordinate();
		coordinate.setXCoordinate(x[0]);
		coordinate.setYCoordinate(x[1]);
		return coordinate;
	}

	// ��ȡ���ֵ�λ����Ϣ
	public static TwoDCoordinate DoFirePoint(Message m) throws Exception {
		double x[]=byteToDouble(m.getContentAsBytes());
		/*System.out.println((x[0]));
		System.out.println((x[1]));
		System.out.println((x[2]));*/
		TwoDCoordinate coordinate=new TwoDCoordinate();
		coordinate.setXCoordinate(x[0]);
		coordinate.setYCoordinate(x[1]);
		return coordinate;
	}

	// ��ȡҽ��������Ϣ
	public static String[] DoDocterInfo(Message m) throws Exception {
		byte[] newByte1 = new byte[8];
		byte[] newByte2 = new byte[16];
		String[] str=new String[2];
		System.arraycopy(m.getContentAsBytes(), 0, newByte1, 0, 8);
		System.arraycopy(m.getContentAsBytes(), 8, newByte2, 0, 16);
		str[0]= new String(newByte1,"GBK");//����תΪ���ġ���
		str[1]= new String(newByte2).trim();
		
		return str;	
	}

	/**
	 * ��ȡ��ѯ�����Ϣ
	 * @param m �������˽��ܵ�����Ϣ
	 * @throws Exception
	 */
	public static Place[] DoSearchPoint(Message m) throws Exception {
	 
		 int num=Integer.valueOf(m.getHeader("num")).intValue();
		 Place pl[]=new Place[num];
		 for(int i=0;i<num;i++)
		 {
			 byte[] newByte1 = new byte[16];
			 byte[] b = new byte[24];
			 System.arraycopy(m.getContentAsBytes(), i*(16 + 24), newByte1, 0, 16);
			 System.arraycopy(m.getContentAsBytes(), i*(16 + 24)+16, b, 0, 24);
			 String name= new String(newByte1,"GBK");//����תΪ���ġ���
			 pl[i]=new Place("",0,0,0);
			 pl[i].name=name.trim();
			 
			  
			 
			 long l[] = new long[3];
				for (int j = 0; j < 3; j++) {
					l[0] = b[0];
					l[0] &= 0xff;
					l[0] |= ((long) b[1] << 8);
					l[0] &= 0xffff;
					l[0] |= ((long) b[2] << 16);
					l[0] &= 0xffffff;
					l[0] |= ((long) b[3] << 24);
					l[0] &= 0xffffffffl;
					l[0] |= ((long) b[4] << 32);
					l[0] &= 0xffffffffffl;

					l[0] |= ((long) b[5] << 40);
					l[0] &= 0xffffffffffffl;
					l[0] |= ((long) b[6] << 48);
					l[0] &= 0xffffffffffffffl;
					l[0] |= ((long) b[7] << 56);

					l[1] = b[8];
					l[1] &= 0xff;
					l[1] |= ((long) b[9] << 8);
					l[1] &= 0xffff;
					l[1] |= ((long) b[10] << 16);
					l[1] &= 0xffffff;
					l[1] |= ((long) b[11] << 24);
					l[1] &= 0xffffffffl;
					l[1] |= ((long) b[12] << 32);
					l[1] &= 0xffffffffffl;

					l[1] |= ((long) b[13] << 40);
					l[1] &= 0xffffffffffffl;
					l[1] |= ((long) b[14] << 48);
					l[1] &= 0xffffffffffffffl;
					l[1] |= ((long) b[15] << 56);

					l[2] = b[16];
					l[2] &= 0xff;
					l[2] |= ((long) b[17] << 8);
					l[2] &= 0xffff;
					l[2] |= ((long) b[18] << 16);
					l[2] &= 0xffffff;
					l[2] |= ((long) b[19] << 24);
					l[2] &= 0xffffffffl;
					l[2] |= ((long) b[20] << 32);
					l[2] &= 0xffffffffffl;

					l[2] |= ((long) b[21] << 40);
					l[2] &= 0xffffffffffffl;
					l[2] |= ((long) b[22] << 48);
					l[2] &= 0xffffffffffffffl;
					l[2] |= ((long) b[23] << 56);
				}

				double d[] = new double[3];
				d[0] = Double.longBitsToDouble(l[0]);
				d[1] = Double.longBitsToDouble(l[1]);
				d[2] = Double.longBitsToDouble(l[2]);
				pl[i].x=d[0]; 
				pl[i].y=d[1];
				pl[i].z=d[2];							 
		 }
		 return pl;
	}
	/**
	 * ��ȡ��ѯ��ƥ�����ĸ���
	 * @param m �������˷��͹�������Ϣ
	 * @return
	 */
	public static int Do_GetQuery_Num(Message m)
	{
		int num=Integer.valueOf(m.getHeader("num")).intValue();
		return num;
	}
	/**
	 * ��ȡ����·���ж�Ӧ�Ľڵ������
	 * @param m
	 * @return
	 * @throws Exception
	 */
	public static TwoDCoordinate[] DoNavigation(Message m) throws Exception {
		int num=Integer.valueOf(m.getHeader("point")).intValue();
		Node temp[]=new Node[num];
		temp=byteToDoubleSome(m.getContentAsBytes(),num);
		TwoDCoordinate[] coordinate=new TwoDCoordinate[num];
		
		for(int i=0;i<num;i++)
		{
			coordinate[i]=new TwoDCoordinate();
			double x=temp[i].coor_x;
			double y=temp[i].coor_y;
			try
			{
				coordinate[i].setXCoordinate(x);
				coordinate[i].setYCoordinate(y);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return coordinate;
	}
	/**
	 * ��ȡ����·���еĽڵ����Ŀ
	 * @param m �ͻ��˽��ܵ��ĵ���·����Ϣ
	 * @return ����·���еĽڵ����Ŀ
	 */
	public static int DoGetNavigateNode_Num(Message m)
	{
		int node_num=Integer.valueOf(m.getHeader("point")).intValue();
		return node_num;
	}

	public static void main(String args[]) throws Exception {

		// create a client to receive messages asynchronously
		// Client rcpt=new Client("0.0.0.0",1234,"receiver");
		MOMClient rcpt = new MOMClient("127.0.0.1", 3009, "receiver");
		// then set the MessageListener for the client
		//

		rcpt.setMessageListener(new MessageListener() {

			public void messageReceived(Message m) {
				try {
					// ��ȡ��Ϣͷ
					if (m.getHeader("type").equals("login")) {
						DoLogin(m);
					}
					if (m.getHeader("type").equals("enroll")) {
						DoEnroll(m);
					} 
					if (m.getHeader("type").equals("locateself")) {
						DoLocateSelf(m);
					}
					if (m.getHeader("type").equals("docterinfo")) {
					//	DoDocterInfo(m);
					}
					if (m.getHeader("type").equals("searchpoint")) {
						DoSearchPoint(m);
					}
					if (m.getHeader("type").equals("navigate")) {
						DoNavigation(m);
					}
					if (m.getHeader("type").equals("firepoint")) {
						DoFirePoint(m);
					}
					else
						// System.out.println(m.getContentAsBytes().toString());
						System.out.println(m.getHeader("type"));
					System.out.println(m.getContent());
					System.out.println(m.getSender());
					System.out.println();

				} catch (Exception ex) {
				}
			}

			public void exceptionRaised(Exception ex) {
			}
		});
		// connect to the dispatcher
		rcpt.connect();
		//
		// wait for user input
		System.in.read(new byte[10]);
		// disconnect
		rcpt.disconnect();
	}
}