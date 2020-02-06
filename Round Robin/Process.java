package roundrobin;

public class Process implements Comparable<Object>{

	protected int pid;
	protected int AT;               //Arrival time  준비 큐에 프로세스가 도착하는 시간. 도달 시간
	protected int RT;            //Burst time 버스트 시간. 프로세스가 작업을 완료하는 데에 필요한 CPU를 점유시간
	protected int WT;            //Wait time     준비 상태에서 dispatch를 기다리는 시간
	protected int TT;            //Total time    모든 프로세스를 처리하는 데에 걸리는 시간 Run time+Wait time
	protected int ATcheck;          //진행중인 시간과 AT를 비교하기 위해 만든 변수
	protected int RTcheck;       //진행중인 시간과 RT를 비교하기 위해 만든 변수
	protected int index;		      //AT 오름차순으로 정렬 시의 순서 index
	protected boolean flag=false;   //AT가 지나고 준비 큐에 도달했는지에 대한 여부
	
	
	@Override             //Sort시 AT가 작은 순서로 정렬
	public int compareTo(Object o) {
		Process p = (Process)o;
		return Integer.compare(this.AT, p.AT);	
	}	
}



