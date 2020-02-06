package fcfs;

public class FCFSmain {
	

	public static void main(String[] args) {

		Scheduler s1 = new Scheduler(4);     	//스케줄러 생성. ***생성자 입력 : (프로세스 개수)***
		s1.createProcess();                   	//프로세스 생성
		s1.getReadTime();                      	//RT 설정
		s1.readyQueueing();						//FCFS 준비
		s1.fcfsScheduling();					//FCFS 스케줄링
		s1.showResult();			  			//결과 출력  
		}

}
