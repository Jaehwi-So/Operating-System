package roundrobin;


import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class Scheduler {
	int timeSlice; //Ÿ�� �����̽�
	int processNum;   //���μ��� ����
	int totalTime; //��� ���μ����� RT�� ��, �� CPU �����ð�
	Queue queue;      //��� ť
	Process selected; //���õ� ���μ���
	ArrayList<Process> processArr = new ArrayList<Process>();
		
	//Ÿ�� �����̽��� ���μ����� �� ������ �����ڷ� ����	
    Scheduler(int time, int number){
    	this.timeSlice=time;
    	this.processNum=number;   	
    }
   
    //���μ��� ������ŭ ���μ����� �����ϰ� �迭����Ʈ�� �߰�
    void createProcess(){
    	for(int i=0; i<this.processNum; i++) {
    		processArr.add(new Process());
    		processArr.get(i).pid=i+1;
    		processArr.get(i).AT = (int)(Math.random()*20)+1;
    	}
    }
    
    //���μ������� �۾��ð� �Է�
    void getReadTime() {
		Scanner sc = new Scanner(System.in);
		for(int i=0; i <this.processNum; i++) { 
			System.out.printf(i+1 + "���� ���μ��� �۾��ð� �Է�: "); 
			processArr.get(i).RT= sc.nextInt();
			this.totalTime += processArr.get(i).RT;     //�۾��ð����� �� �ð�
		}
		sc.close();
    }
    
    //���� �ð��� ���μ����� RT�񱳿�  ��� ť�� �ִ� WT���, �� TT�� ����Ѵ�.
    void timeCalculate(int sequence) {
	    processArr.get(sequence).RTcheck++; //���� dispatch�� ���μ����� RT�� ����ð��� ���ϰ�  TT�� ����Ѵ�.
    	processArr.get(sequence).TT++;
		for(int t=0; t<processNum; t++) {
			//���� dispatch���� �ʰ� �غ� ť�� �ִ� ���μ������� wait time�� turn around time ���
			if(t!=sequence && processArr.get(t).RT != processArr.get(t).RTcheck && processArr.get(t).flag==true) {
			processArr.get(t).WT++;
			processArr.get(t).TT++;}
		}   	
    }   
    
    //���μ������� Arrival time�� ����԰� ���ÿ� Arrival time�� ���� ���μ����� ť�� ���� �ִ´�.
    void timeCalculateAT() {
    	for(int i=0; i<processNum; i++) {             
    		if(processArr.get(i).flag==false) {
    		processArr.get(i).ATcheck++;  //�� ���μ����鸶�� AT�� ���ϱ� ���� ATcheck�� ������Ų��.
    		}  		
    		if(processArr.get(i).ATcheck==processArr.get(i).AT&&processArr.get(i).flag==false) {
    			queue.en_queue(queue, processArr.get(i)); //AT�� �����Ͽ��� �غ� ť�� ������ ���� ���μ������� enqueue�Ѵ�.
    			processArr.get(i).flag=true;  //�غ� ť�� �����ߴٴ� flag�� true�� �����.
    			System.out.printf("(%darrive)", processArr.get(i).pid);   			
    		}
    	}
    }
    
    //���� �κ� �غ�. ť�� �����ϰ� ť�� ù��° ���μ����� ���� ������ ���
    void readyQueueing() {
    	Collections.sort(processArr);    //���μ������� AT�� ���� ������������ ����
    	for(int i=0; i<this.processNum; i++) {    //���� ���ĵ� ���μ������� ���� ���
    		processArr.get(i).index=i;
    		System.out.printf("pid : %d, AT : %d RT : %d\n", processArr.get(i).pid, processArr.get(i).AT, processArr.get(i).RT );
    	}   
    	this.queue = new Queue(processNum+1);   //ť ����
    	queue.init_queue(queue);              //ť �ʱ�ȭ
    }
       
    //�غ� ť�� ���μ����� �ִ����� Ȯ���ϴ� �۾�
    void is_have_process() {
       	//���� �غ� ť�� ���μ����� ���ٸ� Arrival time�� ����ϸ� ť�� ���μ����� ������ �� ���� while�� ���� ����Ѵ�.
		while(queue.is_empty(queue)==true) {
			timeCalculateAT();
		if(queue.is_empty(queue)==true) {
			System.out.printf("*");
			}
		}   	
    }
    
    //���� �κ� �����ٸ�
    void roundRobin() {    	
    	int time = 1;      //���� ���� �ð�
    	int allocate = 0;  //���� ���μ����� Ÿ�� �����̽� �Ҵ� �ð�
    	
    	//*���� �κ� �ʱ� ����*
    	is_have_process(); //ť�� ���μ����� �ִ°��� ����
    	selected=queue.de_queue(queue);    //CPU���� �۾��� ���μ����� dequeue�Ѵ�.	
    	//���� �ð��� ���� AT, RT, WT, TT�� ������ش�.
    	timeCalculateAT();                   
    	timeCalculate(selected.index);
    	System.out.printf("%d", selected.pid);
		allocate++;  //���� ���μ����� Ÿ�� �����̽� �Ҵ� �ð��� ������ش�.
		
		//**************���� �κ� �˰���***************
    	while(time<totalTime) {
    		timeCalculateAT(); //AT ���
    		
    		//�ش� ���μ����� Ÿ�� �����̽��� ��� ����߰ų�(timeout), �۾��� ���� ���(exit) ���� ��ȯ�� ���ش�.
    		if(allocate>=timeSlice||processArr.get(selected.index).RTcheck>=processArr.get(selected.index).RT) {
    			//Ÿ�� �����̽��� ��� ��������� �۾��� ������ ���� ���(timeout), �ٽ� �غ� ť�� �ش� ���μ����� �־��ش�.
    			if(allocate>=timeSlice&&processArr.get(selected.index).RTcheck<processArr.get(selected.index).RT) {
    	    			queue.en_queue(queue, processArr.get(selected.index)); 
    				}   						
    			is_have_process();//ť�� ���μ����� �ִ����� �˻�. ���ٸ� ���μ����� ������ �� ���� ���   			
    			selected=queue.de_queue(queue); // dispatch, ť���� ���μ����� dequeue�Ѵ�.
    			System.out.printf("%d", selected.pid); 
    			allocate=1; //Ÿ�� �����̽� �Ҵ� �ð� �ʱ�ȭ
    		}  		
    		else {
    		System.out.printf("-");  
    		allocate++; //Ÿ�� �����̽� �Ҵ� �ð� ����
    		} 
    		
    	time++;   //�۾� ���� �ð� ����
    	timeCalculate(selected.index);
        }
    	//***********���� �κ� �˰��� ����*****************
    	
    	 System.out.println("�Ϸ�");
	 }
  
    //��� ���
	void showResult(){
		System.out.println("(PIDarrive):ť�� �ش� ���μ��� arrive, ����(PID) : �ش� pid�� ���� ��ȯ�� ���ÿ� Ÿ�� �����̽� ����, - : Ÿ�� �����̽� ����, * : ť�� ���μ����� �������� ����");
		System.out.printf("���μ��� ���� : %d, Time slice : %d\n", processNum, timeSlice);
		for(int i=0; i<this.processNum; i++) {
			System.out.printf("Process[PID : %d] :  AT : %d, RT : %d, WT : %d TT : %d\n", processArr.get(i).pid,processArr.get(i).AT,processArr.get(i).RT,processArr.get(i).WT,processArr.get(i).TT);
		}
	}
}




		
    


