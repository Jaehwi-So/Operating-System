package fcfs;

public class FCFSmain {
	

	public static void main(String[] args) {

		Scheduler s1 = new Scheduler(4);     	//�����ٷ� ����. ***������ �Է� : (���μ��� ����)***
		s1.createProcess();                   	//���μ��� ����
		s1.getReadTime();                      	//RT ����
		s1.readyQueueing();						//FCFS �غ�
		s1.fcfsScheduling();					//FCFS �����ٸ�
		s1.showResult();			  			//��� ���  
		}

}
