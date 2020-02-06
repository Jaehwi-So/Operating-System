package roundrobin;


import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class Scheduler {
	int timeSlice; //타임 슬라이스
	int processNum;   //프로세스 개수
	int totalTime; //모든 프로세스의 RT의 합, 총 CPU 점유시간
	Queue queue;      //대기 큐
	Process selected; //선택된 프로세스
	ArrayList<Process> processArr = new ArrayList<Process>();
		
	//타임 슬라이스와 프로세스의 총 개수를 생성자로 받음	
    Scheduler(int time, int number){
    	this.timeSlice=time;
    	this.processNum=number;   	
    }
   
    //프로세스 개수만큼 프로세스를 생성하고 배열리스트에 추가
    void createProcess(){
    	for(int i=0; i<this.processNum; i++) {
    		processArr.add(new Process());
    		processArr.get(i).pid=i+1;
    		processArr.get(i).AT = (int)(Math.random()*20)+1;
    	}
    }
    
    //프로세스들의 작업시간 입력
    void getReadTime() {
		Scanner sc = new Scanner(System.in);
		for(int i=0; i <this.processNum; i++) { 
			System.out.printf(i+1 + "번쨰 프로세스 작업시간 입력: "); 
			processArr.get(i).RT= sc.nextInt();
			this.totalTime += processArr.get(i).RT;     //작업시간들의 총 시간
		}
		sc.close();
    }
    
    //현재 시간과 프로세스의 RT비교와  대기 큐에 있는 WT계산, 총 TT를 계산한다.
    void timeCalculate(int sequence) {
	    processArr.get(sequence).RTcheck++; //현재 dispatch된 프로세스의 RT와 진행시간을 비교하고  TT를 계산한다.
    	processArr.get(sequence).TT++;
		for(int t=0; t<processNum; t++) {
			//현재 dispatch되지 않고 준비 큐에 있는 프로세스들의 wait time과 turn around time 계산
			if(t!=sequence && processArr.get(t).RT != processArr.get(t).RTcheck && processArr.get(t).flag==true) {
			processArr.get(t).WT++;
			processArr.get(t).TT++;}
		}   	
    }   
    
    //프로세스들의 Arrival time을 계산함과 동시에 Arrival time이 지난 프로세스를 큐에 집어 넣는다.
    void timeCalculateAT() {
    	for(int i=0; i<processNum; i++) {             
    		if(processArr.get(i).flag==false) {
    		processArr.get(i).ATcheck++;  //각 프로세스들마다 AT와 비교하기 위한 ATcheck를 증가시킨다.
    		}  		
    		if(processArr.get(i).ATcheck==processArr.get(i).AT&&processArr.get(i).flag==false) {
    			queue.en_queue(queue, processArr.get(i)); //AT에 도달하였고 준비 큐에 들어오지 않은 프로세스들을 enqueue한다.
    			processArr.get(i).flag=true;  //준비 큐에 도달했다는 flag를 true로 만든다.
    			System.out.printf("(%darrive)", processArr.get(i).pid);   			
    		}
    	}
    }
    
    //라운드 로빈 준비. 큐를 생성하고 큐에 첫번째 프로세스가 들어올 때까지 대기
    void readyQueueing() {
    	Collections.sort(processArr);    //프로세스들을 AT에 따라 오름차순으로 정렬
    	for(int i=0; i<this.processNum; i++) {    //현재 정렬된 프로세스들의 정보 출력
    		processArr.get(i).index=i;
    		System.out.printf("pid : %d, AT : %d RT : %d\n", processArr.get(i).pid, processArr.get(i).AT, processArr.get(i).RT );
    	}   
    	this.queue = new Queue(processNum+1);   //큐 생성
    	queue.init_queue(queue);              //큐 초기화
    }
       
    //준비 큐에 프로세스가 있는지를 확인하는 작업
    void is_have_process() {
       	//만약 준비 큐에 프로세스가 없다면 Arrival time을 계산하며 큐에 프로세스가 도착할 때 까지 while을 통해 대기한다.
		while(queue.is_empty(queue)==true) {
			timeCalculateAT();
		if(queue.is_empty(queue)==true) {
			System.out.printf("*");
			}
		}   	
    }
    
    //라운드 로빈 스케줄링
    void roundRobin() {    	
    	int time = 1;      //현재 진행 시간
    	int allocate = 0;  //현재 프로세스의 타임 슬라이스 할당 시간
    	
    	//*라운드 로빈 초기 시작*
    	is_have_process(); //큐에 프로세스가 있는가의 여부
    	selected=queue.de_queue(queue);    //CPU에서 작업할 프로세스를 dequeue한다.	
    	//진행 시간에 따른 AT, RT, WT, TT를 계산해준다.
    	timeCalculateAT();                   
    	timeCalculate(selected.index);
    	System.out.printf("%d", selected.pid);
		allocate++;  //현재 프로세스의 타임 슬라이스 할당 시간을 계산해준다.
		
		//**************라운드 로빈 알고리즘***************
    	while(time<totalTime) {
    		timeCalculateAT(); //AT 계산
    		
    		//해당 프로세스가 타임 슬라이스를 모두 사용했거나(timeout), 작업이 끝난 경우(exit) 문맥 교환을 해준다.
    		if(allocate>=timeSlice||processArr.get(selected.index).RTcheck>=processArr.get(selected.index).RT) {
    			//타임 슬라이스를 모두 사용했지만 작업이 끝나지 않은 경우(timeout), 다시 준비 큐에 해당 프로세스를 넣어준다.
    			if(allocate>=timeSlice&&processArr.get(selected.index).RTcheck<processArr.get(selected.index).RT) {
    	    			queue.en_queue(queue, processArr.get(selected.index)); 
    				}   						
    			is_have_process();//큐에 프로세스가 있는지를 검사. 없다면 프로세스가 도착할 때 까지 대기   			
    			selected=queue.de_queue(queue); // dispatch, 큐에서 프로세스를 dequeue한다.
    			System.out.printf("%d", selected.pid); 
    			allocate=1; //타임 슬라이스 할당 시간 초기화
    		}  		
    		else {
    		System.out.printf("-");  
    		allocate++; //타임 슬라이스 할당 시간 증가
    		} 
    		
    	time++;   //작업 진행 시간 증가
    	timeCalculate(selected.index);
        }
    	//***********라운드 로빈 알고리즘 종료*****************
    	
    	 System.out.println("완료");
	 }
  
    //결과 출력
	void showResult(){
		System.out.println("(PIDarrive):큐에 해당 프로세스 arrive, 숫자(PID) : 해당 pid가 문맥 교환과 동시에 타임 슬라이스 점유, - : 타임 슬라이스 점유, * : 큐에 프로세스가 도착하지 않음");
		System.out.printf("프로세스 개수 : %d, Time slice : %d\n", processNum, timeSlice);
		for(int i=0; i<this.processNum; i++) {
			System.out.printf("Process[PID : %d] :  AT : %d, RT : %d, WT : %d TT : %d\n", processArr.get(i).pid,processArr.get(i).AT,processArr.get(i).RT,processArr.get(i).WT,processArr.get(i).TT);
		}
	}
}




		
    

