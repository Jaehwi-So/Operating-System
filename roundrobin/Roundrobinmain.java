package roundrobin;

public class Roundrobinmain {
	

	public static void main(String[] args) {

		Scheduler s1 = new Scheduler(5,4);     	//�����ٷ� ����. ***������ �Է� : (Ÿ�� �����̽�/���μ��� ����)***
		s1.createProcess();                   	//���μ��� ����
		s1.getReadTime();                      	//RT ����
		s1.readyQueueing();						//���� �κ� �غ�
		s1.roundRobin();						//���� �κ� �����ٸ�
		s1.showResult();			  			//��� ���  
		}

}
